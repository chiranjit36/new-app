package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.firebase.OperationType
import com.example.firebase.handleFirestoreError
import com.example.model.AttendanceRecord
import com.example.model.CertificateRecord
import com.example.model.CourseFirestoreItem
import com.example.model.CourseReview
import com.example.model.Enrollment
import com.example.model.NotificationItem
import com.example.model.PaymentRecord
import com.example.model.QuizAttempt
import com.example.model.ReferralRecord
import com.example.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CommerceRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    // --- User Profile ---
    fun observeUserProfile(userId: String): Flow<UserProfile?> {
        val path = "users/$userId"
        return db.collection("users").document(userId)
            .snapshots()
            .map { snapshot ->
                snapshot.toObject(
                    UserProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.GET, path)
                }
                throw error
            }
    }

    suspend fun ensureUserProfile(
        userId: String,
        displayName: String,
        photoUrl: String?
    ): UserProfile {
        val docRef = db.collection("users").document(userId)
        return try {
            val snap = docRef.get().await()
            if (snap.exists()) {
                snap.toObject(
                    UserProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ) ?: UserProfile(userId = userId, displayName = displayName)
            } else {
                val safeName = displayName.ifBlank { "Commerce Scholar" }.take(75)
                val code = "CHUB-${userId.take(4).uppercase()}"
                val payload = mutableMapOf<String, Any>(
                    "userId" to userId,
                    "displayName" to safeName,
                    "role" to "student",
                    "preferredLanguage" to "en",
                    "subscriptionPlan" to "free",
                    "referralCode" to code,
                    "walletCredits" to 500L,
                    "studyStreakDays" to 1L,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                if (!photoUrl.isNullOrBlank()) {
                    payload["photoUrl"] = photoUrl.take(490)
                }
                docRef.set(payload).await()
                // Welcome notification
                createNotificationInternal(
                    userId = userId,
                    title = "Welcome to Commerce Classes Hub",
                    body = "You received ₹500 starter referral credits. Explore Accounting, CA/CS, Taxation & Stock Markets!",
                    category = "system"
                )
                UserProfile(
                    id = userId,
                    userId = userId,
                    displayName = safeName,
                    photoUrl = photoUrl,
                    role = "student",
                    preferredLanguage = "en",
                    subscriptionPlan = "free",
                    referralCode = code,
                    walletCredits = 500L,
                    studyStreakDays = 1L
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    suspend fun updateLanguage(languageCode: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        try {
            docRef.update(
                mapOf(
                    "preferredLanguage" to languageCode,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    // --- Published & Teacher Courses (Firestore) ---
    fun observePublishedCourses(): Flow<List<CourseFirestoreItem>> {
        val path = "courses"
        return db.collection(path)
            .whereEqualTo("status", "published")
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    CourseFirestoreItem::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    fun observeTeacherCourses(teacherId: String): Flow<List<CourseFirestoreItem>> {
        val path = "courses"
        return db.collection(path)
            .whereEqualTo("teacherId", teacherId)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    CourseFirestoreItem::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == teacherId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun publishTeacherCourse(
        title: String,
        category: String,
        description: String,
        teacherName: String,
        priceInr: Long,
        durationHours: Long,
        level: String,
        nextLiveSession: String?
    ): Result<String> {
        val uid = requireUserId()
        val courseId = "tc_${UUID.randomUUID().toString().take(12).replace("-", "")}"
        val docRef = db.collection("courses").document(courseId)
        val payload = mutableMapOf<String, Any>(
            "title" to title.trim().take(115),
            "category" to category.trim().take(55),
            "description" to description.trim().take(950),
            "teacherId" to uid,
            "teacherName" to teacherName.ifBlank { "Faculty Member" }.take(75),
            "priceInr" to priceInr.coerceIn(0L, 500000L),
            "durationHours" to durationHours.coerceIn(1L, 1000L),
            "level" to level,
            "status" to "published",
            "ratingAvg" to 5.0,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (!nextLiveSession.isNullOrBlank()) {
            payload["nextLiveSession"] = nextLiveSession.trim().take(115)
        }
        return try {
            docRef.set(payload).await()
            createNotificationInternal(
                userId = uid,
                title = "Course Published",
                body = "\"${title.trim()}\" is now live in the Commerce Classes Hub catalog.",
                category = "system"
            )
            Result.success(courseId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    // --- Course Reviews ---
    fun observeCourseReviews(courseId: String): Flow<List<CourseReview>> {
        val path = "courses/$courseId/reviews"
        return db.collection("courses").document(courseId).collection("reviews")
            .whereEqualTo("status", "visible")
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    CourseReview::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ).sortedByDescending { it.createdAt?.seconds ?: 0L }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun submitCourseReview(
        courseId: String,
        authorName: String,
        rating: Int,
        comment: String
    ): Result<Unit> {
        val uid = requireUserId()
        val reviewId = "rev_${uid.take(12)}_${System.currentTimeMillis()}"
        val docRef = db.collection("courses").document(courseId)
            .collection("reviews").document(reviewId)
        val payload = mapOf(
            "userId" to uid,
            "authorName" to authorName.ifBlank { "Student" }.take(75),
            "courseId" to courseId,
            "rating" to rating.coerceIn(1, 5).toLong(),
            "comment" to comment.trim().take(580),
            "status" to "visible",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    // --- Enrollments & Payments ---
    fun observeEnrollments(userId: String): Flow<List<Enrollment>> {
        val path = "users/$userId/enrollments"
        return db.collection("users").document(userId).collection("enrollments")
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    Enrollment::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun enrollAndPayCourse(
        courseId: String,
        courseTitle: String,
        category: String,
        totalLessonsCount: Int,
        amountInr: Int,
        paymentMethod: String,
        walletDiscountInr: Int = 0
    ): Result<String> {
        val uid = requireUserId()
        val safePaidAmount = (amountInr - walletDiscountInr).coerceAtLeast(0).toLong()
        val txnRef = "CHUB-${UUID.randomUUID().toString().take(8).uppercase()}"
        val paymentId = "pay_${System.currentTimeMillis()}"
        val enrollmentRef = db.collection("users").document(uid).collection("enrollments").document(courseId)
        val paymentRef = db.collection("users").document(uid).collection("payments").document(paymentId)

        return try {
            val enrollPayload = mapOf(
                "userId" to uid,
                "courseId" to courseId,
                "courseTitle" to courseTitle.take(115),
                "category" to category.take(55),
                "progressPercent" to 0L,
                "completedLessonsCount" to 0L,
                "totalLessonsCount" to totalLessonsCount.coerceAtLeast(1).toLong(),
                "status" to "active",
                "enrolledAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            val paymentPayload = mapOf(
                "userId" to uid,
                "itemTitle" to courseTitle.take(115),
                "itemType" to "course",
                "amountInr" to safePaidAmount,
                "paymentMethod" to paymentMethod.take(35),
                "status" to "paid",
                "transactionRef" to txnRef,
                "paidAt" to FieldValue.serverTimestamp()
            )
            enrollmentRef.set(enrollPayload).await()
            paymentRef.set(paymentPayload).await()

            if (walletDiscountInr > 0) {
                val userSnap = db.collection("users").document(uid).get().await()
                val currentCredits = userSnap.getLong("walletCredits") ?: 0L
                val updatedCredits = (currentCredits - walletDiscountInr).coerceAtLeast(0L)
                db.collection("users").document(uid).update(
                    mapOf(
                        "walletCredits" to updatedCredits,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }

            createNotificationInternal(
                userId = uid,
                title = "Course Enrolled: $courseTitle",
                body = "Payment ₹$safePaidAmount confirmed (Ref: $txnRef). Live & recorded modules unlocked!",
                category = "payment"
            )
            Result.success(txnRef)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, enrollmentRef.path)
            Result.failure(e)
        }
    }

    suspend fun updateEnrollmentProgress(
        enrollment: Enrollment,
        newCompletedLessons: Int
    ): Result<Unit> {
        val uid = requireUserId()
        if (enrollment.status == "completed") return Result.success(Unit)
        val total = enrollment.totalLessonsCount.coerceAtLeast(1L)
        val clampedCompleted = newCompletedLessons.toLong().coerceIn(0L, total)
        val pct = ((clampedCompleted * 100L) / total).coerceIn(0L, 100L)
        val newStatus = if (pct >= 100L) "completed" else "active"
        val docRef = db.collection("users").document(uid)
            .collection("enrollments").document(enrollment.courseId)

        return try {
            docRef.update(
                mapOf(
                    "progressPercent" to pct,
                    "completedLessonsCount" to clampedCompleted,
                    "status" to newStatus,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            if (newStatus == "completed") {
                val userSnap = db.collection("users").document(uid).get().await()
                val studentName = userSnap.getString("displayName") ?: "Commerce Scholar"
                issueCertificateInternal(
                    userId = uid,
                    studentName = studentName,
                    courseId = enrollment.courseId,
                    courseTitle = enrollment.courseTitle,
                    scorePercent = 92
                )
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    // --- Subscriptions ---
    suspend fun purchaseSubscription(
        planId: String,
        planName: String,
        priceInr: Int,
        paymentMethod: String
    ): Result<String> {
        val uid = requireUserId()
        val txnRef = "SUB-${UUID.randomUUID().toString().take(8).uppercase()}"
        val userRef = db.collection("users").document(uid)
        val payRef = userRef.collection("payments").document("pay_${System.currentTimeMillis()}")
        return try {
            userRef.update(
                mapOf(
                    "subscriptionPlan" to planId,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            payRef.set(
                mapOf(
                    "userId" to uid,
                    "itemTitle" to "$planName Plan",
                    "itemType" to "subscription",
                    "amountInr" to priceInr.toLong(),
                    "paymentMethod" to paymentMethod.take(35),
                    "status" to "paid",
                    "transactionRef" to txnRef,
                    "paidAt" to FieldValue.serverTimestamp()
                )
            ).await()
            createNotificationInternal(
                userId = uid,
                title = "$planName Activated",
                body = "Your subscription is active (Ref: $txnRef). Enjoy unlimited classes & AI tutoring.",
                category = "payment"
            )
            Result.success(txnRef)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, userRef.path)
            Result.failure(e)
        }
    }

    // --- Attendance ---
    fun observeAttendance(userId: String): Flow<List<AttendanceRecord>> {
        val path = "users/$userId/attendance"
        return db.collection("users").document(userId).collection("attendance")
            .orderBy("markedAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    AttendanceRecord::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun markAttendance(
        courseId: String,
        courseTitle: String,
        sessionTitle: String,
        sessionType: String
    ): Result<Unit> {
        val uid = requireUserId()
        val docId = "att_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
        val docRef = db.collection("users").document(uid).collection("attendance").document(docId)
        val payload = mapOf(
            "userId" to uid,
            "courseId" to courseId,
            "courseTitle" to courseTitle.take(115),
            "sessionTitle" to sessionTitle.take(135),
            "sessionType" to if (sessionType == "live") "live" else "recorded",
            "status" to "present",
            "markedAt" to FieldValue.serverTimestamp()
        )
        return try {
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    // --- Quizzes & Certificates ---
    fun observeQuizAttempts(userId: String): Flow<List<QuizAttempt>> {
        val path = "users/$userId/quiz_attempts"
        return db.collection("users").document(userId).collection("quiz_attempts")
            .orderBy("attemptedAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    QuizAttempt::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    fun observeCertificates(userId: String): Flow<List<CertificateRecord>> {
        val path = "users/$userId/certificates"
        return db.collection("users").document(userId).collection("certificates")
            .orderBy("issuedAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    CertificateRecord::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun submitQuizAttempt(
        courseId: String,
        courseTitle: String,
        quizTitle: String,
        scorePercent: Int,
        totalQuestions: Int,
        studentName: String
    ): Result<Boolean> {
        val uid = requireUserId()
        val passed = scorePercent >= 60
        val attemptId = "quiz_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
        val docRef = db.collection("users").document(uid).collection("quiz_attempts").document(attemptId)
        return try {
            docRef.set(
                mapOf(
                    "userId" to uid,
                    "courseId" to courseId,
                    "courseTitle" to courseTitle.take(115),
                    "quizTitle" to quizTitle.take(115),
                    "score" to scorePercent.coerceIn(0, 100).toLong(),
                    "totalQuestions" to totalQuestions.coerceAtLeast(1).toLong(),
                    "passed" to passed,
                    "attemptedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            if (passed) {
                issueCertificateInternal(
                    userId = uid,
                    studentName = studentName,
                    courseId = courseId,
                    courseTitle = courseTitle,
                    scorePercent = scorePercent
                )
                createNotificationInternal(
                    userId = uid,
                    title = "Certificate Unlocked!",
                    body = "Scored $scorePercent% in $courseTitle quiz. Your verified certificate is ready.",
                    category = "achievement"
                )
            }
            Result.success(passed)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    private suspend fun issueCertificateInternal(
        userId: String,
        studentName: String,
        courseId: String,
        courseTitle: String,
        scorePercent: Int
    ) {
        val certId = "cert_$courseId"
        val certRef = db.collection("users").document(userId).collection("certificates").document(certId)
        val existing = certRef.get().await()
        if (!existing.exists()) {
            val certNum = "CHUB-2026-${UUID.randomUUID().toString().take(6).uppercase()}"
            certRef.set(
                mapOf(
                    "userId" to userId,
                    "studentName" to studentName.ifBlank { "Commerce Scholar" }.take(75),
                    "courseId" to courseId,
                    "courseTitle" to courseTitle.take(115),
                    "certificateNumber" to certNum,
                    "scorePercent" to scorePercent.coerceIn(0, 100).toLong(),
                    "issuedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        }
    }

    // --- Payments ---
    fun observePayments(userId: String): Flow<List<PaymentRecord>> {
        val path = "users/$userId/payments"
        return db.collection("users").document(userId).collection("payments")
            .orderBy("paidAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    PaymentRecord::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    // --- Notifications ---
    fun observeNotifications(userId: String): Flow<List<NotificationItem>> {
        val path = "users/$userId/notifications"
        return db.collection("users").document(userId).collection("notifications")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    NotificationItem::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun markNotificationRead(notificationId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
            .collection("notifications").document(notificationId)
        try {
            docRef.update("isRead", true).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
        }
    }

    private suspend fun createNotificationInternal(
        userId: String,
        title: String,
        body: String,
        category: String
    ) {
        val id = "notif_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
        val ref = db.collection("users").document(userId).collection("notifications").document(id)
        try {
            ref.set(
                mapOf(
                    "userId" to userId,
                    "title" to title.take(95),
                    "body" to body.take(390),
                    "category" to category,
                    "isRead" to false,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            Log.w("CommerceRepository", "Notification write skipped: ${e.message}")
        }
    }

    // --- Referrals ---
    fun observeReferrals(userId: String): Flow<List<ReferralRecord>> {
        val path = "users/$userId/referrals"
        return db.collection("users").document(userId).collection("referrals")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snap ->
                snap.toObjects(
                    ReferralRecord::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
            .catch { error ->
                if (error is CancellationException) throw error
                if (auth.currentUser?.uid == userId) {
                    handleFirestoreError(error, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun sendReferralInvite(
        friendName: String,
        referralCode: String
    ): Result<Unit> {
        val uid = requireUserId()
        val refId = "ref_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
        val docRef = db.collection("users").document(uid).collection("referrals").document(refId)
        val userRef = db.collection("users").document(uid)
        return try {
            docRef.set(
                mapOf(
                    "userId" to uid,
                    "referredName" to friendName.trim().take(75),
                    "referralCode" to referralCode.take(30),
                    "rewardInr" to 500L,
                    "status" to "credited",
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()
            userRef.update(
                mapOf(
                    "walletCredits" to FieldValue.increment(500L),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            createNotificationInternal(
                userId = uid,
                title = "Referral Bonus Credited!",
                body = "+₹500 added to your Commerce Wallet for inviting ${friendName.trim()}.",
                category = "referral"
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }
}
