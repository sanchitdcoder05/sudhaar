package com.san.sudhaar.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.san.sudhaar.R
import com.san.sudhaar.model.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.osmdroid.util.GeoPoint
import java.security.MessageDigest
import java.util.Locale
import java.util.UUID

class AppViewModel : ViewModel() {
    private val auth: FirebaseAuth? = try { FirebaseAuth.getInstance() } catch (e: Exception) { Log.e("AppViewModel", "Auth Init Error", e); null }
    private val db: FirebaseFirestore? = try { FirebaseFirestore.getInstance() } catch (e: Exception) { Log.e("AppViewModel", "Firestore Init Error", e); null }

    private val _currentUserUid = MutableStateFlow<String?>(auth?.currentUser?.uid)
    val currentUserUid: StateFlow<String?> = _currentUserUid

    private val _currentUserName = MutableStateFlow<String>(auth?.currentUser?.displayName ?: "User")
    val currentUserName: StateFlow<String> = _currentUserName
    
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports
    
    private val _myReports = MutableStateFlow<List<Report>>(emptyList())
    val myReports: StateFlow<List<Report>> = _myReports
    
    private val _currentLocation = MutableStateFlow<String>("Fetching location...")
    val currentLocation: StateFlow<String> = _currentLocation

    private val _currentGeoPoint = MutableStateFlow<GeoPoint?>(null)
    val currentGeoPoint: StateFlow<GeoPoint?> = _currentGeoPoint

    init {
        loadMockDataIfNoFirebase()
        if (db != null) {
            fetchReports()
            fetchMyReports()
        }
    }

    private fun loadMockDataIfNoFirebase() {
        if (db == null) {
            Log.d("AppViewModel", "Using Mock Data because Firebase is not initialized")
            _reports.value = listOf(
                Report(
                    id = "mock_1",
                    title = "Garbage",
                    description = "there are garbage",
                    type = "Garbage",
                    locationName = "Dhulkot Road, Mehrekā Gaun",
                    status = 0,
                    votes = 2
                ),
                Report(
                    id = "mock_2",
                    title = "Urgent Request to Repair Dangerous Pothole",
                    description = "The pothole has been present for several weeks...",
                    type = "Pothole",
                    locationName = "Wanadongri ct, Wanadongri",
                    status = 1,
                    votes = 3
                )
            )
            _myReports.value = _reports.value
            _currentLocation.value = "Wanadongri ct, Wanadongri"
        }
    }

