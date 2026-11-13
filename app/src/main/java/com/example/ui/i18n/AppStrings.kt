package com.example.ui.i18n

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.model.AppLanguage

data class LocalizedStrings(
    val appTitle: String,
    val discoverTab: String,
    val learningTab: String,
    val aiMentorTab: String,
    val profileTab: String,
    val studentPanel: String,
    val teacherPanel: String,
    val adminPanel: String,
    val searchCoursesHint: String,
    val liveClassesTitle: String,
    val featuredCoursesTitle: String,
    val facultyTitle: String,
    val subscriptionPlansTitle: String,
    val enrollNow: String,
    val enrolledBadge: String,
    val myDashboardTitle: String,
    val attendanceTitle: String,
    val progressReportsTitle: String,
    val studyMaterialsTitle: String,
    val quizzesTitle: String,
    val certificatesTitle: String,
    val aiDoubtSolverTitle: String,
    val aiStudyPlanTitle: String,
    val askDoubtPlaceholder: String,
    val referralTitle: String,
    val paymentHistoryTitle: String,
    val notificationsTitle: String,
    val languageTitle: String,
    val signOut: String
)

object AppStringsProvider {
    private val en = LocalizedStrings(
        appTitle = "COMMERCETIME",
        discoverTab = "Home",
        learningTab = "My Learning",
        aiMentorTab = "AI Doubt",
        profileTab = "Account",
        studentPanel = "Student",
        teacherPanel = "Teacher",
        adminPanel = "Admin",
        searchCoursesHint = "Search Class 11, 12, B.Com, Accounting...",
        liveClassesTitle = "Live & Recorded Classes",
        featuredCoursesTitle = "All Commerce Batches",
        facultyTitle = "Our Faculty — Apurba Sarkar",
        subscriptionPlansTitle = "Membership Plans",
        enrollNow = "Join & Pay",
        enrolledBadge = "Enrolled",
        myDashboardTitle = "Student Dashboard",
        attendanceTitle = "Attendance Log",
        progressReportsTitle = "Progress Analytics",
        studyMaterialsTitle = "Study Materials",
        quizzesTitle = "Course Quizzes",
        certificatesTitle = "Earned Certificates",
        aiDoubtSolverTitle = "24x7 E-Learning AI Assistant",
        aiStudyPlanTitle = "AI Study Roadmap",
        askDoubtPlaceholder = "Ask Apurba Da's AI assistant any Commerce doubt...",
        referralTitle = "Refer & Earn ₹500",
        paymentHistoryTitle = "Payment Receipts",
        notificationsTitle = "Notifications",
        languageTitle = "Teaching & App Language",
        signOut = "Sign Out"
    )

    private val hi = LocalizedStrings(
        appTitle = "COMMERCETIME",
        discoverTab = "होम",
        learningTab = "मेरी पढ़ाई",
        aiMentorTab = "AI मदद",
        profileTab = "खाता",
        studentPanel = "छात्र",
        teacherPanel = "शिक्षक",
        adminPanel = "प्रशासक",
        searchCoursesHint = "कक्षा 11, 12, B.Com, अकाउंटिंग खोजें...",
        liveClassesTitle = "लाइव और रिकॉर्डेड कक्षाएं",
        featuredCoursesTitle = "सभी कॉमर्स बैच",
        facultyTitle = "शिक्षक — अपूर्ब सरकार",
        subscriptionPlansTitle = "सदस्यता योजनाएं",
        enrollNow = "जुड़ें और भुगतान करें",
        enrolledBadge = "नामांकित",
        myDashboardTitle = "छात्र डैशबोर्ड",
        attendanceTitle = "उपस्थिति रिकॉर्ड",
        progressReportsTitle = "प्रगति रिपोर्ट",
        studyMaterialsTitle = "अध्ययन सामग्री",
        quizzesTitle = "कोर्स क्विज़",
        certificatesTitle = "प्रमाण पत्र",
        aiDoubtSolverTitle = "24x7 ई-लर्निंग AI सहायक",
        aiStudyPlanTitle = "AI अध्ययन योजना",
        askDoubtPlaceholder = "अकाउंट्स, GST, या B.Com का प्रश्न पूछें...",
        referralTitle = "रेफर करें और ₹500 कमाएं",
        paymentHistoryTitle = "भुगतान रसीदें",
        notificationsTitle = "सूचनाएं",
        languageTitle = "भाषा",
        signOut = "लॉग आउट"
    )

