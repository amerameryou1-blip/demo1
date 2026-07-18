package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserStats
import com.example.data.model.VerifiedAction
import com.example.ui.GreenJourneyViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeDashboard(
    viewModel: GreenJourneyViewModel,
    userStats: UserStats?,
    recentActions: List<VerifiedAction>,
    modifier: Modifier = Modifier
) {
    var showQuickOffsetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_dashboard_scroll")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Welcoming & Profile header
        item {
            WelcomeHeader(userStats = userStats, onNotificationClick = {
                viewModel.setActiveTab("notifications")
            })
        }

        // Tier Loyalty Card
        item {
            userStats?.let { stats ->
                LoyaltyCard(stats = stats)
            }
        }

        // Quick action row
        item {
            QuickActionsRow(
                onScanClick = { viewModel.setActiveTab("earn") },
                onOffsetClick = { showQuickOffsetDialog = true },
                onPerksClick = { viewModel.setActiveTab("rewards") },
                onTransitClick = {
                    viewModel.verifyEcoAction(
                        category = "Airport Rail Transit",
                        airportCode = userStats?.homeAirport?.take(3) ?: "DXB",
                        customTitle = "Airport Electric Express Train",
                        points = 90,
                        co2 = 12.0f
                    )
                }
            )
        }

        // Daily quests
        item {
            DailyQuestsSection(viewModel = viewModel, userStats = userStats)
        }

        // Recent activity header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Verified Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "View Logs",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryGreen,
                    modifier = Modifier.clickable { viewModel.setActiveTab("earn") }
                )
            }
        }

        // Recent Activity List
        if (recentActions.isEmpty()) {
            item {
                EmptyStateCard(message = "No eco actions verified yet. Scan a boarding pass or refill a reusable bottle to earn points!")
            }
        } else {
            items(recentActions.take(3)) { action ->
                ActionRowItem(action = action)
            }
        }
    }

    if (showQuickOffsetDialog) {
        CarbonOffsetDialog(
            onDismiss = { showQuickOffsetDialog = false },
            onConfirm = { amount ->
                viewModel.verifyEcoAction(
                    category = "Verified Carbon Offset",
                    airportCode = userStats?.homeAirport?.take(3) ?: "DXB",
                    customTitle = "Premium SAF Aviation Offset Token",
                    points = if (amount == 50) 200 else 500,
                    co2 = amount.toFloat()
                )
                showQuickOffsetDialog = false
            }
        )
    }
}

@Composable
fun WelcomeHeader(
    userStats: UserStats?,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AviationBlue.copy(alpha = 0.15f))
                    .border(1.5.dp, AviationBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = AviationBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Welcome Back,",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = userStats?.name ?: "Sarah Jenkins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceDark)
                .border(1.dp, BorderColor, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = AviationBlue
            )
        }
    }
}

