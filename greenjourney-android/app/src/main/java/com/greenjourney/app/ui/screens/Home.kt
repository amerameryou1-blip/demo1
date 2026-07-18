package com.greenjourney.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenjourney.app.data.AppState
import com.greenjourney.app.data.UserStats
import com.greenjourney.app.data.VerifiedAction
import com.greenjourney.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun tierColor(tier: String) = when (tier) {
    "Platinum" -> Color(0xFFE2E8F0)
    "Gold" -> Gold
    "Silver" -> Color(0xFFCBD5E1)
    else -> Color(0xFFF59E0B)
}

private fun fmt(ts: Long): String =
    SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(ts))

@Composable
fun HomeScreen(state: AppState) {
    var showOffset by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item { WelcomeHeader(state) }
        item { LoyaltyCard(state.stats) }
        item { QuickActionsRow(state, onOffset = { showOffset = true }) }
        item { DailyQuestsCard(state) }
        item { RecentHeader(onViewLogs = { state.activeTab = "earn" }) }
        if (state.actions.isEmpty()) {
            item {
                EmptyStateCard("No eco actions verified yet. Scan a boarding pass or refill a reusable bottle to earn points!")
            }
        } else {
            items(state.actions.take(3), key = { it.id }) { ActionRow(it) }
        }
    }

    if (showOffset) {
        CarbonOffsetDialog(
            onDismiss = { showOffset = false },
            onConfirm = { amount ->
                state.verifyEcoAction(
                    category = "Verified Carbon Offset",
                    airportCode = state.stats.homeAirport.take(3),
                    customTitle = "Premium SAF Aviation Offset Token",
                    points = if (amount == 50) 200 else 500,
                    co2 = amount.toFloat(),
                )
                showOffset = false
            },
        )
    }
}

@Composable
private fun WelcomeHeader(state: AppState) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0B1425))
                    .border(1.5.dp, Brush.linearGradient(listOf(Mint, Cyan, Sky)), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    state.stats.name.split(" ")
                        .mapNotNull { it.firstOrNull() }
                        .take(2)
                        .joinToString(""),
                    color = Color(0xFFA5F3FC),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("WELCOME BACK,", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.8.sp)
                Text(state.stats.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x0AFFFFFF))
                .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(12.dp))
                .clickable { state.activeTab = "notifications" },
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun LoyaltyCard(stats: UserStats) {
    val tierC = tierColor(stats.tier)
    val nextTierName = if (stats.tier == "Silver") "GOLD" else "PLATINUM"
    val targetPoints = if (stats.tier == "Silver") 3500 else 7500
    val progress = (stats.greenPoints.toFloat() / targetPoints.toFloat()).coerceIn(0f, 1f)

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xF0162138), Color(0xF00A1022))
                )
            )
            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(22.dp))
    ) {
        Box(
            Modifier
                .size(220.dp)
                .align(Alignment.TopStart)
                .offset(x = (-60).dp, y = (-80).dp)
                .background(Brush.radialGradient(listOf(Color(0x4034D399), Color.Transparent)), CircleShape)
        )
        Box(
            Modifier
                .size(240.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 70.dp, y = 80.dp)
                .background(Brush.radialGradient(listOf(Color(0x3D22D3EE), Color.Transparent)), CircleShape)
        )
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        "GREEN FREQUENT FLYER",
                        color = Mint,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.6.sp,
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(stats.frequentFlyerId, color = TextSecondary, fontSize = 11.5.sp, fontFamily = FontFamily.Monospace)
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(tierC.copy(alpha = 0.10f))
                        .border(1.dp, tierC.copy(alpha = 0.35f), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = tierC, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(stats.tier.uppercase(), color = tierC, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.6.sp)
                    }
                }
            }

            HorizontalDivider(color = Color(0x1AFFFFFF), thickness = 1.dp)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Green Points Balance", color = TextSecondary, fontSize = 11.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "%,d".format(stats.greenPoints),
                            color = TextPrimary,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black,
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("PTS", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("CO\u2082 Saved", color = TextSecondary, fontSize = 11.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("%.1f".format(stats.co2SavedKg), color = Color(0xFF67E8F9), fontSize = 30.sp, fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(5.dp))
                        Text("KG", color = Color(0xFF67E8F9), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 5.dp))
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("NEXT TIER: $nextTierName STATUS", color = TextSecondary, fontSize = 10.5.sp, letterSpacing = 0.8.sp)
                    Text(
                        "%,d / %,d PTS".format(stats.greenPoints, targetPoints),
                        color = TextPrimary,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                GradientProgress(progress = progress, height = 7.dp)
            }
        }
    }
}

private data class QuickAction(val icon: ImageVector, val label: String, val tint: Color, val onClick: () -> Unit)

