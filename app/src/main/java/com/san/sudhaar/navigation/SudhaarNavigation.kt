package com.san.sudhaar.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.san.sudhaar.ui.screens.*
import com.san.sudhaar.ui.theme.DarkText
import com.san.sudhaar.ui.theme.PrimaryPurple
import com.san.sudhaar.viewmodel.AppViewModel
import org.osmdroid.util.GeoPoint

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Report : Screen("report", "Report", Icons.Default.Add)
    object Map : Screen("map", "Map", Icons.Default.LocationOn)
    object Community : Screen("community", "Community", Icons.Default.People)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Report,
    Screen.Map,
    Screen.Community,
    Screen.Profile
)

@Composable
fun SudhaarApp(viewModel: AppViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    
    val showBottomNav = currentRoute in bottomNavScreens.map { it.route }
    val currentUserUid by viewModel.currentUserUid.collectAsState()
    
    // Remember login state to bypass login screen if already logged in
    val startDestination = if (currentUserUid != null) Screen.Home.route else Screen.Login.route

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.any { it.value } // Either fine or coarse is fine
        if (granted) {
            viewModel.fetchLocation(context)
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Automatically trigger location fetching once when logged in
    LaunchedEffect(currentUserUid) {
        if (currentUserUid != null) {
            val hasFineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarseLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            
            if (hasFineLoc || hasCoarseLoc) {
                viewModel.fetchLocation(context)
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginClick = { email, pwd ->
                        viewModel.login(email, pwd, onSuccess = {
                            Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }, onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        })
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    onGoogleSignInClick = {
                        viewModel.loginWithGoogle(context, onSuccess = {
                            Toast.makeText(context, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }, onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        })
                    },
                    onForgotPwdClick = {
                        Toast.makeText(context, "Password reset not yet implemented", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterClick = { name, email, pwd ->
                        viewModel.register(name, email, pwd, onSuccess = {
                            Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }, onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        })
                    },
                    onLoginClick = {
                        navController.popBackStack()
                    },
                    onGoogleSignInClick = {
                        viewModel.loginWithGoogle(context, onSuccess = {
                            Toast.makeText(context, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }, onError = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        })
                    }
                )
            }
            
            composable(Screen.Home.route) {
                val userName by viewModel.currentUserName.collectAsState()
                val currentLocation by viewModel.currentLocation.collectAsState()
                val reports by viewModel.reports.collectAsState()
                
                HomeScreen(
                    userName = userName,
                    currentLocation = currentLocation,
                    recentReports = reports.take(2),
                    onReportIssueClick = { navController.navigate(Screen.Report.route) },
                    onIssuesNearMeClick = { navController.navigate(Screen.Map.route) },
                    onMyComplaintsClick = { navController.navigate(Screen.Profile.route) },
                    onAlertsClick = { Toast.makeText(context, "Alerts & News clicked", Toast.LENGTH_SHORT).show() },
                    onReportClick = { /* TODO */ }
                )
            }
            
            composable(Screen.Report.route) {
                val currentLocation by viewModel.currentLocation.collectAsState()
                ReportScreen(
                    currentLocation = currentLocation,
                    onSubmitClick = { title, desc, cat, prio ->
                        if (title.isBlank() || desc.isBlank()) {
                            Toast.makeText(context, "Please fill in all details", Toast.LENGTH_SHORT).show()
                            return@ReportScreen
                        }
                        Toast.makeText(context, "Submitting report...", Toast.LENGTH_SHORT).show()
                        viewModel.submitReport(title, desc, cat, prio, onSuccess = {
                            Toast.makeText(context, "Report submitted!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Home.route)
                            }
                        })
                    }
                )
            }
            
            composable(Screen.Map.route) {
                val reports by viewModel.reports.collectAsState()
                val currentGeoPoint by viewModel.currentGeoPoint.collectAsState()
                val defaultLocation = currentGeoPoint ?: GeoPoint(21.1458, 79.0882) 
                MapScreen(
                    reports = reports,
                    currentLocation = defaultLocation
                )
            }
            
            composable(Screen.Community.route) {
                val reports by viewModel.reports.collectAsState()
                val currentUserId by viewModel.currentUserUid.collectAsState()
                CommunityScreen(
                    reports = reports,
                    currentUserId = currentUserId,
                    onVoteClick = { reportId ->
                        viewModel.voteReport(reportId)
                    }
                )
            }
            
            composable(Screen.Profile.route) {
                val myReports by viewModel.myReports.collectAsState()
                ProfileScreen(
                    reports = myReports,
                    onLogoutClick = {
                        viewModel.logout()
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        bottomNavScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                label = { Text(screen.title) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    unselectedIconColor = DarkText,
                    selectedTextColor = PrimaryPurple,
                    unselectedTextColor = DarkText,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}