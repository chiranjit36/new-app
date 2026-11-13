package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage
import com.example.model.AppRole
import com.example.model.PaymentRecord
import com.example.model.ReferralRecord
import com.example.model.UserProfile
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.MonoFinanceFamily

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccountScreen(
    userProfile: UserProfile?,
    userEmail: String?,
    activeRole: AppRole,
    currentLanguage: AppLanguage,
    payments: List<PaymentRecord>,
    referrals: List<ReferralRecord>,
    onRoleChange: (AppRole) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onSendReferral: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val strings = LocalAppStrings.current
    var friendName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (userProfile?.displayName ?: "CH").take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = userProfile?.displayName ?: "Commerce Scholar",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        if (!userEmail.isNullOrBlank()) {
                            Text(
                                text = userEmail,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                        Surface(color = Color(0x33FFFFFF), shape = RoundedCornerShape(50)) {
                            Text(
                                text = "Plan: ${userProfile?.subscriptionPlan?.uppercase() ?: "FREE"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF0C45A),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Separate Panels Switcher (Student / Teacher / Admin)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.SupervisorAccount, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Workspace Panel", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        "Switch seamlessly between Student, Teacher, and Administrator panels.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val roles = listOf(
                        AppRole.STUDENT to strings.studentPanel,
                        AppRole.TEACHER to strings.teacherPanel,
                        AppRole.ADMIN to strings.adminPanel
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        roles.forEachIndexed { idx, (role, label) ->
                            SegmentedButton(
                                selected = activeRole == role,
                                onClick = { onRoleChange(role) },
                                shape = SegmentedButtonDefaults.itemShape(idx, roles.size),
                                modifier = Modifier.testTag("role_switch_${role.key}")
                            ) {
                                Text(label, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }

        // Multilingual Language Switcher
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Language, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Text(strings.languageTitle, style = MaterialTheme.typography.titleMedium)
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            FilterChip(
                                selected = lang == currentLanguage,
                                onClick = { onLanguageChange(lang) },
                                label = { Text(lang.nativeLabel) },
                                modifier = Modifier.testTag("lang_chip_${lang.code}")
                            )
                        }
                    }
                }
            }
        }

        // Referral Rewards Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.CardGiftcard, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                            Text(strings.referralTitle, style = MaterialTheme.typography.titleMedium)
                        }
                        Text(
                            text = "₹${userProfile?.walletCredits ?: 500}",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = MonoFinanceFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        "Your Code: ${userProfile?.referralCode ?: "CT-USER"} • Earn ₹500 per classmate who joins.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = friendName,
                            onValueChange = { friendName = it },
                            placeholder = { Text("Classmate's name") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                onSendReferral(friendName)
                                friendName = ""
                            },
                            modifier = Modifier.testTag("send_referral_button")
                        ) {
                            Text("Invite")
                        }
                    }
                    if (referrals.isNotEmpty()) {
                        Text(
                            "Recent Rewards: ${referrals.take(3).joinToString { "${it.referredName} (+₹${it.rewardInr})" }}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Payment Receipts History
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(strings.paymentHistoryTitle, style = MaterialTheme.typography.titleLarge)
            }
        }

        if (payments.isEmpty()) {
            item {
                Text(
                    "No transactions yet. Course & subscription receipts appear here automatically.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(payments, key = { it.id }) { pay ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pay.itemTitle, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "${pay.paymentMethod} • Ref: ${pay.transactionRef}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "₹${pay.amountInr}",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = MonoFinanceFamily,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(6.dp))
            OutlinedButton(
                onClick = onSignOut,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("sign_out_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(strings.signOut)
            }
        }
    }
}
