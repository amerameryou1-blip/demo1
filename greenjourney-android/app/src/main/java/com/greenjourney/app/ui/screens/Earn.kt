package com.greenjourney.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenjourney.app.data.AppState
import com.greenjourney.app.ui.theme.*
import kotlinx.coroutines.delay

private data class QuickOpt(
    val label: String,
    val icon: ImageVector,
    val pts: Int,
    val co2: Float,
    val tint: Color,
    val category: String,
    val title: String,
)

private val QUICK = listOf(
    QuickOpt("Digital Boarding Pass", Icons.Default.Smartphone, 150, 25f, Mint, "Digital Boarding Pass", "Digital Boarding Pass - Flight EK201"),
    QuickOpt("Verified SAF Offset", Icons.Default.Air, 350, 85f, Color(0xFF67E8F9), "Verified Carbon Offset", "Verified SAF Biofuel Purchase"),
    QuickOpt("Hydration Station", Icons.Default.LocalDrink, 30, 0.8f, Sky, "Hydration Station", "Reusable Bottle Hydration Refill"),
    QuickOpt("Digital Receipt", Icons.Default.ReceiptLong, 40, 1.2f, Mint, "Digital Receipt", "Paperless Duty Free Digital Receipt"),
    QuickOpt("Airport Rail Transit", Icons.Default.Train, 90, 12f, Gold, "Airport Rail Transit", "Airport Electric Express Train"),
)

@Composable
fun EarnScreen(state: AppState) {
    var scanning by remember { mutableStateOf(false) }

    LaunchedEffect(scanning) {
        if (scanning) {
            delay(1500)
            state.verifyEcoAction("Digital Boarding Pass", "DXB", "Digital Boarding Pass - Flight EK201", 150, 25.0f)
            scanning = false
        }
    }

    val infinite = rememberInfiniteTransition(label = "scan")
    val sweep by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "sweep",
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column {
                Text("SCAN & EARN", color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp)
                Spacer(Modifier.height(4.dp))
                Text("Verify Your Eco Actions", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Scan boarding pass QR codes, station tags, or receipts to earn Green Points instantly.",
                    color = TextSecondary, fontSize = 12.5.sp,
                )
            }
        }

        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 24.dp) {
                Column(
                    Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(Modifier.size(210.dp)) {
                        Box(
                            Modifier
                                .matchParentSize()
                                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(24.dp))
                        )
                        Bracket(Alignment.TopStart); Bracket(Alignment.TopEnd)
                        Bracket(Alignment.BottomStart); Bracket(Alignment.BottomEnd)
                        Box(
                            Modifier
                                .align(Alignment.Center)
                                .size(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.linearGradient(listOf(Color(0x1A22D3EE), Color(0x1A34D399)))),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.QrCodeScanner,
                                null,
                                tint = Color(0xCC67E8F9),
                                modifier = Modifier.size(80.dp),
                            )
                        }
                        if (scanning) {
                            Box(
                                Modifier
                                    .align(Alignment.Center)
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .offset(y = (150.dp * sweep))
                                        .background(Color(0xFF67E8F9))
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    PrimaryMintButton(onClick = { scanning = true }, enabled = !scanning) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.QrCodeScanner, null, tint = MintDeep, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                if (scanning) "Scanning..." else "Start QR Scan",
                                color = MintDeep, fontWeight = FontWeight.Bold, fontSize = 13.sp,
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Camera permission required on device", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        item {
            Column {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Quick Verify (Demo QR Codes)", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.PhotoCamera, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.height(10.dp))
                QUICK.chunked(2).forEach { rowItems ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        rowItems.forEach { opt ->
                            Box(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(listOf(opt.tint.copy(alpha = 0.16f), opt.tint.copy(alpha = 0.04f))))
                                    .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(16.dp))
                                    .clickable {
                                        state.verifyEcoAction(opt.category, "DXB", opt.title, opt.pts, opt.co2)
                                    }
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x990B1425))
                                            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(opt.icon, null, tint = opt.tint, modifier = Modifier.size(18.dp))
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text(opt.label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(
                                            "+${opt.pts} PTS \u2022 ${opt.co2}kg CO\u2082",
                                            color = opt.tint, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                }
                            }
                        }
                        if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Verification Ledger", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("${state.actions.size} ENTRIES", color = TextMuted, fontSize = 10.sp, letterSpacing = 0.8.sp)
            }
        }

        items(state.actions, key = { it.id }) { a ->
            GlassCard(Modifier.fillMaxWidth(), corner = 14.dp) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Mint.copy(alpha = 0.12f))
                                .border(1.dp, Mint.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.QrCodeScanner, null, tint = Mint, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(a.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(a.airportCode, color = Color(0xFF67E8F9), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                Text("  \u2022  ", color = TextMuted, fontSize = 10.5.sp)
                                Text(a.referenceNo, color = TextMuted, fontSize = 10.5.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("+${a.pointsEarned}", color = Mint, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        Text("-${"%.1f".format(a.co2OffsetKg)}kg", color = Color(0xFF67E8F9), fontSize = 10.5.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.Bracket(align: Alignment) {
    val corner = align
    Box(
        Modifier
            .align(align)
            .size(28.dp, 28.dp)
    ) {
        Box(
            Modifier
                .align(corner)
                .width(28.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xCC67E8F9))
        )
        Box(
            Modifier
                .align(corner)
                .width(3.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xCC67E8F9))
        )
    }
}
