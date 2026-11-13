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
        appTitle = "Commerce Classes Hub",
        discoverTab = "Discover",
        learningTab = "My Learning",
        aiMentorTab = "AI Mentor",
        profileTab = "Account",
        studentPanel = "Student",
        teacherPanel = "Teacher",
        adminPanel = "Admin",
        searchCoursesHint = "Search Accounting, CA, Taxation, Stocks...",
        liveClassesTitle = "Live & Upcoming Classes",
        featuredCoursesTitle = "Commerce Courses",
        facultyTitle = "Distinguished Faculty",
        subscriptionPlansTitle = "Membership Plans",
        enrollNow = "Join & Pay",
        enrolledBadge = "Enrolled",
        myDashboardTitle = "Student Dashboard",
        attendanceTitle = "Attendance Log",
        progressReportsTitle = "Progress Analytics",
        studyMaterialsTitle = "Study Materials",
        quizzesTitle = "Course Quizzes",
        certificatesTitle = "Earned Certificates",
        aiDoubtSolverTitle = "Gemini AI Doubt Solver",
        aiStudyPlanTitle = "AI Study Roadmap",
        askDoubtPlaceholder = "Ask about balance sheets, GST ITC, NPV, or upload notes...",
        referralTitle = "Refer & Earn ₹500",
        paymentHistoryTitle = "Payment Receipts",
        notificationsTitle = "Notifications",
        languageTitle = "App Language",
        signOut = "Sign Out"
    )

    private val hi = LocalizedStrings(
        appTitle = "कॉमर्स क्लासेस हब",
        discoverTab = "कोर्स खोजें",
        learningTab = "मेरी पढ़ाई",
        aiMentorTab = "AI गुरु",
        profileTab = "खाता",
        studentPanel = "छात्र",
        teacherPanel = "शिक्षक",
        adminPanel = "प्रशासक",
        searchCoursesHint = "अकाउंटिंग, CA, टैक्स, शेयर बाज़ार खोजें...",
        liveClassesTitle = "लाइव और आगामी कक्षाएं",
        featuredCoursesTitle = "कॉमर्स पाठ्यक्रम",
        facultyTitle = "अनुभवी शिक्षक",
        subscriptionPlansTitle = "सदस्यता योजनाएं",
        enrollNow = "जुड़ें और भुगतान करें",
        enrolledBadge = "नामांकित",
        myDashboardTitle = "छात्र डैशबोर्ड",
        attendanceTitle = "उपस्थिति रिकॉर्ड",
        progressReportsTitle = "प्रगति रिपोर्ट",
        studyMaterialsTitle = "अध्ययन सामग्री",
        quizzesTitle = "कोर्स क्विज़",
        certificatesTitle = "प्रमाण पत्र",
        aiDoubtSolverTitle = "Gemini AI संदेह समाधान",
        aiStudyPlanTitle = "AI अध्ययन योजना",
        askDoubtPlaceholder = "बैलेंस शीट, GST, या फाइनेंस का प्रश्न पूछें...",
        referralTitle = "रेफर करें और ₹500 कमाएं",
        paymentHistoryTitle = "भुगतान रसीदें",
        notificationsTitle = "सूचनाएं",
        languageTitle = "ऐप भाषा",
        signOut = "लॉग आउट"
    )

    private val es = LocalizedStrings(
        appTitle = "Commerce Classes Hub",
        discoverTab = "Explorar",
        learningTab = "Mi Estudio",
        aiMentorTab = "Tutor IA",
        profileTab = "Cuenta",
        studentPanel = "Estudiante",
        teacherPanel = "Docente",
        adminPanel = "Admin",
        searchCoursesHint = "Buscar Contabilidad, Finanzas, Impuestos...",
        liveClassesTitle = "Clases en Vivo",
        featuredCoursesTitle = "Cursos de Comercio",
        facultyTitle = "Profesores Destacados",
        subscriptionPlansTitle = "Planes de Suscripción",
        enrollNow = "Unirse y Pagar",
        enrolledBadge = "Inscrito",
        myDashboardTitle = "Panel del Estudiante",
        attendanceTitle = "Registro de Asistencia",
        progressReportsTitle = "Informe de Progreso",
        studyMaterialsTitle = "Material de Estudio",
        quizzesTitle = "Cuestionarios",
        certificatesTitle = "Certificados",
        aiDoubtSolverTitle = "Tutor IA Gemini",
        aiStudyPlanTitle = "Plan de Estudio IA",
        askDoubtPlaceholder = "Pregunta sobre balances, impuestos o finanzas...",
        referralTitle = "Invita y Gana ₹500",
        paymentHistoryTitle = "Recibos de Pago",
        notificationsTitle = "Notificaciones",
        languageTitle = "Idioma",
        signOut = "Cerrar Sesión"
    )

    fun forLanguage(lang: AppLanguage): LocalizedStrings = when (lang) {
        AppLanguage.EN -> en
        AppLanguage.HI -> hi
        AppLanguage.ES -> es
    }
}

val LocalAppStrings = staticCompositionLocalOf { AppStringsProvider.forLanguage(AppLanguage.EN) }
