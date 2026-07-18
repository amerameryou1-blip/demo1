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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Challenge
import com.example.ui.GreenJourneyViewModel
import com.example.ui.theme.*

@Composable
fun ChallengesScreen(
    viewModel: GreenJourneyViewModel,
    challengesList: List<Challenge>,
    modifier: Modifier = Modifier
) {
    val activeQuests = challengesList.filter { !it.completed || !it.claimed }
    val completedQuests = challengesList.filter { it.completed && it.claimed }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("challenges_screen_scroll")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Quest header board
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Milestones & Ecological Quests",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Challenge yourself to earn massive loyalty points bonuses by reducing your travel footprint. Complete milestones to level up your status.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Active challenges title
        item {
            Text(
                text = "Active Quests (${activeQuests.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (activeQuests.isEmpty()) {
            item {
                EmptyStateCard(message = "Congratulations! You have completed and claimed every eco milestone challenge!")
            }
        } else {
            items(activeQuests) { challenge ->
                ChallengeCardItem(
                    challenge = challenge,
                    onClaimClick = { viewModel.claimChallengeBonus(challenge.id) }
                )
            }
        }

        // Completed challenges title
        if (completedQuests.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Claimed Achievements (${completedQuests.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(completedQuests) { challenge ->
                ChallengeCardItem(
                    challenge = challenge,
                    onClaimClick = {}
                )
            }
        }
    }
}

@Composable
fun ChallengeCardItem(
    challenge: Challenge,
    onClaimClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val challengeIcon = when (challenge.iconName) {
        "Droplets" -> Icons.Default.WaterDrop
        "Smartphone" -> Icons.Default.Smartphone
        "Globe" -> Icons.Default.Language
        "Train" -> Icons.Default.DirectionsTransit
        else -> Icons.Default.EmojiEvents
    }

    val themeColor = when (challenge.iconName) {
        "Droplets" -> PrimaryLight
        "Smartphone" -> PrimaryGreen
        "Globe" -> AviationBlue
        "Train" -> GoldTier
        else -> PrimaryGreen
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (challenge.completed && !challenge.claimed) PrimaryGreen else BorderColor,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (challenge.completed && !challenge.claimed) CardBackground else SurfaceDark
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(themeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = challengeIcon,
                            contentDescription = null,
                            tint = themeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = challenge.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = challenge.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = themeColor,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PrimaryGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${challenge.bonusPoints} PTS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = PrimaryGreen
                    )
                }
            }

            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            // Progress meter or Claim button
            if (challenge.completed) {
                if (challenge.claimed) {
                    // Fully completed and claimed
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BorderColor, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CLAIMED ACHIEVEMENT",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                } else {
                    // Completed but NOT claimed yet!
                    Button(
                        onClick = onClaimClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Color(0xFF022C22),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CLAIM +${challenge.bonusPoints} PTS BONUS",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF022C22),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                // In progress, show custom progress bar
                val progressFraction = challenge.currentProgress.toFloat() / challenge.targetCount.toFloat()

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PROGRESS TRACKING",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${challenge.currentProgress} / ${challenge.targetCount}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    LinearProgressIndicator(
                        progress = progressFraction.coerceIn(0f, 1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = themeColor,
                        trackColor = BorderColor
                    )
                }
            }
        }
    }
}
