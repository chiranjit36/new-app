package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.AttendanceRecord
import com.example.model.CertificateRecord
import com.example.model.CommerceCourse
import com.example.model.Enrollment
import com.example.model.QuizAttempt
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.MonoFinanceFamily
import com.example.viewmodel.UiState

@Composable
fun LearningDashboardScreen(
    enrollmentsState: UiState<List<Enrollment>>,
    attendance: List<AttendanceRecord>,
    quizAttempts: List<QuizAttempt>,
    certificates: List<CertificateRecord>,
    allCourses: List<CommerceCourse>,
    onOpenCourse: (CommerceCourse) -> Unit,
    onExploreCourses: () -> Unit
) {
    val strings = LocalAppStrings.current
    var selectedCertificate by remember { mutableStateOf<CertificateRecord?>(null) }

    val enrollments = (enrollmentsState as? UiState.Success)?.data ?: emptyList()
    val avgQuizScore = if (quizAttempts.isEmpty()) 0 else quizAttempts.map { it.score.toInt() }.average().toInt()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(text = strings.myDashboardTitle, style = MaterialTheme.typography.headlineMedium)
        }

        // KPI Bento Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardKpiCard(
                    title = "Enrolled",
                    value = "${enrollments.size}",
                    accent = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                DashboardKpiCard(
                    title = "Attendance",
                    value = "${attendance.size}",
                    accent = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                DashboardKpiCard(
                    title = "Avg Quiz",
                    value = "$avgQuizScore%",
                    accent = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
                DashboardKpiCard(
                    title = "Certs",
                    value = "${certificates.size}",
                    accent = Color(0xFF0E7C6B),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Custom Canvas Progress Report Chart
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(strings.progressReportsTitle, style = MaterialTheme.typography.titleMedium)
                    val barData = if (enrollments.isNotEmpty()) {
                        enrollments.take(5).map { it.category.take(8) to (it.progressPercent.toFloat() / 100f).coerceAtLeast(0.08f) }
                    } else {
                        listOf("Acct" to 0.15f, "Tax" to 0.1f, "Fin" to 0.1f, "Econ" to 0.1f)
                    }
                    val primaryBar = MaterialTheme.colorScheme.primary
                    val secondaryBar = MaterialTheme.colorScheme.secondary
                    val trackColor = MaterialTheme.colorScheme.surfaceVariant

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        val count = barData.size.coerceAtLeast(1)
                        val spacing = size.width / (count * 2f + 1f)
                        val barWidth = spacing
                        barData.forEachIndexed { idx, (_, pct) ->
                            val x = spacing + idx * (barWidth + spacing)
                            drawRoundRect(
                                color = trackColor,
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, size.height),
                                cornerRadius = CornerRadius(12f, 12f)
                            )
                            val fillHeight = (size.height * pct).coerceAtLeast(12f)
                            drawRoundRect(
                                color = if (idx % 2 == 0) primaryBar else secondaryBar,
                                topLeft = Offset(x, size.height - fillHeight),
                                size = Size(barWidth, fillHeight),
                                cornerRadius = CornerRadius(12f, 12f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        barData.forEach { (label, _) ->
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Enrolled Courses
        item {
            Text("Active Courses", style = MaterialTheme.typography.titleLarge)
        }

        when (enrollmentsState) {
            is UiState.Loading -> {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            is UiState.Error -> {
                item {
                    Text(
                        text = enrollmentsState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            is UiState.Success -> {
                if (enrollments.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_commerce_study),
                                    contentDescription = "Start learning",
                                    modifier = Modifier.height(120.dp).clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Text("No enrolled courses yet", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Join Accounting, CA/CS Foundation, Taxation, or Stock Market tracks to start tracking progress.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Button(onClick = onExploreCourses) {
                                    Text("Browse Courses")
                                }
                            }
                        }
                    }
                } else {
                    items(enrollments, key = { it.courseId }) { enrollment ->
                        val matchingCourse = allCourses.find { it.id == enrollment.courseId }
                        Card(
                            onClick = { matchingCourse?.let(onOpenCourse) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(enrollment.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                    Text(
                                        "${enrollment.progressPercent}%",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontFamily = MonoFinanceFamily
                                    )
                                }
                                Text(enrollment.courseTitle, style = MaterialTheme.typography.titleMedium)
                                LinearProgressIndicator(
                                    progress = { (enrollment.progressPercent / 100f).coerceIn(0f, 1f) },
                                    modifier = Modifier.fillMaxWidth().height(7.dp)
                                )
                                Text(
                                    "${enrollment.completedLessonsCount}/${enrollment.totalLessonsCount} modules completed • Status: ${enrollment.status.uppercase()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Certificates Vault
        item {
            Text(strings.certificatesTitle, style = MaterialTheme.typography.titleLarge)
        }

        if (certificates.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Text(
                            "Complete a batch or score 60%+ on any quiz to earn your verified COMMERCETIME Certificate.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        } else {
            items(certificates, key = { it.id }) { cert ->
                Card(
                    onClick = { selectedCertificate = cert },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_certificate_seal),
                            contentDescription = "Certificate Seal",
                            modifier = Modifier.size(54.dp).clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cert.courseTitle, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "ID: ${cert.certificateNumber} • Score ${cert.scorePercent}%",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Icon(Icons.Filled.School, contentDescription = "View Certificate")
                    }
                }
            }
        }

        // Attendance Log
        item {
            Text(strings.attendanceTitle, style = MaterialTheme.typography.titleLarge)
        }

        if (attendance.isEmpty()) {
            item {
                Text(
                    "Join a live or recorded lesson in any enrolled course to log your attendance automatically.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(attendance.take(8), key = { it.id }) { att ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.EventAvailable, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(att.sessionTitle, style = MaterialTheme.typography.titleSmall)
                        Text("${att.courseTitle} • ${att.sessionType.uppercase()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(50)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(12.dp))
                            Text("Present", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }

    selectedCertificate?.let { cert ->
        Dialog(onDismissRequest = { selectedCertificate = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF8F5)),
                border = BorderStroke(2.dp, Color(0xFFB8831A))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_certificate_seal),
                        contentDescription = "Certificate Crest",
                        modifier = Modifier.size(88.dp).clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        "CERTIFICATE OF EXCELLENCE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFB8831A)
                    )
                    Text(
                        "COMMERCETIME",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF0B2545)
                    )
                    Text(
                        "Apurba Da's Classes • Apurba Sarkar",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF0E7C6B)
                    )
                    HorizontalDivider(color = Color(0xFFD5DCE6))
                    Text("Awarded to", style = MaterialTheme.typography.bodySmall, color = Color(0xFF526075))
                    Text(
                        cert.studentName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF0B2545),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "For successfully completing ${cert.courseTitle} with an assessment score of ${cert.scorePercent}%.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF101B2D),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Certificate ID: ${cert.certificateNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = MonoFinanceFamily,
                        color = Color(0xFF0E7C6B)
                    )
                    Button(onClick = { selectedCertificate = null }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardKpiCard(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = MonoFinanceFamily,
                fontWeight = FontWeight.Bold,
                color = accent
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
