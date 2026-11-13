package com.example.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AiChatMessage
import com.example.model.AiStudyPlan
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import java.util.UUID

sealed interface AiPlanUiState {
    data object Idle : AiPlanUiState
    data object Loading : AiPlanUiState
    data class Success(val plan: AiStudyPlan) : AiPlanUiState
    data class Error(val message: String) : AiPlanUiState
}

class AiMentorViewModel : ViewModel() {

    private val chatModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content {
                text(
                    "You are the 24x7 E-Learning AI Assistant for COMMERCETIME (Apurba Da's Classes, taught by Apurba Sarkar, M.COM, MBA). " +
                        "Help Class 11, Class 12, B.Com, BBA, MBA, and CA/CS Foundation students with Accountancy, Costing, " +
                        "Taxation (GST & Income Tax), Business Law, Economics, and Corporate Finance. " +
                        "Respond clearly in English, Bengali, or Hindi matching the student's question."
                )
            }
        )
    }

    private val studyPlanSchema = Schema.obj(
        properties = mapOf(
            "summary" to Schema.string(description = "Personalized 2-sentence study motivation and strategy"),
            "recommendedCourseTitles" to Schema.array(
                items = Schema.string(),
                description = "2 to 3 recommended commerce course titles"
            ),
            "focusTopics" to Schema.array(
                items = Schema.string(),
                description = "3 high-impact commerce topics to prioritize"
            ),
            "weeklySchedule" to Schema.array(
                items = Schema.string(),
                description = "4 concise weekly study milestones"
            )
        )
    )

    private val plannerModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = studyPlanSchema
            }
        )
    }

    private val _messages = MutableStateFlow(
        listOf(
            AiChatMessage(
                id = "welcome_ai",
                isUser = false,
                text = "Welcome to COMMERCETIME (Apurba Da's Classes)! Ask any doubt for Class 11, Class 12, or B.Com in Accountancy, Costing, GST, Law, or Economics — in English, Bengali, or Hindi — or upload a photo of a problem."
            )
        )
    )
    val messages: StateFlow<List<AiChatMessage>> = _messages.asStateFlow()

    private val _isChatStreaming = MutableStateFlow(false)
    val isChatStreaming: StateFlow<Boolean> = _isChatStreaming.asStateFlow()

    private val _chatError = MutableStateFlow<String?>(null)
    val chatError: StateFlow<String?> = _chatError.asStateFlow()

    private val _planState = MutableStateFlow<AiPlanUiState>(AiPlanUiState.Idle)
    val planState: StateFlow<AiPlanUiState> = _planState.asStateFlow()

    fun clearChatError() {
        _chatError.value = null
    }

    fun askDoubt(prompt: String, subjectTag: String, attachedBitmap: Bitmap?) {
        val cleanPrompt = prompt.trim()
        if (cleanPrompt.isEmpty() && attachedBitmap == null) return
        if (_isChatStreaming.value) return

        val displayQuery = if (subjectTag != "General") "[$subjectTag] $cleanPrompt" else cleanPrompt
        val userMsg = AiChatMessage(
            id = "u_${UUID.randomUUID().toString().take(8)}",
            isUser = true,
            text = displayQuery.ifBlank { "[$subjectTag] Please solve the attached problem image." },
            hasAttachment = attachedBitmap != null
        )
        val assistantMsgId = "a_${UUID.randomUUID().toString().take(8)}"
        _messages.value = _messages.value + userMsg + AiChatMessage(
            id = assistantMsgId,
            isUser = false,
            text = "",
            isStreaming = true
        )
        _chatError.value = null
        _isChatStreaming.value = true

        viewModelScope.launch {
            val accumulated = StringBuilder()
            try {
                val inputContent = content {
                    if (attachedBitmap != null) {
                        image(attachedBitmap)
                    }
                    text(
                        "Subject Focus: $subjectTag.\n" +
                            "Student Question: ${cleanPrompt.ifBlank { "Analyze and solve the commerce problem shown in the image step by step." }}"
                    )
                }

                withTimeout(60_000L) {
                    chatModel.generateContentStream(inputContent).collect { chunk ->
                        val chunkText = chunk.text.orEmpty()
                        accumulated.append(chunkText)
                        updateAssistantMessage(
                            id = assistantMsgId,
                            text = accumulated.toString(),
                            isStreaming = true
                        )
                    }
                }

                if (accumulated.isNotBlank()) {
                    updateAssistantMessage(
                        id = assistantMsgId,
                        text = accumulated.toString(),
                        isStreaming = false
                    )
                } else {
                    removeMessage(assistantMsgId)
                    _chatError.value = "Gemini returned an empty response. Please try rephrasing your question."
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "AI Doubt Solver timed out", e)
                removeMessage(assistantMsgId)
                _chatError.value = "AI response timed out after 60 seconds. Please try again."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "AI Doubt Solver failed", e)
                removeMessage(assistantMsgId)
                _chatError.value = e.localizedMessage ?: "AI Mentor encountered an error. Check network & App Check."
            } finally {
                _isChatStreaming.value = false
            }
        }
    }

    fun generatePersonalizedStudyPlan(
        goalTrack: String,
        enrolledCourseTitles: List<String>,
        averageQuizScore: Int,
        availableCourseTitles: List<String>
    ) {
        viewModelScope.launch {
            _planState.value = AiPlanUiState.Loading
            try {
                val prompt = buildString {
                    appendLine("Create a personalized commerce learning roadmap.")
                    appendLine("Target Goal: $goalTrack")
                    appendLine("Currently Enrolled Courses: ${enrolledCourseTitles.ifEmpty { listOf("None yet") }.joinToString(", ")}")
                    appendLine("Average Quiz Score: $averageQuizScore%")
                    appendLine("Available Catalog Courses: ${availableCourseTitles.joinToString(", ")}")
                    appendLine("Recommend courses from the available catalog, identify 3 key weak/high-yield topics, and provide a 4-step weekly study schedule.")
                }

                val response = withTimeout(60_000L) {
                    plannerModel.generateContent(prompt)
                }
                val jsonString = response.text
                    ?: throw IllegalStateException("AI returned an empty study plan.")

                val json = JSONObject(jsonString)
                val summary = json.getString("summary")
                val recArray = json.getJSONArray("recommendedCourseTitles")
                val recs = (0 until recArray.length()).map { recArray.getString(it) }
                val topicsArray = json.getJSONArray("focusTopics")
                val topics = (0 until topicsArray.length()).map { topicsArray.getString(it) }
                val schedArray = json.getJSONArray("weeklySchedule")
                val schedule = (0 until schedArray.length()).map { schedArray.getString(it) }

                _planState.value = AiPlanUiState.Success(
                    AiStudyPlan(
                        summary = summary,
                        recommendedCourseTitles = recs,
                        focusTopics = topics,
                        weeklySchedule = schedule
                    )
                )
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Study plan generation timed out", e)
                _planState.value = AiPlanUiState.Error("Study plan generation timed out after 60s.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Study plan generation failed", e)
                _planState.value = AiPlanUiState.Error(
                    e.localizedMessage ?: "Could not generate study plan."
                )
            }
        }
    }

    private fun updateAssistantMessage(id: String, text: String, isStreaming: Boolean) {
        _messages.value = _messages.value.map { msg ->
            if (msg.id == id) msg.copy(text = text, isStreaming = isStreaming) else msg
        }
    }

    private fun removeMessage(id: String) {
        _messages.value = _messages.value.filterNot { it.id == id }
    }

    private companion object {
        const val TAG = "AiMentorViewModel"
    }
}
