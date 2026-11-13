package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class CommerceRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun ensureUserProfile_and_enrollAndPayCourse_succeedsForOwner() = runBlocking {
        val uid = signInTestUser("student_${UUID.randomUUID().toString().take(6)}@test.com")
        val repository = CommerceRepository(firestore)

        val profile = withTimeout(TIMEOUT_MS) {
            repository.ensureUserProfile(uid, "Aarav Patel", null)
        }
        assertEquals(uid, profile.userId)
        assertEquals("student", profile.role)

        val enrollResult = withTimeout(TIMEOUT_MS) {
            repository.enrollAndPayCourse(
                courseId = "course_accounting_mastery",
                courseTitle = "Financial Accounting & IFRS Mastery",
                category = "Accounting",
                totalLessonsCount = 3,
                amountInr = 1999,
                paymentMethod = "UPI"
            )
        }
        assertTrue(enrollResult.isSuccess)

        val enrollments = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeEnrollments(uid).first { it.isNotEmpty() }
        }
        assertTrue(enrollments.any { it.courseId == "course_accounting_mastery" })
    }

    @Test
    fun publishTeacherCourse_and_submitReview_succeedsWithAlignedQueries() = runBlocking {
        val teacherUid = signInTestUser("teacher_${UUID.randomUUID().toString().take(6)}@test.com")
        val repo = CommerceRepository(firestore)

        val courseId = withTimeout(TIMEOUT_MS) {
            repo.publishTeacherCourse(
                title = "Advanced Auditing & Assurance",
                category = "CA Foundation",
                description = "Detailed standards on auditing and internal controls for CA aspirants.",
                teacherName = "CA Priya Nair",
                priceInr = 2200L,
                durationHours = 30L,
                level = "Intermediate",
                nextLiveSession = "Sunday 10:00 AM"
            ).getOrThrow()
        }

        val published = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observePublishedCourses().first { list -> list.any { it.id == courseId } }
        }
        assertTrue(published.any { it.id == courseId })

        val reviewResult = withTimeout(TIMEOUT_MS) {
            repo.submitCourseReview(
                courseId = courseId,
                authorName = "Riya Sen",
                rating = 5,
                comment = "Crystal clear audit evidence breakdown!"
            )
        }
        assertTrue(reviewResult.isSuccess)
    }

    @Test
    fun crossUserRead_enrollments_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser("alice_${UUID.randomUUID().toString().take(6)}@test.com")
        val aliceRepo = CommerceRepository(firestore)
        withTimeout(TIMEOUT_MS) {
            aliceRepo.ensureUserProfile(aliceUid, "Alice", null)
        }

        signInTestUser("bob_${UUID.randomUUID().toString().take(6)}@test.com")
        val bobRepo = CommerceRepository(firestore)

        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                bobRepo.observeEnrollments(aliceUid).first()
            }
            fail("Expected PERMISSION_DENIED for cross-user enrollment observation")
        } catch (e: Exception) {
            val fsError = e.unwrapFirestoreException()
            assertNotNull("Expected FirebaseFirestoreException, got $e", fsError)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsError!!.code)
        }
    }

    @Test
    fun unauthenticatedQuery_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        try {
            withTimeout(TIMEOUT_MS) {
                firestore.collection("courses")
                    .whereEqualTo("status", "published")
                    .get()
                    .await()
            }
            fail("Expected PERMISSION_DENIED for unauthenticated courses query")
        } catch (e: Exception) {
            val fsError = e.unwrapFirestoreException() ?: (e as? FirebaseFirestoreException)
            assertNotNull("Expected FirebaseFirestoreException, got $e", fsError)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsError!!.code)
        }
    }

    private companion object {
        const val TIMEOUT_MS = 8000L
        const val FLOW_TIMEOUT_MS = 5000L
    }
}
