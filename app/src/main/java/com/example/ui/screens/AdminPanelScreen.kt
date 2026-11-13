package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CatalogData
import com.example.model.CommerceCourse
import com.example.model.Enrollment
import com.example.model.PaymentRecord
import com.example.ui.theme.MonoFinanceFamily

@Composable
fun AdminPanelScreen(
    isVerifiedAdmin: Boolean,
    allCourses: List<CommerceCourse>,
    enrollments: List<Enrollment>,
    payments: List<PaymentRecord>
) {
    val totalRevenue = payments.sumOf { it.amountInr }
    val customCoursesCount = allCourses.count { it.isUserCreated }

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
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, tint = Color(0xFFF0C45A))
                        Text("EXECUTIVE CONSOLE", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF0C45A))
                    }
                    Text("Administrator Panel", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Surface(
                        color = if (isVerifiedAdmin) Color(0xFF0E7C6B) else Color(0x33FFFFFF),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.padding(end = 2.dp))
                            Text(
                                text = if (isVerifiedAdmin) "Verified Root Admin" else "Read-Only Executive Preview",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Executive KPIs
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminMetricCard("Active Courses", "${allCourses.size}", Modifier.weight(1f))
                AdminMetricCard("Faculty", "${CatalogData.teachers.size}", Modifier.weight(1f))
                AdminMetricCard("Payments", "₹$totalRevenue", Modifier.weight(1f))
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Platform Governance Summary", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "• ${CatalogData.categories.size - 1} core commerce verticals active (Accounting, Business, Economics, Finance, Tax, CA/CS, Stock Market)",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "• $customCoursesCount teacher-published courses synced via Firestore",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "• ${enrollments.size} active enrollments & ${payments.size} verified payment receipts in current workspace",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "• Zero-trust Firestore security rules & App Check attestation enforced",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        item {
            Text("Catalog Inventory (${allCourses.size})", style = MaterialTheme.typography.titleLarge)
        }

        items(allCourses, key = { "admin_${it.id}" }) { course ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(course.title, style = MaterialTheme.typography.titleSmall)
                        Text(
                            "${course.category} • ${course.teacherName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = if (course.isUserCreated) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (course.isUserCreated) "FACULTY" else "CURATED",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = MonoFinanceFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
