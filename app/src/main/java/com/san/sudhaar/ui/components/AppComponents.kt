package com.san.sudhaar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.model.Report
import com.san.sudhaar.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeActionCard(
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    iconBackgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBackgroundColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(24.dp),
                        tint = DarkText
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = title,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText
            )
            Text(
                text = subtitle,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                color = GrayText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ReportStatusProgress(status: Int) {
    val steps = listOf("Reported", "Validated", "Working", "Completed")
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isCompleted = index <= status
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            if (isCompleted) StatusGreen else StatusGray,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = step,
                    fontSize = 10.sp,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = DarkText,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
            
            if (index < steps.size - 1) {
                Divider(
                    color = if (index < status) StatusGreen else StatusGray,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp),
                    thickness = 2.dp
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    report: Report,
    currentUserId: String? = null,
    onVoteClick: () -> Unit = {},
    showAuthor: Boolean = false,
    authorName: String = "Anonymous"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (showAuthor) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.LightGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = authorName,
                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    Text(
                        text = report.title,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DarkText
                    )
                    
                    if (report.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = report.description,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            color = DarkText,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    val dateStr = report.timestamp?.toDate()?.let {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
                    } ?: "Just now"
                    
                    Text(
                        text = dateStr,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        color = GrayText,
                        fontSize = 12.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val hasVoted = currentUserId != null && report.votedBy.contains(currentUserId)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onVoteClick() }
                    ) {
                        Text(
                            text = "${report.votes} Votes",
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (hasVoted) PrimaryPurple else DarkText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (hasVoted) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Vote",
                            tint = if (hasVoted) PrimaryPurple else GrayText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Location icon placeholder
                Text(
                    text = "📍 ${report.locationName}",
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = GrayText
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ReportStatusProgress(status = report.status)
        }
    }
}