    private val bn = LocalizedStrings(
        appTitle = "COMMERCETIME",
        discoverTab = "হোম",
        learningTab = "আমার পড়াশোনা",
        aiMentorTab = "AI সহায়ক",
        profileTab = "অ্যাকাউন্ট",
        studentPanel = "ছাত্র",
        teacherPanel = "শিক্ষক",
        adminPanel = "অ্যাডমিন",
        searchCoursesHint = "ক্লাস ১১, ১২, B.Com, অ্যাকাউন্টিং খুঁজুন...",
        liveClassesTitle = "লাইভ ও রেকর্ডেড ক্লাস",
        featuredCoursesTitle = "সকল কমার্স ব্যাচ",
        facultyTitle = "আমাদের শিক্ষক — অপূর্ব সরকার",
        subscriptionPlansTitle = "মেম্বারশিপ প্ল্যান",
        enrollNow = "ভর্তি ও পেমেন্ট",
        enrolledBadge = "ভর্তি সম্পন্ন",
        myDashboardTitle = "স্টুডেন্ট ড্যাশবোর্ড",
        attendanceTitle = "উপস্থিতি লগ",
        progressReportsTitle = "প্রগতি রিপোর্ট",
        studyMaterialsTitle = "স্টাডি মেটেরিয়াল",
        quizzesTitle = "কোর্স কুইজ",
        certificatesTitle = "সার্টিফিকেট",
        aiDoubtSolverTitle = "24x7 ই-লার্নিং AI অ্যাসিস্ট্যান্ট",
        aiStudyPlanTitle = "AI স্টাডি রোডম্যাপ",
        askDoubtPlaceholder = "অ্যাকাউন্টিং বা কমার্সের যেকোনো প্রশ্ন করুন...",
        referralTitle = "রেফার করুন ও ₹500 জিতুন",
        paymentHistoryTitle = "পেমেন্ট রসিদ",
        notificationsTitle = "নোটিফিকেশন",
        languageTitle = "অ্যাপের ভাষা",
        signOut = "সাইন আউট"
    )

    private val es = LocalizedStrings(
        appTitle = "COMMERCETIME",
        discoverTab = "Inicio",
        learningTab = "Mi Estudio",
        aiMentorTab = "Tutor IA",
        profileTab = "Cuenta",
        studentPanel = "Estudiante",
        teacherPanel = "Docente",
        adminPanel = "Admin",
        searchCoursesHint = "Buscar Clase 11, 12, B.Com, Contabilidad...",
        liveClassesTitle = "Clases en Vivo",
        featuredCoursesTitle = "Cursos de Comercio",
        facultyTitle = "Profesor — Apurba Sarkar",
        subscriptionPlansTitle = "Planes de Suscripción",
        enrollNow = "Unirse y Pagar",
        enrolledBadge = "Inscrito",
        myDashboardTitle = "Panel del Estudiante",
        attendanceTitle = "Asistencia",
        progressReportsTitle = "Progreso",
        studyMaterialsTitle = "Material de Estudio",
        quizzesTitle = "Cuestionarios",
        certificatesTitle = "Certificados",
        aiDoubtSolverTitle = "Asistente IA 24x7",
        aiStudyPlanTitle = "Plan de Estudio IA",
        askDoubtPlaceholder = "Pregunta sobre contabilidad o finanzas...",
        referralTitle = "Invita y Gana ₹500",
        paymentHistoryTitle = "Recibos de Pago",
        notificationsTitle = "Notificaciones",
        languageTitle = "Idioma",
        signOut = "Cerrar Sesión"
    )

    fun forLanguage(lang: AppLanguage): LocalizedStrings = when (lang) {
        AppLanguage.EN -> en
        AppLanguage.HI -> hi
        AppLanguage.BN -> bn
        AppLanguage.ES -> es
    }
}

val LocalAppStrings = staticCompositionLocalOf { AppStringsProvider.forLanguage(AppLanguage.EN) }
