package com.greenjourney.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenjourney.app.data.AppState
import com.greenjourney.app.data.Challenge
import com.greenjourney.app.ui.theme.*

private fun challengeIcon(key: String) = when (key) {
    "Droplets" -> Icons.Default.Opacity
    "Smartphone" -> Icons.Default.Smartphone
    "Globe" -> Icons.Default.Public
    else -> Icons.Default.Train
}

@Composable
fun ChallengesScreen(state: AppState) {
    val total = state.challenges.size
    val completed = state.challenges.count { it.completed }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 24.dp) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0x4DFBBF24), Color(0x26F97316))))
                            .border(1.dp, Color(0x66FBBF24), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.EmojiEvents, null, tint = Gold, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("ECO QUESTS", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp)
                        Text("Level Up Your Impact", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Spacer(Modifier.height(3.dp))
                        Text(
                            "$completed of $total quests completed",
                            color = TextSecondary, fontSize = 12.sp,
                        )
                    }
                }
            }
        }

        items(state.challenges, key = { it.id }) { c ->
            ChallengeCard(c) { state.claimChallengeBonus(c.id) }
        }
    }
}

@Composable
private fun ChallengeCard(c: Challenge, onClaim: () -> Unit) {
    val pct = (c.currentProgress.toFloat() / c.targetCount.toFloat()).coerceIn(0f, 1f)
    GlassCard(Modifier.fillMaxWidth(), corner = 18.dp) {
        Row(Modifier.padding(16.dp)) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (c.completed) Mint.copy(alpha = 0.18f) else Color(0x0AFFFFFF))
                    .border(1.dp, if (c.completed) Mint.copy(alpha = 0.4f) else Color(0x14FFFFFF), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(challengeIcon(c.iconKey), null, tint = if (c.completed) Mint else TextSecondary, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(Modifier.weight(1f)) {
                        Text(c.category.uppercase(), color = TextMuted, fontSize = 10.sp, letterSpacing = 1.sp)
                        Text(c.title, color = TextPrimary, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0x26FBBF24))
                            .border(1.dp, Color(0x4DFBBF24), RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Gold, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("+${c.bonusPoints} PTS", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(c.description, color = TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)

                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Progress", color = TextSecondary, fontSize = 11.sp)
                    Text("${c.currentProgress} / ${c.targetCount}", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(6.dp))
                GradientProgress(progress = pct, height = 6.dp)

                Spacer(Modifier.height(12.dp))
                when {
                    c.completed && !c.claimed -> {
                        PrimaryMintButton(onClick = onClaim, modifier = Modifier.fillMaxWidth()) {
                            Text("Claim Bonus", color = MintDeep, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    c.completed && c.claimed -> {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Mint.copy(alpha = 0.10f))
                                .border(1.dp, Mint.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Mint, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Bonus Claimed", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    else -> {
                        Text(
                            "Keep going — ${c.targetCount - c.currentProgress} more to unlock this bonus.",
                            color = TextMuted, fontSize = 11.sp,
                        )
                    }
                }
            }
        }
    }
}
