package com.greenjourney.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenjourney.app.ui.theme.Cyan
import com.greenjourney.app.ui.theme.Emerald
import com.greenjourney.app.ui.theme.Mint
import com.greenjourney.app.ui.theme.Sky

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 20.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(corner))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xE312182D), Color(0xDF080D1C))
                )
            )
            .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(corner)),
        content = content,
    )
}

@Composable
fun GradientProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 7.dp,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(Color(0x1AFFFFFF))
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(CircleShape)
                .background(Brush.horizontalGradient(listOf(Mint, Cyan, Sky)))
        )
    }
}

@Composable
fun PrimaryMintButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .then(
                if (enabled) Modifier.background(Brush.linearGradient(listOf(Mint, Emerald)))
                else Modifier.background(Color(0x0DFFFFFF))
            )
            .border(
                1.dp,
                if (enabled) Color.Transparent else Color(0x14FFFFFF),
                RoundedCornerShape(14.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) { content() }
}

@Composable
fun GhostButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x0AFFFFFF))
            .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) { content() }
}
