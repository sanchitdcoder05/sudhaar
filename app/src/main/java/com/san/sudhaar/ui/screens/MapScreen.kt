package com.san.sudhaar.ui.screens

import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.san.sudhaar.model.Report
import com.san.sudhaar.ui.theme.BackgroundLavender
import com.san.sudhaar.ui.theme.DarkText
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    reports: List<Report>,
    currentLocation: GeoPoint?
) {
    val context = LocalContext.current
    Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        Text(
            text = "Map Screen",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier.padding(24.dp),
            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
        )
        
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 80.dp),
            factory = { ctx ->
                MapView(ctx).apply {
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    if (currentLocation != null) {
                        controller.setCenter(currentLocation)
                        val myLocationMarker = Marker(this)
                        myLocationMarker.position = currentLocation
                        myLocationMarker.title = "You are here"
                        // myLocationMarker.icon = ... set custom icon if available
                        overlays.add(myLocationMarker)
                    } else {
                        // Default to some center if not available
                        controller.setCenter(GeoPoint(21.1458, 79.0882)) // Nagpur example
                    }
                    
                    reports.forEach { report ->
                        if (report.location != null) {
                            val marker = Marker(this)
                            marker.position = GeoPoint(report.location.latitude, report.location.longitude)
                            marker.title = report.title
                            marker.snippet = report.description
                            overlays.add(marker)
                        }
                    }
                }
            },
            update = { view ->
                // Updating markers can happen here when data changes
            }
        )
    }
}