@Composable
fun LoyaltyCard(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    val tierColor = when (stats.tier) {
        "Platinum" -> Color(0xFFE2E8F0)
        "Gold" -> GoldTier
        "Silver" -> Color(0xFF94A3B8)
        else -> Color(0xFFCD7F32)
    }

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            SurfaceDark,
            CardBackground,
            SurfaceLightDark
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
            .testTag("loyalty_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(gradientBrush)
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GREEN FREQUENT FLYER",
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Text(
                            text = stats.frequentFlyerId,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }

                    // Badge status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(tierColor.copy(alpha = 0.15f))
                            .border(1.dp, tierColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = tierColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stats.tier.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = tierColor
                            )
                        }
                    }
                }

                Divider(color = BorderColor, thickness = 1.dp)

                // Stats numbers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Green Points Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stats.greenPoints.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PTS",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "CO₂ Saved",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format("%.1f", stats.co2SavedKg),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = AviationBlue
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "KG",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AviationBlue
                            )
                        }
                    }
                }

                // Progress to next tier
                val nextTierName = if (stats.tier == "Silver") "GOLD" else "PLATINUM"
                val targetPoints = if (stats.tier == "Silver") 3500 else 7500
                val progress = stats.greenPoints.toFloat() / targetPoints.toFloat()

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Next Tier: $nextTierName Status",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "${stats.greenPoints} / $targetPoints PTS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = PrimaryGreen,
                        trackColor = BorderColor
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionsRow(
    onScanClick: () -> Unit,
    onOffsetClick: () -> Unit,
    onPerksClick: () -> Unit,
    onTransitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(
            icon = Icons.Default.QrCodeScanner,
            label = "Scan QR",
            tintColor = AviationBlue,
            onClick = onScanClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.Co2,
            label = "SAF Offset",
            tintColor = PrimaryGreen,
            onClick = onOffsetClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.CardGiftcard,
            label = "Redeem Perks",
            tintColor = GoldTier,
            onClick = onPerksClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = Icons.Default.DirectionsTransit,
            label = "Airport Rail",
            tintColor = SecondaryTeal,
            onClick = onTransitClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    tintColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DailyQuestsSection(
    viewModel: GreenJourneyViewModel,
    userStats: UserStats?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldTier,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Today's Daily Eco Quests",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Refreshes Daily",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Divider(color = BorderColor)

            // Quest 1
            QuestItem(
                title = "Mobile App Boarding Check-in",
                points = "+60 Pts",
                co2 = "1.5kg saved",
                icon = Icons.Default.Smartphone,
                onClaim = {
                    viewModel.verifyEcoAction(
                        category = "Mobile Check-in",
                        airportCode = userStats?.homeAirport?.take(3) ?: "DXB",
                        customTitle = "Verified Paperless Mobile Check-in",
                        points = 60,
                        co2 = 1.5f
                    )
                }
            )

            // Quest 2
            QuestItem(
                title = "Water Bottle Hydration Refill",
                points = "+30 Pts",
                co2 = "0.8kg saved",
                icon = Icons.Default.LocalDrink,
                onClaim = {
                    viewModel.verifyEcoAction(
                        category = "Hydration Station",
                        airportCode = userStats?.homeAirport?.take(3) ?: "DXB",
                        customTitle = "Refilled Reusable Bottle at Terminal Hub",
                        points = 30,
                        co2 = 0.8f
                    )
                }
            )

            // Quest 3
            QuestItem(
                title = "Duty Free Digital Receipt Sync",
                points = "+40 Pts",
                co2 = "1.2kg saved",
                icon = Icons.Default.ReceiptLong,
                onClaim = {
                    viewModel.verifyEcoAction(
                        category = "Digital Receipt",
                        airportCode = userStats?.homeAirport?.take(3) ?: "DXB",
                        customTitle = "Synchronized Paperless Duty Free Purchase",
                        points = 40,
                        co2 = 1.2f
                    )
                }
            )
        }
    }
}

@Composable
fun QuestItem(
    title: String,
    points: String,
    co2: String,
    icon: ImageVector,
    onClaim: () -> Unit
) {
    var completed by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !completed) {
                completed = true
                onClaim()
            }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (completed) PrimaryGreen.copy(alpha = 0.15f) else SurfaceLightDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (completed) PrimaryGreen else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (completed) TextSecondary else TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = co2,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = points,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (completed) TextSecondary else PrimaryGreen,
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (completed) PrimaryGreen else Color.Transparent)
                    .border(
                        1.5.dp,
                        if (completed) PrimaryGreen else BorderColor,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color(0xFF022C22),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionRowItem(
    action: VerifiedAction,
    modifier: Modifier = Modifier
) {
    val categoryIcon = when (action.category) {
        "Digital Boarding Pass" -> Icons.Default.Smartphone
        "Verified Carbon Offset" -> Icons.Default.Co2
        "Hydration Station" -> Icons.Default.LocalDrink
        "Digital Receipt" -> Icons.Default.ReceiptLong
        "Airport Rail Transit" -> Icons.Default.DirectionsTransit
        else -> Icons.Default.CheckCircle
    }

    val iconColor = when (action.category) {
        "Verified Carbon Offset" -> AviationBlue
        "Hydration Station" -> PrimaryLight
        "Airport Rail Transit" -> GoldTier
        else -> PrimaryGreen
    }

    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(action.timestamp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = action.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = action.airportCode,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = iconColor
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+${action.pointsEarned} PTS",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Text(
                    text = "-${String.format("%.1f", action.co2OffsetKg)}kg CO₂",
                    style = MaterialTheme.typography.labelSmall,
                    color = AviationBlue
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Co2,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CarbonOffsetDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Purchase Carbon Offset",
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Contribute directly to Sustainable Aviation Fuel (SAF) offsets to neutralise travel footprint.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onConfirm(50) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceLightDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("50 kg CO₂", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("+200 Pts", color = PrimaryGreen, style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Button(
                        onClick = { onConfirm(120) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("120 kg CO₂", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
                            Text("+500 Pts", color = Color(0xFF022C22), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = CardBackground,
        shape = RoundedCornerShape(16.dp)
    )
}
