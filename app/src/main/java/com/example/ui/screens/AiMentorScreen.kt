package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AiChatMessage
import com.example.model.CommerceCourse
import com.example.model.Enrollment
import com.example.model.QuizAttempt
import com.example.ui.i18n.LocalAppStrings
import com.example.viewmodel.AiMentorViewModel
import com.example.viewmodel.AiPlanUiState

@Composable
fun AiMentorScreen(
    messages: List<AiChatMessage>,
    isStreaming: Boolean,
    chatError: String?,
    planState: AiPlanUiState,
    enrollments: List<Enrollment>,
    quizAttempts: List<QuizAttempt>,
    allCourses: List<CommerceCourse>,
    onAskDoubt: (prompt: String, subject: String, bitmap: Bitmap?) -> Unit,
    onDismissError: () -> Unit,
    onGeneratePlan: (goal: String, enrolled: List<String>, avgScore: Int, catalog: List<String>) -> Unit
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    var modeIndex by remember { mutableIntStateOf(0) }
    var promptText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("General") }
    var attachedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val subjects = listOf("General", "Accounting", "Taxation", "Economics", "Finance", "CA/CS Law", "Stocks")

    // Zero-permission Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            attachedBitmap = runCatching {
                val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(src) { decoder, info, _ ->
                        val maxDim = 1024
                        val ratio = maxOf(info.size.width, info.size.height).toFloat() / maxDim
                        if (ratio > 1f) {
                            decoder.setTargetSize(
                                (info.size.width / ratio).toInt().coerceAtLeast(1),
                                (info.size.height / ratio).toInt().coerceAtLeast(1)
                            )
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                bmp
            }.getOrNull()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .clip(RoundedCornerShape(18.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_ai_mentor_art),
                contentDescription = "Gemini AI Commerce Mentor",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.horizontalGradient(listOf(Color(0xDC0B2545), Color(0x660B2545))))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color(0xFFF0C45A), modifier = Modifier.size(16.dp))
                    Text("POWERED BY GEMINI AI", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF0C45A))
                }
                Text(
                    text = if (modeIndex == 0) strings.aiDoubtSolverTitle else strings.aiStudyPlanTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            listOf("Doubt Solver", "Study Roadmap").forEachIndexed { idx, label ->
                SegmentedButton(
                    selected = modeIndex == idx,
                    onClick = { modeIndex = idx },
                    shape = SegmentedButtonDefaults.itemShape(idx, 2)
                ) {
                    Text(label)
                }
            }
        }

        if (modeIndex == 0) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(subjects) { subj ->
                    FilterChip(
                        selected = subj == selectedSubject,
                        onClick = { selectedSubject = subj },
                        label = { Text(subj) }
                    )
                }
            }

            // Quick Prompt Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val quickPrompts = listOf(
                    "Explain GST Input Tax Credit set-off",
                    "How to pass Depreciation journal entry?",
                    "Difference between NPV and IRR",
                    "What is a good ROE and P/E ratio?"
                )
                items(quickPrompts) { sample ->
                    AssistChip(
                        onClick = { onAskDoubt(sample, selectedSubject, null) },
                        label = { Text(sample, maxLines = 1) }
                    )
                }
            }

            if (chatError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = chatError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDismissError) {
                            Icon(Icons.Filled.Close, contentDescription = "Dismiss error")
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            contentColor = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(18.dp),
                            border = if (msg.isUser) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.widthIn(max = 310.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (msg.hasAttachment) {
                                    Text(
                                        "[Attached Image]",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFF0C45A)
                                    )
                                }
                                Text(
                                    text = msg.text.ifBlank { if (msg.isStreaming) "Thinking..." else "" },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            if (attachedBitmap != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Image(
                        bitmap = attachedBitmap!!.asImageBitmap(),
                        contentDescription = "Selected problem image",
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text("Problem image attached", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    IconButton(onClick = { attachedBitmap = null }) {
                        Icon(Icons.Filled.Close, contentDescription = "Remove image")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.testTag("pick_doubt_image_button")
                ) {
                    Icon(Icons.Filled.AddPhotoAlternate, contentDescription = "Upload balance sheet or problem photo")
                }
                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = { Text(strings.askDoubtPlaceholder, maxLines = 1) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_doubt_input")
                )
                IconButton(
                    onClick = {
                        val q = promptText
                        val bmp = attachedBitmap
                        promptText = ""
                        attachedBitmap = null
                        onAskDoubt(q, selectedSubject, bmp)
                    },
                    enabled = !isStreaming && (promptText.isNotBlank() || attachedBitmap != null),
                    modifier = Modifier.testTag("send_ai_doubt_button")
                ) {
                    if (isStreaming) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Ask Gemini AI")
                    }
                }
            }
        } else {
            // Personalized AI Study Roadmap
            var selectedGoal by remember { mutableStateOf("B.Com Honours") }
            val goals = listOf("Class 11 & 12 Boards", "B.Com Honours", "CA Foundation", "CS Foundation", "Taxation Pro", "Stock Market Basics")

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text("Select Your Career / Exam Goal", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(goals) { goal ->
                            FilterChip(
                                selected = selectedGoal == goal,
                                onClick = { selectedGoal = goal },
                                label = { Text(goal) }
                            )
                        }
                    }
                }
                item {
                    val avgScore = if (quizAttempts.isEmpty()) 0 else quizAttempts.map { it.score.toInt() }.average().toInt()
                    Button(
                        onClick = {
                            onGeneratePlan(
                                selectedGoal,
                                enrollments.map { it.courseTitle },
                                avgScore,
                                allCourses.map { it.title }
                            )
                        },
                        enabled = planState !is AiPlanUiState.Loading,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_study_plan_button")
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Generate Personalized AI Roadmap")
                    }
                }

                when (planState) {
                    is AiPlanUiState.Idle -> {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text(
                                    "Tap above to let Gemini analyze your enrolled tracks, quiz scores, and target exam to build a structured study plan.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                    is AiPlanUiState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is AiPlanUiState.Error -> {
                        item {
                            Surface(color = MaterialTheme.colorScheme.errorContainer, shape = RoundedCornerShape(14.dp)) {
                                Text(
                                    planState.message,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                    is AiPlanUiState.Success -> {
                        val plan = planState.plan
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("AI Strategy Summary", style = MaterialTheme.typography.titleMedium)
                                    Text(plan.summary, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Recommended Courses", style = MaterialTheme.typography.titleSmall)
                                    plan.recommendedCourseTitles.forEach { rec ->
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                            Text(rec, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text("High-Impact Focus Topics", style = MaterialTheme.typography.titleSmall)
                                    plan.focusTopics.forEach { topic ->
                                        Text("• $topic", style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text("4-Week Study Schedule", style = MaterialTheme.typography.titleSmall)
                                    plan.weeklySchedule.forEach { step ->
                                        Text("→ $step", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