@Composable
private fun QuickActionsRow(state: AppState, onOffset: () -> Unit) {
    val items = listOf(
        QuickAction(Icons.Default.QrCodeScanner, "Scan QR", Color(0xFF67E8F9)) { state.activeTab = "earn" },
        QuickAction(Icons.Default.Eco, "SAF Offset", Mint, onOffset),
        QuickAction(Icons.Default.CardGiftcard, "Redeem Perks", Gold) { state.activeTab = "rewards" },
        QuickAction(Icons.Default.Train, "Airport Rail", TealC) {
            state.verifyEcoAction(
                category = "Airport Rail Transit",
                airportCode = state.stats.homeAirport.take(3),
                customTitle = "Airport Electric Express Train",
                points = 90,
                co2 = 12.0f,
            )
        },
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { qa ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Color(0x10FFFFFF), Color(0x05FFFFFF))))
                        .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp))
                        .clickable(onClick = qa.onClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(qa.icon, qa.label, tint = qa.tint, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.height(6.dp))
                Text(qa.label, color = TextPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun DailyQuestsCard(state: AppState) {
    GlassCard(Modifier.fillMaxWidth(), corner = 16.dp) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text("Today's Daily Eco Quests", color = TextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                }
                Text("REFRESHES DAILY", color = TextMuted, fontSize = 10.sp, letterSpacing = 0.8.sp)
            }
            HorizontalDivider(color = Color(0x1AFFFFFF))
            QuestItem(
                title = "Mobile App Boarding Check-in",
                points = "+60 Pts",
                co2 = "1.5kg saved",
                icon = Icons.Default.Smartphone,
            ) {
                state.verifyEcoAction("Mobile Check-in", state.stats.homeAirport.take(3), "Verified Paperless Mobile Check-in", 60, 1.5f)
            }
            QuestItem(
                title = "Water Bottle Hydration Refill",
                points = "+30 Pts",
                co2 = "0.8kg saved",
                icon = Icons.Default.LocalDrink,
            ) {
                state.verifyEcoAction("Hydration Station", state.stats.homeAirport.take(3), "Refilled Reusable Bottle at Terminal Hub", 30, 0.8f)
            }
            QuestItem(
                title = "Duty Free Digital Receipt Sync",
                points = "+40 Pts",
                co2 = "1.2kg saved",
                icon = Icons.Default.ReceiptLong,
            ) {
                state.verifyEcoAction("Digital Receipt", state.stats.homeAirport.take(3), "Synchronized Paperless Duty Free Purchase", 40, 1.2f)
            }
        }
    }
}

@Composable
private fun QuestItem(title: String, points: String, co2: String, icon: ImageVector, onClaim: () -> Unit) {
    var completed by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !completed) { completed = true; onClaim() }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (completed) Mint.copy(alpha = 0.15f) else Color(0xFF111D33))
                    .border(1.dp, if (completed) Mint.copy(alpha = 0.4f) else Color(0x12FFFFFF), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, tint = if (completed) Mint else TextSecondary, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    color = if (completed) TextMuted else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (completed) TextDecoration.LineThrough else null,
                )
                Text(co2, color = TextMuted, fontSize = 10.5.sp)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(points, color = if (completed) TextMuted else Mint, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (completed) Mint else Color.Transparent)
                    .border(1.5.dp, if (completed) Mint else Color(0x26FFFFFF), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (completed) Icon(Icons.Default.Check, null, tint = MintDeep, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun RecentHeader(onViewLogs: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Recent Verified Actions", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onViewLogs)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text("View Logs", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Mint, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
internal fun ActionRow(action: VerifiedAction) {
    val (icon, tint) = when (action.category) {
        "Digital Boarding Pass" -> Icons.Default.Smartphone to Mint
        "Verified Carbon Offset" -> Icons.Default.Air to Color(0xFF67E8F9)
        "Hydration Station" -> Icons.Default.LocalDrink to Sky
        "Digital Receipt" -> Icons.Default.ReceiptLong to Mint
        "Airport Rail Transit" -> Icons.Default.Train to Gold
        else -> Icons.Default.CheckCircle to Mint
    }
    GlassCard(Modifier.fillMaxWidth(), corner = 14.dp) {
        Row(Modifier.padding(14.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(tint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(action.title, color = TextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(action.airportCode, color = tint, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        Text("  \u2022  ", color = TextMuted, fontSize = 10.5.sp)
                        Text(fmt(action.timestamp), color = TextMuted, fontSize = 10.5.sp)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("+${action.pointsEarned} PTS", color = Mint, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                Text("-${"%.1f".format(action.co2OffsetKg)}kg CO\u2082", color = Color(0xFF67E8F9), fontSize = 10.5.sp)
            }
        }
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    GlassCard(Modifier.fillMaxWidth(), corner = 14.dp) {
        Column(
            Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x0AFFFFFF)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Eco, null, tint = TextMuted, modifier = Modifier.size(26.dp))
            }
            Text(message, color = TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun CarbonOffsetDialog(onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0D1529),
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Mint.copy(alpha = 0.15f))
                        .border(1.dp, Mint.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Eco, null, tint = Mint, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(10.dp))
                Text("Purchase Carbon Offset", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Contribute directly to Sustainable Aviation Fuel (SAF) offsets to neutralise travel footprint.",
                    color = TextSecondary,
                    fontSize = 12.5.sp,
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x0AFFFFFF))
                            .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(12.dp))
                            .clickable { onConfirm(50) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("50 kg CO\u2082", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("+200 Pts", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(Mint, Emerald)))
                            .clickable { onConfirm(120) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("120 kg CO\u2082", color = MintDeep, fontWeight = FontWeight.Black, fontSize = 13.sp)
                            Text("+500 Pts", color = MintDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        },
    )
}
