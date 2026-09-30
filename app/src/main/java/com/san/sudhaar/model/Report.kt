package com.san.sudhaar.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.UUID

data class Report(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val priority: String = "Low",
    val imageURL: String? = null,
    val videoURL: String? = null,
    val audioURL: String? = null,
    val location: GeoPoint? = null,
    val locationName: String = "",
    val status: Int = 0,
    val uid: String = "",
    val timestamp: Timestamp? = null,
    val votedBy: List<String> = emptyList(),
    val votes: Int = 0
)

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null
)