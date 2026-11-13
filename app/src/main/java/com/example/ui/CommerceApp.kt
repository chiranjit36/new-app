package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.R
import com.example.auth.authStateFlow
import com.example.auth.signOut
import com.example.data.CommerceRepository
import com.example.model.AppRole
import com.example.model.SubscriptionPlanOption
import com.example.ui.i18n.AppStringsProvider
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AiMentorScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CourseDetailScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.LearningDashboardScreen
import com.example.ui.screens.TeacherPanelScreen
import com.example.ui.theme.MonoFinanceFamily
import com.example.viewmodel.AiMentorViewModel
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AppNavigation(auth: FirebaseAuth = Firebase.auth) {
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val user = currentUser

    if (user == null) {
        AuthScreen(onAuthSuccess = {})
    } else {
        key(user.uid) {
            AuthenticatedHubShell(
                currentUserId = user.uid,
                displayName = user.displayName ?: "Commerce Scholar",
                photoUrl = user.photoUrl?.toString(),
                userEmail = user.email
            )
        }
    }
}

private data class NavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthenticatedHubShell(
    currentUserId: String,
    displayName: String,
    photoUrl: String?,
    userEmail: String?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val mainViewModel: MainViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                MainViewModel(
                    repository = CommerceRepository(db),
                    currentUserId = currentUserId,
                    initialDisplayName = displayName,
                    initialPhotoUrl = photoUrl,
                    userEmail = userEmail
                )
            }
        }
    )

    val aiViewModel: AiMentorViewModel = viewModel(key = "ai_$currentUserId")

    val userProfile by mainViewModel.userProfile.collectAsStateWithLifecycle()
    val currentLanguage by mainViewModel.currentLanguage.collectAsStateWithLifecycle()
    val activeRole by mainViewModel.activeRole.collectAsStateWithLifecycle()
    val selectedCategory by mainViewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by mainViewModel.searchQuery.collectAsStateWithLifecycle()
    val allCourses by mainViewModel.allCourses.collectAsStateWithLifecycle()
    val filteredCourses by mainViewModel.filteredCourses.collectAsStateWithLifecycle()
    val teacherCourses by mainViewModel.teacherCourses.collectAsStateWithLifecycle()
    val enrollmentsState by mainViewModel.enrollmentsState.collectAsStateWithLifecycle()
    val attendance by mainViewModel.attendanceRecords.collectAsStateWithLifecycle()
    val quizAttempts by mainViewModel.quizAttempts.collectAsStateWithLifecycle()
    val certificates by mainViewModel.certificates.collectAsStateWithLifecycle()
    val payments by mainViewModel.payments.collectAsStateWithLifecycle()
    val notifications by mainViewModel.notifications.collectAsStateWithLifecycle()
    val referrals by mainViewModel.referrals.collectAsStateWithLifecycle()
    val selectedCourseReviews by mainViewModel.selectedCourseReviews.collectAsStateWithLifecycle()
    val actionMessage by mainViewModel.actionMessage.collectAsStateWithLifecycle()
    val isBusy by mainViewModel.isBusy.collectAsStateWithLifecycle()

    val aiMessages by aiViewModel.messages.collectAsStateWithLifecycle()
    val isAiStreaming by aiViewModel.isChatStreaming.collectAsStateWithLifecycle()
    val aiChatError by aiViewModel.chatError.collectAsStateWithLifecycle()
    val aiPlanState by aiViewModel.planState.collectAsStateWithLifecycle()

    val strings = AppStringsProvider.forLanguage(currentLanguage)
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var openCourseId by rememberSaveable { mutableStateOf<String?>(null) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var pendingSubscriptionPlan by remember { mutableStateOf<SubscriptionPlanOption?>(null) }

    LaunchedEffect(openCourseId) {
        mainViewModel.setSelectedCourseId(openCourseId)
    }

    LaunchedEffect(actionMessage) {
        actionMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            mainViewModel.clearActionMessage()
        }
    }

    val enrollmentsList = (enrollmentsState as? UiState.Success)?.data ?: emptyList()
    val unreadCount = notifications.count { !it.isRead }

    CompositionLocalProvider(LocalAppStrings provides strings) {
        val openCourse = openCourseId?.let { id -> allCourses.find { it.id == id } }
        if (openCourse != null) {
            CourseDetailScreen(
                course = openCourse,
                enrollment = enrollmentsList.find { it.courseId == openCourse.id },
                walletCredits = userProfile?.walletCredits?.toInt() ?: 0,
                reviews = selectedCourseReviews,
                onBack = { openCourseId = null },
                onEnrollAndPay = { method, useWallet ->
                    mainViewModel.enrollCourse(openCourse, method, useWallet)
                },
                onWatchAndMarkAttendance = { lesson ->
                    mainViewModel.completeLessonAndMarkAttendance(
                        course = openCourse,
                        lesson = lesson,
                        currentEnrollment = enrollmentsList.find { it.courseId == openCourse.id }
                    )
                },
                onSubmitQuiz = { answers ->
                    mainViewModel.submitQuiz(openCourse, answers)
                },
                onSubmitReview = { rating, comment ->
                    mainViewModel.submitReview(openCourse.id, rating, comment)
                }
            )
        } else {
            val studentTabs = listOf(
                NavTab(strings.discoverTab, Icons.Filled.Explore, Icons.Outlined.Explore, "nav_discover"),
                NavTab(strings.learningTab, Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_learning"),
                NavTab(strings.aiMentorTab, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_ai_mentor"),
                NavTab(strings.profileTab, Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, "nav_account")
            )

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useNavigationRail = maxWidth >= 700.dp

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = strings.appTitle,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Surface(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier.clickable {
                                            val next = when (activeRole) {
                                                AppRole.STUDENT -> AppRole.TEACHER
                                                AppRole.TEACHER -> AppRole.ADMIN
                                                AppRole.ADMIN -> AppRole.STUDENT
                                            }
                                            mainViewModel.setRole(next)
                                        }
                                    ) {
                                        Text(
                                            text = when (activeRole) {
                                                AppRole.STUDENT -> strings.studentPanel
                                                AppRole.TEACHER -> strings.teacherPanel
                                                AppRole.ADMIN -> strings.adminPanel
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { showNotificationsDialog = true },
                                    modifier = Modifier.testTag("notifications_button")
                                ) {
                                    BadgedBox(
                                        badge = {
                                            if (unreadCount > 0) {
                                                Badge { Text("$unreadCount") }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Notifications,
                                            contentDescription = strings.notificationsTitle
                                        )
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        if (!useNavigationRail && activeRole == AppRole.STUDENT) {
                            NavigationBar(windowInsets = WindowInsets.navigationBars) {
                                studentTabs.forEachIndexed { idx, tab ->
                                    NavigationBarItem(
                                        selected = selectedTabIndex == idx,
                                        onClick = { selectedTabIndex = idx },
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedTabIndex == idx) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.title
                                            )
                                        },
                                        label = { Text(tab.title, maxLines = 1) },
                                        modifier = Modifier.testTag(tab.tag)
                                    )
                                }
                            }
                        } else if (!useNavigationRail && activeRole != AppRole.STUDENT) {
                            NavigationBar(windowInsets = WindowInsets.navigationBars) {
                                NavigationBarItem(
                                    selected = true,
                                    onClick = {},
                                    icon = { Icon(Icons.Filled.Explore, contentDescription = null) },
                                    label = {
                                        Text(
                                            if (activeRole == AppRole.TEACHER) strings.teacherPanel else strings.adminPanel
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    selected = false,
                                    onClick = {
                                        mainViewModel.setRole(AppRole.STUDENT)
                                        selectedTabIndex = 0
                                    },
                                    icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = null) },
                                    label = { Text("Back to Student") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (useNavigationRail && activeRole == AppRole.STUDENT) {
                            NavigationRail {
                                studentTabs.forEachIndexed { idx, tab ->
                                    NavigationRailItem(
                                        selected = selectedTabIndex == idx,
                                        onClick = { selectedTabIndex = idx },
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedTabIndex == idx) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.title
                                            )
                                        },
                                        label = { Text(tab.title) }
                                    )
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                            when (activeRole) {
                                AppRole.TEACHER -> {
                                    TeacherPanelScreen(
                                        teacherCourses = teacherCourses,
                                        isBusy = isBusy,
                                        onPublishCourse = { title, cat, desc, price, hrs, lvl, live ->
                                            mainViewModel.publishTeacherCourse(title, cat, desc, price, hrs, lvl, live)
                                        }
                                    )
                                }
                                AppRole.ADMIN -> {
                                    AdminPanelScreen(
                                        isVerifiedAdmin = mainViewModel.isVerifiedAdminAccount,
                                        allCourses = allCourses,
                                        enrollments = enrollmentsList,
                                        payments = payments
                                    )
                                }
                                AppRole.STUDENT -> {
                                    when (selectedTabIndex) {
                                        0 -> DiscoverScreen(
                                            userProfile = userProfile,
                                            courses = filteredCourses,
                                            enrollments = enrollmentsList,
                                            selectedCategory = selectedCategory,
                                            searchQuery = searchQuery,
                                            onSelectCategory = mainViewModel::selectCategory,
                                            onSearchChange = mainViewModel::updateSearchQuery,
                                            onOpenCourse = { openCourseId = it.id },
                                            onOpenAiMentor = { selectedTabIndex = 2 },
                                            onPurchaseSubscription = { plan -> pendingSubscriptionPlan = plan }
                                        )
                                        1 -> LearningDashboardScreen(
                                            enrollmentsState = enrollmentsState,
                                            attendance = attendance,
                                            quizAttempts = quizAttempts,
                                            certificates = certificates,
                                            allCourses = allCourses,
                                            onOpenCourse = { openCourseId = it.id },
                                            onExploreCourses = { selectedTabIndex = 0 }
                                        )
                                        2 -> AiMentorScreen(
                                            messages = aiMessages,
                                            isStreaming = isAiStreaming,
                                            chatError = aiChatError,
                                            planState = aiPlanState,
                                            enrollments = enrollmentsList,
                                            quizAttempts = quizAttempts,
                                            allCourses = allCourses,
                                            onAskDoubt = aiViewModel::askDoubt,
                                            onDismissError = aiViewModel::clearChatError,
                                            onGeneratePlan = aiViewModel::generatePersonalizedStudyPlan
                                        )
                                        else -> AccountScreen(
                                            userProfile = userProfile,
                                            userEmail = userEmail,
                                            activeRole = activeRole,
                                            currentLanguage = currentLanguage,
                                            payments = payments,
                                            referrals = referrals,
                                            onRoleChange = mainViewModel::setRole,
                                            onLanguageChange = mainViewModel::changeLanguage,
                                            onSendReferral = mainViewModel::sendReferral,
                                            onSignOut = {
                                                signOut(credentialManager = credentialManager, scope = scope)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Notifications Dialog
        if (showNotificationsDialog) {
            Dialog(onDismissRequest = { showNotificationsDialog = false }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(strings.notificationsTitle, style = MaterialTheme.typography.headlineSmall)
                        if (notifications.isEmpty()) {
                            Text(
                                "You are all caught up!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.height(280.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(notifications, key = { it.id }) { item ->
                                    Card(
                                        onClick = { mainViewModel.markNotificationRead(item.id) },
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (item.isRead) {
                                                MaterialTheme.colorScheme.surfaceVariant
                                            } else {
                                                MaterialTheme.colorScheme.primaryContainer
                                            }
                                        ),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(item.title, style = MaterialTheme.typography.titleSmall)
                                            Text(item.body, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = { showNotificationsDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }

        // Subscription Upgrade Checkout Dialog
        pendingSubscriptionPlan?.let { plan ->
            var method by remember { mutableStateOf("UPI AutoPay") }
            Dialog(onDismissRequest = { pendingSubscriptionPlan = null }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Activate ${plan.name}", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            "₹${plan.priceInr} (${plan.billingCycle})",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = MonoFinanceFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        listOf("UPI AutoPay", "Credit Card", "NetBanking").forEach { m ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = method == m, onClick = { method = m })
                                Text(m, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(onClick = { pendingSubscriptionPlan = null }) {
                                Text("Cancel")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    mainViewModel.purchaseSubscription(plan.id, plan.name, plan.priceInr, method)
                                    pendingSubscriptionPlan = null
                                }
                            ) {
                                Text("Confirm ₹${plan.priceInr}")
                            }
                        }
                    }
                }
            }
        }
    }
}
