const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const ADMIN_EMAIL = "pandeychiranjit45@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function serverDate() {
  return new Date();
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read users, courses, or enrollments", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("courses").where("status", "==", "published").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("enrollments").get()
  );
});

test("User profile: owner can create student profile and read/update safe fields, cannot escalate role", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const aliceDoc = aliceDb.collection("users").doc(ALICE_UID);

  // Valid creation
  await assertSucceeds(
    aliceDoc.set({
      userId: ALICE_UID,
      displayName: "Alice Sharma",
      role: "student",
      preferredLanguage: "en",
      subscriptionPlan: "free",
      referralCode: "CHUB-ALIC",
      walletCredits: 500,
      studyStreakDays: 1,
      createdAt: serverDate(),
      updatedAt: serverDate(),
    })
  );

  // Owner read succeeds
  await assertSucceeds(aliceDoc.get());

  // Cross-user read fails
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());

  // Self-escalation to admin fails
  await assertFails(
    aliceDoc.update({
      role: "admin",
      updatedAt: serverDate(),
    })
  );

  // Shadow field injection fails
  await assertFails(
    aliceDoc.update({
      isVerified: true,
      updatedAt: serverDate(),
    })
  );

  // Valid language update succeeds
  await assertSucceeds(
    aliceDoc.update({
      preferredLanguage: "hi",
      updatedAt: serverDate(),
    })
  );
});

test("User profile: cannot create profile with role=admin even as regular user", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(
    bobDb.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob",
      role: "admin",
      preferredLanguage: "en",
      subscriptionPlan: "free",
      referralCode: "CHUB-BOB1",
      walletCredits: 500,
      studyStreakDays: 0,
      createdAt: serverDate(),
      updatedAt: serverDate(),
    })
  );
});

test("Email spoofing test: unverified admin email cannot write admin-protected updates", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      role: "student",
      preferredLanguage: "en",
      subscriptionPlan: "free",
      referralCode: "CHUB-ALIC",
      walletCredits: 500,
      studyStreakDays: 1,
      createdAt: serverDate(),
      updatedAt: serverDate(),
    });
  });

  const spoofedDb = testEnv
    .authenticatedContext("attacker_uid", {
      email: ADMIN_EMAIL,
      email_verified: false,
    })
    .firestore();

  await assertFails(
    spoofedDb.collection("users").doc(ALICE_UID).update({
      role: "teacher",
      updatedAt: serverDate(),
    })
  );
});

test("Courses & Reviews: query alignment, teacher ownership, and review ownership", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  // Teacher Alice creates a course
  await assertSucceeds(
    aliceDb.collection("courses").doc("course_tax_101").set({
      title: "Direct & Indirect Taxation Pro",
      category: "Taxation",
      description: "Comprehensive GST and Income Tax masterclass with live case studies.",
      teacherId: ALICE_UID,
      teacherName: "CA Alice Sharma",
      priceInr: 2499,
      durationHours: 36,
      level: "Intermediate",
      status: "published",
      nextLiveSession: "Tomorrow, 7:00 PM",
      ratingAvg: 4.9,
      createdAt: serverDate(),
      updatedAt: serverDate(),
    })
  );

  // Querying published courses with matching filter succeeds
  await assertSucceeds(
    bobDb.collection("courses").where("status", "==", "published").get()
  );

  // Unfiltered course query fails (rules are not filters)
  await assertFails(bobDb.collection("courses").get());

  // Bob cannot modify Alice's course
  await assertFails(
    bobDb.collection("courses").doc("course_tax_101").update({
      priceInr: 0,
      updatedAt: serverDate(),
    })
  );

  // Bob can post a review on the course
  await assertSucceeds(
    bobDb
      .collection("courses")
      .doc("course_tax_101")
      .collection("reviews")
      .doc("rev_bob")
      .set({
        userId: BOB_UID,
        authorName: "Bob Verma",
        courseId: "course_tax_101",
        rating: 5,
        comment: "Very clear GST input tax credit examples!",
        status: "visible",
        createdAt: serverDate(),
        updatedAt: serverDate(),
      })
  );

  // Alice can list visible reviews
  await assertSucceeds(
    aliceDb
      .collection("courses")
      .doc("course_tax_101")
      .collection("reviews")
      .where("status", "==", "visible")
      .get()
  );
});

test("Student subcollections: enrollments, attendance, quiz_attempts, certificates, payments, notifications, referrals", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const aliceRef = aliceDb.collection("users").doc(ALICE_UID);

  // Enrollment create & progress update
  await assertSucceeds(
    aliceRef.collection("enrollments").doc("acc_101").set({
      userId: ALICE_UID,
      courseId: "acc_101",
      courseTitle: "Financial Accounting Mastery",
      category: "Accounting",
      progressPercent: 20,
      completedLessonsCount: 2,
      totalLessonsCount: 10,
      status: "active",
      enrolledAt: serverDate(),
      updatedAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("enrollments").doc("acc_101").update({
      progressPercent: 100,
      completedLessonsCount: 10,
      status: "completed",
      updatedAt: serverDate(),
    })
  );

  // Terminal state lock: once completed, student cannot alter enrollment status
  await assertFails(
    aliceRef.collection("enrollments").doc("acc_101").update({
      progressPercent: 50,
      status: "active",
      updatedAt: serverDate(),
    })
  );

  // Attendance, Quiz, Certificate, Payment, Notification, Referral
  await assertSucceeds(
    aliceRef.collection("attendance").doc("att_1").set({
      userId: ALICE_UID,
      courseId: "acc_101",
      courseTitle: "Financial Accounting Mastery",
      sessionTitle: "Live Trial Balance Workshop",
      sessionType: "live",
      status: "present",
      markedAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("quiz_attempts").doc("quiz_1").set({
      userId: ALICE_UID,
      courseId: "acc_101",
      courseTitle: "Financial Accounting Mastery",
      quizTitle: "Balance Sheet Mastery Quiz",
      score: 90,
      totalQuestions: 5,
      passed: true,
      attemptedAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("certificates").doc("cert_1").set({
      userId: ALICE_UID,
      studentName: "Alice Sharma",
      courseId: "acc_101",
      courseTitle: "Financial Accounting Mastery",
      certificateNumber: "CHUB-2026-8841",
      scorePercent: 90,
      issuedAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("payments").doc("pay_1").set({
      userId: ALICE_UID,
      itemTitle: "Financial Accounting Mastery",
      itemType: "course",
      amountInr: 1999,
      paymentMethod: "UPI",
      status: "paid",
      transactionRef: "TXN99881122",
      paidAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("notifications").doc("notif_1").set({
      userId: ALICE_UID,
      title: "Enrollment Confirmed",
      body: "Welcome to Financial Accounting Mastery!",
      category: "payment",
      isRead: false,
      createdAt: serverDate(),
    })
  );

  await assertSucceeds(
    aliceRef.collection("notifications").doc("notif_1").update({
      isRead: true,
    })
  );

  await assertSucceeds(
    aliceRef.collection("referrals").doc("ref_1").set({
      userId: ALICE_UID,
      referredName: "Rohan Mehta",
      referralCode: "CHUB-ALIC",
      rewardInr: 500,
      status: "credited",
      createdAt: serverDate(),
    })
  );

  // Cross-user isolation check
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("payments").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("certificates").get()
  );
});
