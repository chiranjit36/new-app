package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class AppRole(val key: String) {
    STUDENT("student"),
    TEACHER("teacher"),
    ADMIN("admin")
}

enum class AppLanguage(val code: String, val nativeLabel: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी"),
    BN("bn", "বাংলা"),
    ES("es", "Español")
}

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val role: String = "student",
    val preferredLanguage: String = "en",
    val subscriptionPlan: String = "free",
    val referralCode: String = "",
    val walletCredits: Long = 500L,
    val studyStreakDays: Long = 1L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CourseFirestoreItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val priceInr: Long = 0L,
    val durationHours: Long = 10L,
    val level: String = "Foundation",
    val status: String = "published",
    val nextLiveSession: String? = null,
    val ratingAvg: Double? = 4.8,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CourseReview(
    @DocumentId val id: String = "",
    val userId: String = "",
    val authorName: String = "",
    val courseId: String = "",
    val rating: Long = 5L,
    val comment: String = "",
    val status: String = "visible",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Enrollment(
    @DocumentId val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseTitle: String = "",
    val category: String = "",
    val progressPercent: Long = 0L,
    val completedLessonsCount: Long = 0L,
    val totalLessonsCount: Long = 1L,
    val status: String = "active",
    @ServerTimestamp val enrolledAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AttendanceRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseTitle: String = "",
    val sessionTitle: String = "",
    val sessionType: String = "live",
    val status: String = "present",
    @ServerTimestamp val markedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class QuizAttempt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseTitle: String = "",
    val quizTitle: String = "",
    val score: Long = 0L,
    val totalQuestions: Long = 1L,
    val passed: Boolean = false,
    @ServerTimestamp val attemptedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CertificateRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val studentName: String = "",
    val courseId: String = "",
    val courseTitle: String = "",
    val certificateNumber: String = "",
    val scorePercent: Long = 0L,
    @ServerTimestamp val issuedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PaymentRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val itemTitle: String = "",
    val itemType: String = "course",
    val amountInr: Long = 0L,
    val paymentMethod: String = "UPI",
    val status: String = "paid",
    val transactionRef: String = "",
    @ServerTimestamp val paidAt: Timestamp? = null
)

@IgnoreExtraProperties
data class NotificationItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val category: String = "system",
    val isRead: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ReferralRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val referredName: String = "",
    val referralCode: String = "",
    val rewardInr: Long = 500L,
    val status: String = "credited",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class LessonItem(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val isLive: Boolean,
    val scheduleLabel: String,
    val summary: String,
    val keyTakeaways: List<String>
)

data class StudyMaterialItem(
    val id: String,
    val title: String,
    val format: String,
    val sizeLabel: String,
    val contentPreview: String
)

data class QuizQuestion(
    val id: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class TeacherProfile(
    val id: String,
    val name: String,
    val credentials: String,
    val specialty: String,
    val experienceYears: Int,
    val rating: Double,
    val studentsTaught: String,
    val bio: String,
    val photoResId: Int? = null
)

data class CommerceCourse(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val teacherId: String,
    val teacherName: String,
    val teacherCredentials: String,
    val priceInr: Int,
    val originalPriceInr: Int,
    val durationHours: Int,
    val level: String,
    val ratingAvg: Double,
    val enrolledCount: Int,
    val nextLiveSession: String,
    val isLiveNow: Boolean,
    val syllabusHighlights: List<String>,
    val lessons: List<LessonItem>,
    val studyMaterials: List<StudyMaterialItem>,
    val quizQuestions: List<QuizQuestion>,
    val isUserCreated: Boolean = false
)

data class SubscriptionPlanOption(
    val id: String,
    val name: String,
    val priceInr: Int,
    val billingCycle: String,
    val badge: String,
    val perks: List<String>
)

data class AiStudyPlan(
    val summary: String,
    val recommendedCourseTitles: List<String>,
    val focusTopics: List<String>,
    val weeklySchedule: List<String>
)

data class AiChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val hasAttachment: Boolean = false,
    val isStreaming: Boolean = false
)
