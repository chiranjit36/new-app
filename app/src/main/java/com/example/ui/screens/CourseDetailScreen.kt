package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.CommerceCourse
import com.example.model.CourseReview
import com.example.model.Enrollment
import com.example.model.LessonItem
import com.example.model.StudyMaterialItem
import com.example.ui.theme.MonoFinanceFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    course: CommerceCourse,
    enrollment: Enrollment?,
    walletCredits: Int,
    reviews: List<CourseReview>,
    onBack: () -> Unit,
    onEnrollAndPay: (paymentMethod: String, useWallet: Boolean) -> Unit,
    onWatchAndMarkAttendance: (LessonItem) -> Unit,
    onSubmitQuiz: (Map<String, Int>) -> Unit,
    onSubmitReview: (Int, String) -> Unit
) {
    BackHandler(onBack = onBack)

    var selectedTab by remember { mutableIntStateOf(0) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var activeLessonPlayer by remember { mutableStateOf<LessonItem?>(null) }
    var activeMaterial by remember { mutableStateOf<StudyMaterialItem?>(null) }

    val selectedAnswers = remember(course.id) { mutableStateMapOf<String, Int>() }
    var quizSubmitted by remember(course.id) { mutableStateOf(false) }

    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewText by remember { mutableStateOf("") }

    val isEnrolled = enrollment != null
    val tabTitles = listOf("Classes", "Notes", "Quiz", "Reviews")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course.category, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("course_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "${course.teacherName} (${course.teacherCredentials}) • ${course.durationHours}h • ${course.level}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.92f)
                        )

                        if (isEnrolled && enrollment != null) {
                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Course Progress",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = "${enrollment.progressPercent}% (${enrollment.completedLessonsCount}/${enrollment.totalLessonsCount})",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = MonoFinanceFamily,
                                    color = Color(0xFFF0C45A)
                                )
                            }
                            LinearProgressIndicator(
                                progress = { (enrollment.progressPercent / 100f).coerceIn(0f, 1f) },
                                color = Color(0xFF34D399),
                                trackColor = Color(0x44FFFFFF),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                            )
                        } else {
                            Button(
                                onClick = { showCheckoutDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("open_checkout_button")
                            ) {
                                Icon(Icons.Filled.Payment, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Enroll & Pay ₹${course.priceInr}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

            // Segmented Tabs
            item {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    tabTitles.forEachIndexed { idx, title ->
                        SegmentedButton(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            shape = SegmentedButtonDefaults.itemShape(index = idx, count = tabTitles.size)
                        ) {
                            Text(title, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    item {
                        Text("Syllabus Highlights", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        course.syllabusHighlights.forEach { bullet ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(bullet, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    items(course.lessons, key = { it.id }) { lesson ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = if (lesson.isLive) Color(0xFFFEE2E2) else MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (lesson.isLive) Icons.Filled.LiveTv else Icons.Filled.PlayArrow,
                                                contentDescription = null,
                                                tint = if (lesson.isLive) Color(0xFFB91C1C) else MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = lesson.scheduleLabel,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (lesson.isLive) Color(0xFFB91C1C) else MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${lesson.durationMinutes} min",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    lesson.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Button(
                                    onClick = {
                                        if (isEnrolled) {
                                            activeLessonPlayer = lesson
                                            onWatchAndMarkAttendance(lesson)
                                        } else {
                                            showCheckoutDialog = true
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = if (isEnrolled) Icons.Filled.PlayArrow else Icons.Filled.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        if (isEnrolled) {
                                            if (lesson.isLive) "Join Live & Mark Attendance" else "Watch Recorded & Log Progress"
                                        } else {
                                            "Unlock by Enrolling"
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    items(course.studyMaterials, key = { it.id }) { mat ->
                        Card(
                            onClick = { activeMaterial = mat },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Description,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(mat.title, style = MaterialTheme.typography.titleSmall)
                                    Text(
                                        "${mat.format} • ${mat.sizeLabel}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    "Open",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                2 -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.Quiz, contentDescription = null)
                                Text(
                                    "Score 60% or higher to automatically unlock your verified Commerce Hub Certificate!",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    items(course.quizQuestions, key = { it.id }) { q ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(q.prompt, style = MaterialTheme.typography.titleSmall)
                                q.options.forEachIndexed { idx, opt ->
                                    val chosen = selectedAnswers[q.id] == idx
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = chosen,
                                            onClick = { if (!quizSubmitted) selectedAnswers[q.id] = idx }
                                        )
                                        Text(opt, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                                if (quizSubmitted) {
                                    val isCorrect = selectedAnswers[q.id] == q.correctIndex
                                    Text(
                                        text = if (isCorrect) "✓ Correct: ${q.explanation}" else "✗ Answer: ${q.options[q.correctIndex]}. ${q.explanation}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isCorrect) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                quizSubmitted = true
                                onSubmitQuiz(selectedAnswers.toMap())
                            },
                            enabled = selectedAnswers.size == course.quizQuestions.size,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_quiz_button")
                        ) {
                            Text("Submit Quiz & Grade")
                        }
                    }
                }

                3 -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Leave a Course Review", style = MaterialTheme.typography.titleSmall)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    (1..5).forEach { star ->
                                        IconButton(onClick = { reviewRating = star }) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = "$star stars",
                                                tint = if (star <= reviewRating) Color(0xFFD97706) else MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }
                                }
                                OutlinedTextField(
                                    value = reviewText,
                                    onValueChange = { reviewText = it },
                                    label = { Text("Share your feedback") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button(
                                    onClick = {
                                        onSubmitReview(reviewRating, reviewText)
                                        reviewText = ""
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Post Review")
                                }
                            }
                        }
                    }

                    if (reviews.isEmpty()) {
                        item {
                            Text(
                                "Be the first verified student to review this course!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(reviews, key = { it.id }) { rev ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(rev.authorName, style = MaterialTheme.typography.titleSmall)
                                        Text("★ ${rev.rating}/5", color = Color(0xFFD97706), style = MaterialTheme.typography.labelMedium)
                                    }
                                    Text(rev.comment, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Checkout Dialog (Simulated Secure Checkout)
    if (showCheckoutDialog) {
        var selectedMethod by remember { mutableStateOf("UPI (GPay / PhonePe)") }
        var applyWallet by remember { mutableStateOf(walletCredits > 0) }
        val discount = if (applyWallet) walletCredits.coerceAtMost(course.priceInr) else 0
        val finalPayable = (course.priceInr - discount).coerceAtLeast(0)

        Dialog(onDismissRequest = { showCheckoutDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Secure Course Checkout", style = MaterialTheme.typography.headlineSmall)
                    Text(course.title, style = MaterialTheme.typography.titleSmall)
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Course Fee", style = MaterialTheme.typography.bodyMedium)
                        Text("₹${course.priceInr}", fontFamily = MonoFinanceFamily)
                    }
                    if (walletCredits > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(checked = applyWallet, onCheckedChange = { applyWallet = it })
                            Text(
                                "Apply Wallet Credits (-₹$discount of ₹$walletCredits)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Text("Payment Method", style = MaterialTheme.typography.labelMedium)
                    listOf("UPI (GPay / PhonePe)", "Credit / Debit Card", "NetBanking").forEach { method ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedMethod == method,
                                onClick = { selectedMethod = method }
                            )
                            Text(method, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Payable", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "₹$finalPayable",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = MonoFinanceFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        "Simulated checkout gateway — generates verified Firestore receipt & unlocks course immediately.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = { showCheckoutDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onEnrollAndPay(selectedMethod, applyWallet)
                                showCheckoutDialog = false
                            },
                            modifier = Modifier.testTag("confirm_payment_button")
                        ) {
                            Text("Pay ₹$finalPayable")
                        }
                    }
                }
            }
        }
    }

    // Live / Recorded Class Classroom Modal
    activeLessonPlayer?.let { lesson ->
        Dialog(onDismissRequest = { activeLessonPlayer = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (lesson.isLive) Icons.Filled.LiveTv else Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color(0xFFF0C45A),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = if (lesson.isLive) "LIVE CLASSROOM ACTIVE" else "HD RECORDED LECTURE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                            Text(
                                text = "Attendance Marked Present ✓",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF34D399)
                            )
                        }
                    }
                    Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                    Text(lesson.summary, style = MaterialTheme.typography.bodyMedium)
                    Text("Key Learning Takeaways", style = MaterialTheme.typography.labelMedium)
                    lesson.keyTakeaways.forEach { point ->
                        Text("• $point", style = MaterialTheme.typography.bodySmall)
                    }
                    Button(
                        onClick = { activeLessonPlayer = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }

    // Study Material Reader Modal
    activeMaterial?.let { mat ->
        Dialog(onDismissRequest = { activeMaterial = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(mat.title, style = MaterialTheme.typography.titleLarge)
                    Text(
                        "${mat.format} • ${mat.sizeLabel}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    HorizontalDivider()
                    Text(
                        mat.contentPreview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = { activeMaterial = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close Reader")
                    }
                }
            }
        }
    }
}
