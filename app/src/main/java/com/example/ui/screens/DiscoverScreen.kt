package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.CatalogData
import com.example.model.CommerceCourse
import com.example.model.Enrollment
import com.example.model.SubscriptionPlanOption
import com.example.model.TeacherProfile
import com.example.model.UserProfile
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.MonoFinanceFamily

@Composable
fun DiscoverScreen(
    userProfile: UserProfile?,
    courses: List<CommerceCourse>,
    enrollments: List<Enrollment>,
    selectedCategory: String,
    searchQuery: String,
    onSelectCategory: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onOpenCourse: (CommerceCourse) -> Unit,
    onOpenAiMentor: () -> Unit,
    onPurchaseSubscription: (SubscriptionPlanOption) -> Unit
) {
    val strings = LocalAppStrings.current
    val enrolledIds = remember(enrollments) { enrollments.map { it.courseId }.toSet() }
    val liveCourses = remember(courses) { courses.filter { it.isLiveNow } }
    var selectedTeacher by remember { mutableStateOf<TeacherProfile?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Editorial Welcome Hero
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .clickable(onClick = onOpenAiMentor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_academy),
                    contentDescription = "Commerce Classes Hub academy",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xEC0B2545), Color(0x880B2545))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFF0C45A),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "WELCOME, ${(userProfile?.displayName ?: "SCHOLAR").uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFF0C45A)
                        )
                    }
                    Text(
                        text = "Master Accounting, Tax & Markets",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Text(
                        text = "Wallet Credits: ₹${userProfile?.walletCredits ?: 500} • Streak: ${userProfile?.studyStreakDays ?: 1}d",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFBAE6FD)
                    )
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(strings.searchCoursesHint) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("course_search_input")
            )
        }

        // 8 Commerce Category Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CatalogData.categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectCategory(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("category_chip_${category.lowercase().replace(" ", "_")}")
                    )
                }
            }
        }

        // Live Classes Strip
        if (liveCourses.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.liveClassesTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LiveTv,
                                    contentDescription = null,
                                    tint = Color(0xFFB91C1C),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${liveCourses.size} LIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFB91C1C)
                                )
                            }
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(liveCourses, key = { "live_${it.id}" }) { liveCourse ->
                            Card(
                                onClick = { onOpenCourse(liveCourse) },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.width(265.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = liveCourse.category.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = liveCourse.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = liveCourse.nextLiveSession,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "By ${liveCourse.teacherName}",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Featured Courses Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.featuredCoursesTitle,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${courses.size} tracks",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (courses.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_commerce_study),
                            contentDescription = "No courses found",
                            modifier = Modifier
                                .height(130.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "No matching courses found",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(onClick = {
                            onSelectCategory("All")
                            onSearchChange("")
                        }) {
                            Text("Reset filters")
                        }
                    }
                }
            }
        } else {
            items(courses, key = { it.id }) { course ->
                CourseCatalogCard(
                    course = course,
                    isEnrolled = course.id in enrolledIds,
                    enrollLabel = strings.enrollNow,
                    enrolledBadge = strings.enrolledBadge,
                    onClick = { onOpenCourse(course) }
                )
            }
        }

        // Faculty Spotlight
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = strings.facultyTitle,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(CatalogData.teachers, key = { it.id }) { teacher ->
                        Card(
                            onClick = { selectedTeacher = teacher },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.width(220.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = teacher.name.take(2).uppercase(),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = teacher.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = teacher.credentials,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Text(
                                    text = teacher.specialty,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "${teacher.rating} • ${teacher.experienceYears} yrs exp",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Subscription Plans
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = strings.subscriptionPlansTitle,
                    style = MaterialTheme.typography.titleLarge
                )
                CatalogData.subscriptionPlans.forEach { plan ->
                    val isActive = userProfile?.subscriptionPlan == plan.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (plan.id == "all_access_annual") {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = if (plan.id == "all_access_annual") null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        val contentColor = if (plan.id == "all_access_annual") {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color(0xFFF0C45A)
                                    )
                                    Text(
                                        text = plan.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = contentColor
                                    )
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (isActive) "ACTIVE" else plan.badge,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (plan.priceInr == 0) "Free Forever" else "₹${plan.priceInr} / ${plan.billingCycle}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontFamily = MonoFinanceFamily,
                                color = contentColor
                            )
                            plan.perks.forEach { perk ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF34D399),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = perk,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = contentColor.copy(alpha = 0.9f)
                                    )
                                }
                            }
                            if (!isActive && plan.priceInr > 0) {
                                Button(
                                    onClick = { onPurchaseSubscription(plan) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Upgrade to ${plan.name}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Teacher Profile Dialog
    selectedTeacher?.let { teacher ->
        Dialog(onDismissRequest = { selectedTeacher = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = teacher.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = teacher.credentials,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "${teacher.experienceYears} years experience • ${teacher.studentsTaught} learners • ★ ${teacher.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(text = teacher.bio, style = MaterialTheme.typography.bodyMedium)
                    Button(
                        onClick = { selectedTeacher = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseCatalogCard(
    course: CommerceCourse,
    isEnrolled: Boolean,
    enrollLabel: String,
    enrolledBadge: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("course_card_${course.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = course.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${course.ratingAvg} (${course.enrolledCount})",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${course.teacherName} • ${course.level}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${course.durationHours}h",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹${course.priceInr}",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = MonoFinanceFamily,
                        fontWeight = FontWeight.Bold
                    )
                    if (course.originalPriceInr > course.priceInr) {
                        Text(
                            text = "₹${course.originalPriceInr}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = if (isEnrolled) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isEnrolled) enrolledBadge else enrollLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isEnrolled) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        },
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
