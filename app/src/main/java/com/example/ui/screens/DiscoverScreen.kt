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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
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

private data class GalleryPhoto(
    val resId: Int,
    val title: String,
    val subtitle: String
)

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

    val galleryPhotos = remember {
        listOf(
            GalleryPhoto(
                resId = R.drawable.img_ct_banner,
                title = "COMMERCETIME Coaching Center",
                subtitle = "Class XI, XII, B.Com, BBA, MBA & CA Foundation"
            ),
            GalleryPhoto(
                resId = R.drawable.img_apurba_podium,
                title = "Apurba Sarkar (M.COM, MBA)",
                subtitle = "14+ Years Teaching Experience • Purulia"
            ),
            GalleryPhoto(
                resId = R.drawable.img_apurba_students,
                title = "Teachers Day Celebration",
                subtitle = "Apurba Da with COMMERCETIME students"
            ),
            GalleryPhoto(
                resId = R.drawable.img_apurba_stage,
                title = "Annual Commerce Meet",
                subtitle = "Student awards & mentorship session"
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Clean COMMERCETIME Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(206.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .clickable(onClick = onOpenAiMentor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_ct_banner),
                    contentDescription = "COMMERCETIME banner featuring Apurba Sarkar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x440B2545), Color(0xF00B2545))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "APURBA DA'S CLASSES • CLASS 11, 12 & B.COM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "COMMERCETIME",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Teach By: Apurba Sarkar (M.COM, MBA) • 14+ Years Exp",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE2E8F0)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Credits: ₹${userProfile?.walletCredits ?: 500}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFF0C45A),
                            fontFamily = MonoFinanceFamily
                        )
                        Text(
                            text = "• Tap for 24x7 AI Assistant",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFBAE6FD)
                        )
                    }
                }
            }
        }

        // 2. Full Simple Home — Flagship Paid Batches (₹99, ₹149, ₹199, ₹249)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Paid Batches — Apurba Sarkar",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Simple & affordable Class 11, 12 & B.Com full access",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Exact 4 Paid Batches in a clean 2x2 grid or stacked compact rows
                val batchesToShow = CatalogData.curatedCourses.filter { it.id in CatalogData.flagshipBatchIds }
                batchesToShow.forEach { batch ->
                    val isEnrolled = batch.id in enrolledIds
                    val shortTag = when (batch.id) {
                        "batch_class_11_12" -> "CLASS 11 & 12 BOTH"
                        "batch_bcom_1st_year" -> "B.COM 1ST YEAR (BOTH SEM)"
                        "batch_bcom_1_2_year" -> "B.COM 1ST & 2ND YEAR"
                        "batch_bcom_3_year" -> "B.COM ALL 3 YEARS FULL"
                        else -> batch.category.uppercase()
                    }
                    Card(
                        onClick = { onOpenCourse(batch) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (batch.id == "batch_bcom_3_year") {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (batch.id == "batch_bcom_3_year") {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("flagship_batch_${batch.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.School,
                                    contentDescription = null,
                                    tint = Color(0xFFF0C45A)
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = shortTag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = batch.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "By ${batch.teacherName} • ${batch.durationHours}h classes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    color = if (isEnrolled) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (isEnrolled) strings.enrolledBadge else "₹${batch.priceInr} Only",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontFamily = MonoFinanceFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isEnrolled) {
                                            MaterialTheme.colorScheme.onSecondaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onPrimary
                                        },
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                    )
                                }
                                if (batch.originalPriceInr > batch.priceInr && !isEnrolled) {
                                    Text(
                                        text = "₹${batch.originalPriceInr}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Simple Search & Class/Subject Filter
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

        // 4. Live Classes Strip
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
                                        text = "${liveCourse.category.uppercase()} • ₹${liveCourse.priceInr}",
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

        // 5. All Filtered Courses Header
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
                    text = "${courses.size} batches",
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
                            contentDescription = "No batches found",
                            modifier = Modifier
                                .height(130.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "No matching batches found",
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

        // 6. Meet Apurba Sarkar & COMMERCETIME Faculty
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
                            modifier = Modifier.width(230.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (teacher.photoResId != null) {
                                        Image(
                                            painter = painterResource(id = teacher.photoResId),
                                            contentDescription = teacher.name,
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
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

        // 7. COMMERCETIME Classroom Moments & Center Details
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "COMMERCETIME Coaching Moments",
                    style = MaterialTheme.typography.titleLarge
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(galleryPhotos, key = { it.title }) { photo ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.width(240.dp)
                        ) {
                            Column {
                                Image(
                                    painter = painterResource(id = photo.resId),
                                    contentDescription = photo.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(132.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = photo.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = photo.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = CatalogData.CENTER_NAME + " — Features & Campus",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "• Smart AI Digital Board with AC Classroom & Recorded Classes\n" +
                                "• Online & Offline Classes Available\n" +
                                "• Teaching Medium: Bengali, English, Hindi\n" +
                                "• High Quality Teaching & 24 x 7 E-Learning AI Assistant",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = CatalogData.CENTER_ADDRESS,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${CatalogData.CENTER_PHONE} • ${CatalogData.CENTER_SOCIAL}",
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = MonoFinanceFamily
                            )
                        }
                    }
                }
            }
        }

        // 8. Subscription Plans
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
                                text = if (plan.priceInr == 0) "Free Forever" else "₹${plan.priceInr} • ${plan.billingCycle}",
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
                                    Text("Activate ${plan.name} (₹${plan.priceInr})")
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
                    if (teacher.photoResId != null) {
                        Image(
                            painter = painterResource(id = teacher.photoResId),
                            contentDescription = teacher.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
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
