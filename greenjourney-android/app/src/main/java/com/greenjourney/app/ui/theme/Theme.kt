package com.greenjourney.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val BgDeep      = Color(0xFF030612)
val BgTop       = Color(0xFF050A18)
val BgBottom    = Color(0xFF02040B)
val BorderColor = Color(0x12FFFFFF)

val TextPrimary   = Color(0xFFE5EDFF)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted     = Color(0xFF64748B)

val Mint    = Color(0xFF34D399)
val Emerald = Color(0xFF10B981)
val Cyan    = Color(0xFF22D3EE)
val Sky     = Color(0xFF38BDF8)
val Gold    = Color(0xFFFBBF24)
val Amber   = Color(0xFFF59E0B)
val TealC   = Color(0xFF14B8A6)
val Rose    = Color(0xFFFB7185)
val RoseHot = Color(0xFFF43F5E)
val MintDeep  = Color(0xFF052E1C)

val CardShape = RoundedCornerShape(20.dp)
val SmallShape = RoundedCornerShape(14.dp)

private val scheme = darkColorScheme(
    primary = Mint,
    onPrimary = MintDeep,
    secondary = Cyan,
    onSecondary = BgDeep,
    background = BgDeep,
    onBackground = TextPrimary,
    surface = Color(0xE112182D),
    onSurface = TextPrimary,
    error = RoseHot,
    onError = Color.White,
)

@Composable
fun GreenJourneyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = scheme,
        content = content,
    )
}