    fun login(email: String, pwd: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (auth == null) {
            Log.d("AppViewModel", "Mock Login")
            _currentUserUid.value = "mock_uid"
            _currentUserName.value = email.substringBefore("@")
            onSuccess()
            return
        }
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, pwd).await()
                _currentUserUid.value = auth.currentUser?.uid
                _currentUserName.value = auth.currentUser?.displayName ?: "User"
                fetchMyReports()
                onSuccess()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Login Error", e)
                onError(e.localizedMessage ?: "Login failed")
            }
        }
    }
    
    fun loginWithGoogle(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (auth == null) {
            onError("Firebase Auth not initialized")
            return
        }
        val credentialManager = CredentialManager.create(context)
        val webClientId = context.getString(R.string.default_web_client_id)
        
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setNonce(hashedNonce)
            .build()
            
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(context = context, request = request)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    auth.signInWithCredential(firebaseCredential).await()
                    
                    _currentUserUid.value = auth.currentUser?.uid
                    _currentUserName.value = auth.currentUser?.displayName ?: "User"
                    fetchMyReports()
                    onSuccess()
                } else {
                    onError("Unexpected credential type")
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Google Sign-In failed", e)
                onError(e.message ?: "Google Sign-In failed")
            }
        }
    }

    fun register(name: String, email: String, pwd: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (auth == null) {
            Log.d("AppViewModel", "Mock Register")
            _currentUserUid.value = "mock_uid"
            _currentUserName.value = name
            onSuccess()
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, pwd).await()
                _currentUserUid.value = auth.currentUser?.uid
                _currentUserName.value = name
                onSuccess()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Register Error", e)
                onError(e.localizedMessage ?: "Registration failed")
            }
        }
    }
    
    fun logout() {
        auth?.signOut()
        _currentUserUid.value = null
        _currentUserName.value = "User"
        _myReports.value = emptyList()
    }

    fun fetchLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _currentLocation.value = "Location permission denied"
            return
        }
        
        try {
            _currentLocation.value = "Fetching location..."
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            
            // Use getCurrentLocation instead of lastLocation for better accuracy and triggering GPS when null
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        _currentGeoPoint.value = GeoPoint(location.latitude, location.longitude)
                        viewModelScope.launch(Dispatchers.IO) {
                            try {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                if (!addresses.isNullOrEmpty()) {
                                    val address = addresses[0]
                                    val locName = listOfNotNull(address.subLocality ?: address.locality, address.adminArea ?: address.countryName)
                                        .joinToString(", ")
                                    _currentLocation.value = locName.ifEmpty { "${location.latitude}, ${location.longitude}" }
                                } else {
                                    _currentLocation.value = "${location.latitude}, ${location.longitude}"
                                }
                            } catch (e: Exception) {
                                _currentLocation.value = "${location.latitude}, ${location.longitude}"
                            }
                        }
                    } else {
                        _currentLocation.value = "Location not found"
                    }
                }.addOnFailureListener {
                    _currentLocation.value = "Location failed: ${it.message}"
                }
        } catch (e: Exception) {
            Log.e("AppViewModel", "Location error", e)
            _currentLocation.value = "Location error"
        }
    }

    fun submitReport(title: String, description: String, category: String, priority: String, onSuccess: () -> Unit) {
        val geo = _currentGeoPoint.value
        val report = Report(
            title = title,
            description = description,
            type = category,
            priority = priority,
            locationName = _currentLocation.value,
            location = if (geo != null) com.google.firebase.firestore.GeoPoint(geo.latitude, geo.longitude) else null,
            uid = _currentUserUid.value ?: "anonymous",
            status = 0,
            timestamp = Timestamp.now()
        )
        if (db == null) {
            _myReports.value = listOf(report) + _myReports.value
            _reports.value = listOf(report) + _reports.value
            onSuccess()
            return
        }
        viewModelScope.launch {
            try {
                db.collection("reports").document(report.id).set(report).await()
                fetchReports()
                fetchMyReports()
                onSuccess()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Submit Report Error", e)
            }
        }
    }

    fun voteReport(reportId: String) {
        val uid = _currentUserUid.value ?: return
        if (db == null) {
            _reports.value = _reports.value.map {
                if (it.id == reportId) {
                    val voted = it.votedBy.contains(uid)
                    it.copy(
                        votes = if (voted) it.votes - 1 else it.votes + 1,
                        votedBy = if (voted) it.votedBy - uid else it.votedBy + uid
                    )
                } else it
            }
            return
        }
        
        viewModelScope.launch {
            try {
                db.runTransaction { transaction ->
                    val docRef = db.collection("reports").document(reportId)
                    val snapshot = transaction.get(docRef)
                    if (!snapshot.exists()) return@runTransaction
                    
                    // Safely get list to avoid null pointer exceptions from Firestore
                    val votedByList = snapshot.get("votedBy") as? List<String> ?: emptyList()
                    val currentVotes = snapshot.getLong("votes") ?: 0L
                    val voted = votedByList.contains(uid)
                    
                    val newVotes = if (voted) currentVotes - 1 else currentVotes + 1
                    val newVotedBy = if (voted) votedByList - uid else votedByList + uid
                    
                    transaction.update(docRef, "votes", newVotes)
                    transaction.update(docRef, "votedBy", newVotedBy)
                }.await()
                
                fetchReports()
                fetchMyReports()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Vote Error", e)
            }
        }
    }

    private fun fetchReports() {
        if (db == null) return
        viewModelScope.launch {
            try {
                Log.d("AppViewModel", "Fetching reports...")
                val snapshot = db.collection("reports")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()
                _reports.value = snapshot.toObjects(Report::class.java)
                Log.d("AppViewModel", "Fetched ${_reports.value.size} reports")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Fetch Reports Error", e)
            }
        }
    }

    private fun fetchMyReports() {
        if (db == null) return
        val uid = _currentUserUid.value ?: return
        viewModelScope.launch {
            try {
                Log.d("AppViewModel", "Fetching my reports...")
                val snapshot = db.collection("reports")
                    .whereEqualTo("uid", uid)
                    .get()
                    .await()
                _myReports.value = snapshot.toObjects(Report::class.java).sortedByDescending { it.timestamp }
                Log.d("AppViewModel", "Fetched ${_myReports.value.size} my reports")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Fetch My Reports Error", e)
            }
        }
    }
}