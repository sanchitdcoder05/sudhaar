package com.san.sudhaar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.model.Report
import com.san.sudhaar.ui.components.HomeActionCard
import com.san.sudhaar.ui.components.ReportCard
import com.san.sudhaar.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    currentLocation: String,
    recentReports: List<Report>,
    onReportIssueClick: () -> Unit,
    onIssuesNearMeClick: () -> Unit,
    onMyComplaintsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onReportClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(PrimaryPurple)
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sudhaar",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
                        )
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Hi $userName! 👋",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.headlineMedium.fontFamily
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📍 $currentLocation",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Grid of 4 actions
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeActionCard(
                            title = "Report Issue",
                            subtitle = "Raise civic problems",
                            iconBackgroundColor = PastelBlue,
                            onClick = onReportIssueClick
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeActionCard(
                            title = "Issues near me",
                            subtitle = "Check local problems",
                            iconBackgroundColor = PastelGreen,
                            onClick = onIssuesNearMeClick
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeActionCard(
                            title = "My Complaints",
                            subtitle = "Track your reports",
                            iconBackgroundColor = PastelYellow,
                            onClick = onMyComplaintsClick
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HomeActionCard(
                            title = "Alerts & News",
                            subtitle = "Stay updated",
                            iconBackgroundColor = PastelPink,
                            onClick = onAlertsClick
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Nearby Complaints",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        items(recentReports) { report ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                ReportCard(
                    report = report,
                    currentUserId = null,
                    showAuthor = true,
                    authorName = report.uid.take(5) // Mock author name
                )
            }
        }
    }
}