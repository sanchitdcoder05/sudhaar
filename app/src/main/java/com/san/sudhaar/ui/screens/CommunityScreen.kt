package com.san.sudhaar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.model.Report
import com.san.sudhaar.ui.components.ReportCard
import com.san.sudhaar.ui.theme.BackgroundLavender
import com.san.sudhaar.ui.theme.DarkText

@Composable
fun CommunityScreen(
    reports: List<Report>,
    currentUserId: String?,
    onVoteClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Community Feed",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier.padding(horizontal = 24.dp),
            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(reports) { report ->
                ReportCard(
                    report = report,
                    currentUserId = currentUserId,
                    onVoteClick = { onVoteClick(report.id) },
                    showAuthor = true,
                    authorName = "Anonymous"
                )
            }
        }
    }
}