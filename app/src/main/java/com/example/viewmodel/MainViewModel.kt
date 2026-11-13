package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CatalogData
import com.example.data.CommerceRepository
import com.example.firebase.unwrapFirestoreException
import com.example.model.AppLanguage
import com.example.model.AppRole
import com.example.model.AttendanceRecord
import com.example.model.CertificateRecord
import com.example.model.CommerceCourse
import com.example.model.CourseFirestoreItem
import com.example.model.CourseReview
import com.example.model.Enrollment
import com.example.model.LessonItem
import com.example.model.NotificationItem
import com.example.model.PaymentRecord
import com.example.model.QuizAttempt
import com.example.model.QuizQuestion
import com.example.model.ReferralRecord
import com.example.model.StudyMaterialItem
import com.example.model.UserProfile
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val repository: CommerceRepository,
    val currentUserId: String,
    val initialDisplayName: String,
    val initialPhotoUrl: String?,
    val userEmail: String?
) : ViewModel() {

    private val _activeRole = MutableStateFlow(AppRole.STUDENT)
    val activeRole: StateFlow<AppRole> = _activeRole.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCourseId = MutableStateFlow<String?>(null)

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    val isVerifiedAdminAccount: Boolean =
        userEmail?.equals("pandeychiranjit45@gmail.com", ignoreCase = true) == true

    init {
        viewModelScope.launch {
            try {
                repository.ensureUserProfile(
                    userId = currentUserId,
                    displayName = initialDisplayName,
                    photoUrl = initialPhotoUrl
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w(TAG, "Profile initialization notice: ${e.message}")
            }
        }
    }

    val userProfile: StateFlow<UserProfile?> = repository.observeUserProfile(currentUserId)
        .catch { error ->
            val fsError = unwrapFirestoreException(error)
            if (fsError == null && error is CancellationException) throw error
            Log.w(TAG, "Profile observation error", fsError ?: error)
            emit(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), null)

    val currentLanguage: StateFlow<AppLanguage> = userProfile
        .map { profile ->
            when (profile?.preferredLanguage) {
                "hi" -> AppLanguage.HI
                "bn" -> AppLanguage.BN
                "es" -> AppLanguage.ES
                else -> AppLanguage.EN
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), AppLanguage.EN)

    private val remotePublishedCourses: StateFlow<List<CourseFirestoreItem>> =
        repository.observePublishedCourses()
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Courses observation error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val teacherCourses: StateFlow<List<CourseFirestoreItem>> =
        repository.observeTeacherCourses(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Teacher courses observation error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val allCourses: StateFlow<List<CommerceCourse>> = remotePublishedCourses
        .map { remoteList ->
            val remoteDomain = remoteList.map { it.toDomainCourse() }
            val existingIds = remoteDomain.map { it.id }.toSet()
            CatalogData.curatedCourses.filter { it.id !in existingIds } + remoteDomain
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), CatalogData.curatedCourses)

    val filteredCourses: StateFlow<List<CommerceCourse>> = combine(
        allCourses,
        selectedCategory,
        searchQuery
    ) { courses, category, query ->
        courses.filter { course ->
            val catMatch = category == "All" || course.category.equals(category, ignoreCase = true)
            val q = query.trim()
            val queryMatch = q.isEmpty() ||
                course.title.contains(q, ignoreCase = true) ||
                course.category.contains(q, ignoreCase = true) ||
                course.teacherName.contains(q, ignoreCase = true)
            catMatch && queryMatch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), CatalogData.curatedCourses)

    val enrollmentsState: StateFlow<UiState<List<Enrollment>>> =
        repository.observeEnrollments(currentUserId)
            .map<List<Enrollment>, UiState<List<Enrollment>>> { UiState.Success(it) }
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                val root = fsError ?: error
                Log.w(TAG, "Enrollments error", root)
                emit(UiState.Error(root.message ?: "Failed to load enrollments"))
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), UiState.Loading)

    val attendanceRecords: StateFlow<List<AttendanceRecord>> =
        repository.observeAttendance(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Attendance error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val quizAttempts: StateFlow<List<QuizAttempt>> =
        repository.observeQuizAttempts(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Quiz attempts error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val certificates: StateFlow<List<CertificateRecord>> =
        repository.observeCertificates(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Certificates error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val payments: StateFlow<List<PaymentRecord>> =
        repository.observePayments(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Payments error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val notifications: StateFlow<List<NotificationItem>> =
        repository.observeNotifications(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Notifications error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val referrals: StateFlow<List<ReferralRecord>> =
        repository.observeReferrals(currentUserId)
            .catch { error ->
                val fsError = unwrapFirestoreException(error)
                if (fsError == null && error is CancellationException) throw error
                Log.w(TAG, "Referrals error", fsError ?: error)
                emit(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    val selectedCourseReviews: StateFlow<List<CourseReview>> = _selectedCourseId
        .flatMapLatest { courseId ->
            if (courseId.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                repository.observeCourseReviews(courseId).catch { error ->
                    val fsError = unwrapFirestoreException(error)
                    if (fsError == null && error is CancellationException) throw error
                    Log.w(TAG, "Reviews error", fsError ?: error)
                    emit(emptyList())
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), emptyList())

    fun setRole(role: AppRole) {
        _activeRole.value = role
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCourseId(courseId: String?) {
        _selectedCourseId.value = courseId
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            try {
                repository.updateLanguage(language.code)
                _actionMessage.value = "Language switched to ${language.nativeLabel}"
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _actionMessage.value = e.localizedMessage ?: "Failed to update language"
            }
        }
    }

    fun enrollCourse(
        course: CommerceCourse,
        paymentMethod: String,
        useWalletCredits: Boolean
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val credits = userProfile.value?.walletCredits?.toInt() ?: 0
            val discount = if (useWalletCredits) credits.coerceAtMost(course.priceInr) else 0
            val result = repository.enrollAndPayCourse(
                courseId = course.id,
                courseTitle = course.title,
                category = course.category,
                totalLessonsCount = course.lessons.size.coerceAtLeast(1),
                amountInr = course.priceInr,
                paymentMethod = paymentMethod,
                walletDiscountInr = discount
            )
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { txn -> "Enrolled! Payment confirmed (Ref: $txn)" },
                onFailure = { err -> err.localizedMessage ?: "Enrollment failed" }
            )
        }
    }

    fun completeLessonAndMarkAttendance(
        course: CommerceCourse,
        lesson: LessonItem,
        currentEnrollment: Enrollment?
    ) {
        viewModelScope.launch {
            _isBusy.value = true
            val attResult = repository.markAttendance(
                courseId = course.id,
                courseTitle = course.title,
                sessionTitle = lesson.title,
                sessionType = if (lesson.isLive) "live" else "recorded"
            )
            if (currentEnrollment != null && currentEnrollment.status != "completed") {
                val nextCount = (currentEnrollment.completedLessonsCount.toInt() + 1)
                    .coerceAtMost(currentEnrollment.totalLessonsCount.toInt())
                repository.updateEnrollmentProgress(currentEnrollment, nextCount)
            }
            _isBusy.value = false
            _actionMessage.value = attResult.fold(
                onSuccess = { "Attendance recorded & progress updated for \"${lesson.title}\"" },
                onFailure = { err -> err.localizedMessage ?: "Could not mark attendance" }
            )
        }
    }

    fun submitQuiz(
        course: CommerceCourse,
        selectedAnswers: Map<String, Int>
    ) {
        viewModelScope.launch {
            val total = course.quizQuestions.size.coerceAtLeast(1)
            val correct = course.quizQuestions.count { q -> selectedAnswers[q.id] == q.correctIndex }
            val scorePct = ((correct * 100) / total).coerceIn(0, 100)
            val studentName = userProfile.value?.displayName ?: initialDisplayName
            _isBusy.value = true
            val result = repository.submitQuizAttempt(
                courseId = course.id,
                courseTitle = course.title,
                quizTitle = "${course.category} Assessment",
                scorePercent = scorePct,
                totalQuestions = total,
                studentName = studentName
            )
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { passed ->
                    if (passed) "Passed with $scorePct%! Verified certificate unlocked."
                    else "Scored $scorePct%. Score 60%+ to unlock your certificate."
                },
                onFailure = { err -> err.localizedMessage ?: "Quiz submission failed" }
            )
        }
    }

    fun submitReview(courseId: String, rating: Int, comment: String) {
        if (comment.trim().length < 3) {
            _actionMessage.value = "Please enter at least 3 characters for your review."
            return
        }
        viewModelScope.launch {
            _isBusy.value = true
            val author = userProfile.value?.displayName ?: initialDisplayName
            val result = repository.submitCourseReview(courseId, author, rating, comment)
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { "Thank you! Your review has been published." },
                onFailure = { err -> err.localizedMessage ?: "Review submission failed" }
            )
        }
    }

    fun purchaseSubscription(planId: String, planName: String, priceInr: Int, method: String) {
        viewModelScope.launch {
            _isBusy.value = true
            val result = repository.purchaseSubscription(planId, planName, priceInr, method)
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { txn -> "$planName activated! (Ref: $txn)" },
                onFailure = { err -> err.localizedMessage ?: "Subscription failed" }
            )
        }
    }

    fun sendReferral(friendName: String) {
        if (friendName.trim().length < 2) {
            _actionMessage.value = "Enter your friend's name to send an invite."
            return
        }
        viewModelScope.launch {
            _isBusy.value = true
            val code = userProfile.value?.referralCode ?: "CT-USER"
            val result = repository.sendReferralInvite(friendName, code)
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { "+₹500 referral reward credited to your wallet!" },
                onFailure = { err -> err.localizedMessage ?: "Referral failed" }
            )
        }
    }

    fun publishTeacherCourse(
        title: String,
        category: String,
        description: String,
        priceInr: Long,
        durationHours: Long,
        level: String,
        nextLiveSession: String
    ) {
        if (title.trim().length < 3 || description.trim().length < 10) {
            _actionMessage.value = "Enter a valid course title and description (10+ chars)."
            return
        }
        viewModelScope.launch {
            _isBusy.value = true
            val teacherName = userProfile.value?.displayName ?: initialDisplayName
            val result = repository.publishTeacherCourse(
                title = title,
                category = category,
                description = description,
                teacherName = teacherName,
                priceInr = priceInr,
                durationHours = durationHours,
                level = level,
                nextLiveSession = nextLiveSession.ifBlank { null }
            )
            _isBusy.value = false
            _actionMessage.value = result.fold(
                onSuccess = { "Course published to COMMERCETIME catalog!" },
                onFailure = { err -> err.localizedMessage ?: "Failed to publish course" }
            )
        }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    private fun CourseFirestoreItem.toDomainCourse(): CommerceCourse {
        return CommerceCourse(
            id = id,
            title = title,
            category = category,
            description = description,
            teacherId = teacherId,
            teacherName = teacherName,
            teacherCredentials = "Verified Faculty",
            priceInr = priceInr.toInt(),
            originalPriceInr = (priceInr * 1.4).toInt(),
            durationHours = durationHours.toInt(),
            level = level,
            ratingAvg = ratingAvg ?: 4.9,
            enrolledCount = 120,
            nextLiveSession = nextLiveSession ?: "Flexible Recorded + Live Doubt Clinic",
            isLiveNow = nextLiveSession?.contains("LIVE", ignoreCase = true) == true,
            syllabusHighlights = listOf(
                "$category Core Principles & Practical Case Studies",
                "Exam-Oriented Problem Solving & Mock Reviews",
                "Interactive Faculty Doubt Clearing"
            ),
            lessons = listOf(
                LessonItem(
                    id = "${id}_l1",
                    title = "Orientation & Core Framework in $category",
                    durationMinutes = 45,
                    isLive = true,
                    scheduleLabel = nextLiveSession ?: "Upcoming Live Session",
                    summary = description,
                    keyTakeaways = listOf(
                        "Foundational terminology & exam blueprint",
                        "Worked numerical walkthrough"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "${id}_m1",
                    title = "$title Summary Notes",
                    format = "PDF Guide",
                    sizeLabel = "1.5 MB",
                    contentPreview = description
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "${id}_q1",
                    prompt = "Which best describes the primary objective of $title?",
                    options = listOf(
                        "Building strong practical mastery in $category",
                        "Ignoring financial compliance",
                        "Skipping numerical fundamentals",
                        "None of the above"
                    ),
                    correctIndex = 0,
                    explanation = "Practical conceptual mastery in $category ensures high exam and career readiness."
                )
            ),
            isUserCreated = true
        )
    }

    private companion object {
        const val TAG = "MainViewModel"
        const val STOP_TIMEOUT_MS = 5000L
    }
}
