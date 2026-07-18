package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reward
import com.example.data.model.UserStats
import com.example.data.model.UserVoucher
import com.example.ui.GreenJourneyViewModel
import com.example.ui.theme.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RewardsMarketplaceScreen(
    viewModel: GreenJourneyViewModel,
    userStats: UserStats?,
    rewardsList: List<Reward>,
    vouchersList: List<UserVoucher>,
    modifier: Modifier = Modifier
) {
    var rewardsSubTab by remember { mutableStateOf("browse") } // browse, wallet
    var selectedRewardToRedeem by remember { mutableStateOf<Reward?>(null) }
    var selectedVoucherToShow by remember { mutableStateOf<UserVoucher?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("rewards_marketplace_screen")
    ) {
        // Toggle tabs at the top of the screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(SurfaceDark, RoundedCornerShape(10.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { rewardsSubTab = "browse" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rewardsSubTab == "browse") AviationBlue else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = if (rewardsSubTab == "browse") Color.White else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Browse Perks",
                        fontWeight = FontWeight.Bold,
                        color = if (rewardsSubTab == "browse") Color.White else TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            Button(
                onClick = { rewardsSubTab = "wallet" },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rewardsSubTab == "wallet") AviationBlue else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        tint = if (rewardsSubTab == "wallet") Color.White else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Voucher Wallet (${vouchersList.size})",
                        fontWeight = FontWeight.Bold,
                        color = if (rewardsSubTab == "wallet") Color.White else TextPrimary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Sub tab contents
        if (rewardsSubTab == "browse") {
            BrowseRewardsSection(
                viewModel = viewModel,
                userStats = userStats,
                rewardsList = rewardsList,
                onRedeemClick = { selectedRewardToRedeem = it }
            )
        } else {
            WalletVouchersSection(
                vouchersList = vouchersList,
                rewardsList = rewardsList,
                onVoucherClick = { selectedVoucherToShow = it }
            )
        }
    }

    if (selectedRewardToRedeem != null) {
        val reward = selectedRewardToRedeem!!
        val isAffordable = (userStats?.greenPoints ?: 0) >= reward.pointsRequired

        AlertDialog(
            onDismissRequest = { selectedRewardToRedeem = null },
            title = {
                Text(
                    text = "Confirm Redemption",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Are you sure you want to redeem your points for this perk?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderColor, RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = reward.title,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Required points: ${reward.pointsRequired} Pts",
                                fontWeight = FontWeight.Bold,
                                color = if (isAffordable) PrimaryGreen else Color.Red,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Your balance: ${userStats?.greenPoints ?: 0} Pts",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.redeemReward(reward.id) {
                            rewardsSubTab = "wallet"
                        }
                        selectedRewardToRedeem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AviationBlue),
                    enabled = isAffordable
                ) {
                    Text("Redeem Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRewardToRedeem = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (selectedVoucherToShow != null) {
        val voucher = selectedVoucherToShow!!
        val reward = rewardsList.firstOrNull { it.id == voucher.rewardId }

        AlertDialog(
            onDismissRequest = { selectedVoucherToShow = null },
            title = {
                Text(
                    text = "Lounge Voucher Pass",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = reward?.title ?: "Eco Airport Lounge Pass",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        textAlign = TextAlign.Center
                    )

                    // Simulated Visual QR Code block
                    Column(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                            .size(160.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // QR Visual Representation in pure compose (Grid layout of dark / light square lines)
                        repeat(4) { rowIndex ->
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(4) { colIndex ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                if ((rowIndex + colIndex) % 3 == 0 || (rowIndex * colIndex) % 2 == 1) Color.Black else Color.White
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = voucher.voucherCode,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "SCAN AT BOARDING GATE OR RECEPTION",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    Divider(color = BorderColor)

                    val expFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val expiryString = expFormat.format(Date(voucher.expiresAt))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("STATUS", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("ACTIVE PASS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("EXPIRES", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text(expiryString, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedVoucherToShow = null },
                    colors = ButtonDefaults.buttonColors(containerColor = AviationBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLOSE PASS", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun BrowseRewardsSection(
    viewModel: GreenJourneyViewModel,
    userStats: UserStats?,
    rewardsList: List<Reward>,
    onRedeemClick: (Reward) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Balance card header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Available Loyalty Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = (userStats?.greenPoints ?: 0).toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PTS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AviationBlue.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "TIER: ${userStats?.tier ?: "Silver"}",
                            fontWeight = FontWeight.Bold,
                            color = AviationBlue,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        // List rewards
        items(rewardsList) { reward ->
            RewardCatalogCard(
                reward = reward,
                userPoints = userStats?.greenPoints ?: 0,
                onRedeemClick = { onRedeemClick(reward) }
            )
        }
    }
}

@Composable
fun RewardCatalogCard(
    reward: Reward,
    userPoints: Int,
    onRedeemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canAfford = userPoints >= reward.pointsRequired

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column {
            // Header content showing original images loaded via Coil AsyncImage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Background fallback gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(CardBackground, SurfaceLightDark, BorderColor)
                            )
                        )
                )

                // Loaded original Unsplash image
                AsyncImage(
                    model = reward.imageUrl,
                    contentDescription = reward.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // High-contrast premium dark overlay to protect text legibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PrimaryGreen.copy(alpha = 0.2f))
                            .border(1.dp, PrimaryGreen, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = reward.discountValue,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = PrimaryGreen
                        )
                    }

                    Text(
                        text = reward.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AviationBlue,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Description block
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = reward.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )

                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Divider(color = BorderColor)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${reward.pointsRequired} Pts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = PrimaryGreen
                        )
                    }

                    Button(
                        onClick = onRedeemClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (canAfford) AviationBlue else BorderColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = canAfford,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (canAfford) "REDEEM" else "INSUFFICIENT BALANCE",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (canAfford) Color.White else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WalletVouchersSection(
    vouchersList: List<UserVoucher>,
    rewardsList: List<Reward>,
    onVoucherClick: (UserVoucher) -> Unit,
    modifier: Modifier = Modifier
) {
    if (vouchersList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(54.dp)
                )
                Text(
                    text = "Voucher Wallet Empty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Redeem your points under 'Browse Perks' to unlock digital lounge boarding vouchers and cafes rewards.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 280.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(vouchersList) { voucher ->
                val reward = rewardsList.firstOrNull { it.id == voucher.rewardId }
                VoucherRowItem(
                    voucher = voucher,
                    reward = reward,
                    onClick = { onVoucherClick(voucher) }
                )
            }
        }
    }
}

@Composable
fun VoucherRowItem(
    voucher: UserVoucher,
    reward: Reward?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateString = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(voucher.redeemedAt))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = reward?.title ?: "Eco Voucher Pass",
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
                            text = voucher.voucherCode,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "Claimed: $dateString",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(PrimaryGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "TAP PASS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }
        }
    }
}
