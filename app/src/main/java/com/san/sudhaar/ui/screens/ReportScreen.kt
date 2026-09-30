package com.san.sudhaar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.san.sudhaar.ui.components.PrimaryButton
import com.san.sudhaar.ui.components.RoundedTextField
import com.san.sudhaar.ui.theme.*

@Composable
fun ReportScreen(
    currentLocation: String,
    onSubmitClick: (title: String, description: String, category: String, priority: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Pothole") }
    var selectedPriority by remember { mutableStateOf("Low Priority") }
    
    val categories = listOf("Pothole", "Garbage", "Streetlight", "Water Leak", "Sewage Block", "Other")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Report an Issue",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            fontFamily = MaterialTheme.typography.displayLarge.fontFamily
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title Section
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Title",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                RoundedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "e.g., Large Pothole on Main Street"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add Evidence
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add Evidence",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    EvidenceButton("Take Photo", Modifier.weight(1f))
                    EvidenceButton("Record Video", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    EvidenceButton("From Gallery", Modifier.weight(1f))
                    EvidenceButton("Voice Note", Modifier.weight(1f))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Location
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📍 Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentLocation.ifEmpty { "Location unavailable" },
                    fontSize = 16.sp,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Category
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Use a simple FlowRow alternative using rows since FlowRow is experimental
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryChip("Pothole", selectedCategory) { selectedCategory = it }
                        CategoryChip("Garbage", selectedCategory) { selectedCategory = it }
                        CategoryChip("Streetlight", selectedCategory) { selectedCategory = it }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryChip("Water Leak", selectedCategory) { selectedCategory = it }
                        CategoryChip("Sewage Block", selectedCategory) { selectedCategory = it }
                        CategoryChip("Other", selectedCategory) { selectedCategory = it }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Priority Level
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Priority Level",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                PriorityRadio("Low Priority", "Can wait for scheduled maintenance", selectedPriority) { selectedPriority = it }
                Spacer(modifier = Modifier.height(8.dp))
                PriorityRadio("Medium Priority", "Should be addressed soon", selectedPriority) { selectedPriority = it }
                Spacer(modifier = Modifier.height(8.dp))
                PriorityRadio("High Priority", "Requires immediate attention", selectedPriority) { selectedPriority = it }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        Card(
            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Description",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))
                RoundedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Describe the issue in detail...",
                    singleLine = false,
                    minLines = 4
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        PrimaryButton(
            text = "Submit Report",
            onClick = { onSubmitClick(title, description, selectedCategory, selectedPriority) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EvidenceButton(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(60.dp)
            .border(1.dp, OutlineGray, RoundedCornerShape(12.dp))
            .clickable { /* TODO */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            color = DarkText,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryChip(title: String, selectedCategory: String, onSelect: (String) -> Unit) {
    val isSelected = title == selectedCategory
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) PrimaryPurple else OutlineGray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) PrimaryPurple.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(title) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) PrimaryPurple else DarkText,
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun PriorityRadio(title: String, subtitle: String, selectedPriority: String, onSelect: (String) -> Unit) {
    val isSelected = title == selectedPriority
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onSelect(title) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(title) },
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple)
        )
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = GrayText,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
            )
        }
    }
}