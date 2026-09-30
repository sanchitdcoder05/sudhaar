package com.san.sudhaar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.model.Report
import com.san.sudhaar.ui.components.ReportCard
import com.san.sudhaar.ui.theme.BackgroundLavender
import com.san.sudhaar.ui.theme.DarkText

@Composable
fun ProfileScreen(
    reports: List<Report>,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Reported Problems",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )
            
            Text(
                text = "Logout",
                fontSize = 16.sp,
                color = DarkText,
                modifier = Modifier.clickable { onLogoutClick() },
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No reports yet.",
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    color = DarkText
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reports) { report ->
                    ReportCard(
                        report = report,
                        currentUserId = null,
                        showAuthor = false
                    )
                }
            }
        }
    }
}