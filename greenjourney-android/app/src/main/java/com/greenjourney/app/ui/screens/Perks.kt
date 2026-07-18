package com.greenjourney.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.greenjourney.app.data.AppState
import com.greenjourney.app.data.Reward
import com.greenjourney.app.ui.theme.*

@Composable
fun RewardsScreen(state: AppState) {
    var tab by remember { mutableStateOf("catalog") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 24.dp) {
                Row(
                    Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("PERKS MARKETPLACE", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp)
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("%,d".format(state.stats.greenPoints), color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Black)
                            Spacer(Modifier.width(6.dp))
                            Text("PTS available", color = Mint, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("Trade your eco impact for exclusive travel perks.", color = TextSecondary, fontSize = 11.5.sp)
                    }
                    Box(
                        Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0x40FBBF24), Color(0x1AEAB308))))
                            .border(1.dp, Color(0x66FBBF24), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.CardGiftcard, null, tint = Gold, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0AFFFFFF))
                    .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(16.dp))
                    .padding(4.dp),
            ) {
                TabItem("Catalog", tab == "catalog", Modifier.weight(1f)) { tab = "catalog" }
                TabItem(
                    "My Wallet" + if (state.vouchers.isNotEmpty()) " (${state.vouchers.size})" else "",
                    tab == "wallet",
                    Modifier.weight(1f),
                ) { tab = "wallet" }
            }
        }

        if (tab == "catalog") {
            items(state.rewards, key = { it.id }) { r ->
                RewardCard(r, canAfford = state.stats.greenPoints >= r.cost) { state.redeemReward(r.id) }
            }
        } else if (state.vouchers.isEmpty()) {
            item {
                GlassCard(Modifier.fillMaxWidth(), corner = 20.dp) {
                    Column(
                        Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = TextMuted, modifier = Modifier.size(30.dp))
                        Text(
                            "Your voucher wallet is empty. Redeem a reward from the catalog to unlock perks.",
                            color = TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        } else {
            items(state.vouchers, key = { it.id }) { v ->
                val clipboard = LocalClipboardManager.current
                GlassCard(Modifier.fillMaxWidth(), corner = 16.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("ACTIVE VOUCHER", color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(v.title, color = TextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                                Spacer(Modifier.height(4.dp))
                                Text(v.code, color = TextSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            }
                            GhostButton(onClick = { clipboard.setText(AnnotatedString(v.code)) }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ContentCopy, null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text("Copy", color = TextPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = Mint, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Ready to use at partner terminals", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabItem(label: String, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (active)
                    Modifier
                        .background(Brush.horizontalGradient(listOf(Color(0x4034D399), Color(0x4022D3EE))))
                        .border(1.dp, Color(0x4D6EE7B7), RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = if (active) TextPrimary else TextSecondary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RewardCard(r: Reward, canAfford: Boolean, onRedeem: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth(), corner = 18.dp) {
        Box(Modifier.fillMaxWidth().height(128.dp)) {
            AsyncImage(
                model = r.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x99020603), Color(0xE612182D)),
                        )
                    )
            )
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xB30B1425))
                    .border(1.dp, Mint.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Mint, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(r.category, color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(Gold, Color(0xFFEAB308))))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(r.badge, color = Color(0xFF451A03), fontSize = 10.5.sp, fontWeight = FontWeight.Black)
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text(r.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(r.description, color = TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("%,d".format(r.cost), color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.width(4.dp))
                    Text("PTS", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 2.dp))
                }
                if (canAfford) {
                    PrimaryMintButton(onClick = onRedeem) {
                        Text("Redeem Now", color = MintDeep, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x0DFFFFFF))
                            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(14.dp))
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text("Not Enough", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
