package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CastForEducation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.CatalogData
import com.example.model.CourseFirestoreItem
import com.example.ui.theme.MonoFinanceFamily

@Composable
fun TeacherPanelScreen(
    teacherCourses: List<CourseFirestoreItem>,
    isBusy: Boolean,
    onPublishCourse: (
        title: String,
        category: String,
        description: String,
        priceInr: Long,
        durationHours: Long,
        level: String,
        nextLiveSession: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Accounting") }
    var description by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("1999") }
    var hoursText by remember { mutableStateOf("24") }
    var level by remember { mutableStateOf("Intermediate") }
    var liveSlot by remember { mutableStateOf("Sat 6:00 PM • Live Doubt Clinic") }

    val subjectOptions = CatalogData.categories.filter { it != "All" }
    val levels = listOf("Beginner", "Foundation", "Intermediate", "Advanced")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.CastForEducation, contentDescription = null, tint = Color(0xFFF0C45A))
                        Text("FACULTY STUDIO", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF0C45A))
                    }
                    Text("Teacher Workspace", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text(
                        "Published Courses: ${teacherCourses.size} • Synced live to COMMERCETIME catalog",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Create & Publish Course Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.AddCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Text("Publish New Commerce Course", style = MaterialTheme.typography.titleMedium)
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Course Title") },
                        placeholder = { Text("e.g., Ind AS Consolidated Balance Sheets") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("teacher_course_title")
                    )

                    Text("Commerce Subject", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(subjectOptions) { subj ->
                            FilterChip(
                                selected = category == subj,
                                onClick = { category = subj },
                                label = { Text(subj) }
                            )
                        }
                    }

                    Text("Difficulty Level", style = MaterialTheme.typography.labelMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(levels) { lvl ->
                            FilterChip(
                                selected = level == lvl,
                                onClick = { level = lvl },
                                label = { Text(lvl) }
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Fee (₹)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = hoursText,
                            onValueChange = { hoursText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Duration (hrs)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = liveSlot,
                        onValueChange = { liveSlot = it },
                        label = { Text("Live Class Schedule") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Course Syllabus & Outcomes") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth().testTag("teacher_course_desc")
                    )

                    Button(
                        onClick = {
                            onPublishCourse(
                                title,
                                category,
                                description,
                                priceText.toLongOrNull() ?: 1499L,
                                hoursText.toLongOrNull() ?: 20L,
                                level,
                                liveSlot
                            )
                            if (title.trim().length >= 3 && description.trim().length >= 10) {
                                title = ""
                                description = ""
                            }
                        },
                        enabled = !isBusy,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("publish_course_button")
                    ) {
                        Text("Publish Course Live")
                    }
                }
            }
        }

        item {
            Text("My Published Courses (${teacherCourses.size})", style = MaterialTheme.typography.titleLarge)
        }

        if (teacherCourses.isEmpty()) {
            item {
                Text(
                    "You haven't published a custom course yet. Use the studio form above to launch one!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(teacherCourses, key = { it.id }) { tc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(6.dp)) {
                                Text(tc.category, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                            Text("₹${tc.priceInr}", fontFamily = MonoFinanceFamily, style = MaterialTheme.typography.titleSmall)
                        }
                        Text(tc.title, style = MaterialTheme.typography.titleMedium)
                        Text(tc.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (!tc.nextLiveSession.isNullOrBlank()) {
                            Text("Live: ${tc.nextLiveSession}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
