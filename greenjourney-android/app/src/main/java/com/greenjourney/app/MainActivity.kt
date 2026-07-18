package com.greenjourney.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenjourney.app.data.AppState
import com.greenjourney.app.ui.screens.ChallengesScreen
import com.greenjourney.app.ui.screens.EarnScreen
import com.greenjourney.app.ui.screens.HomeScreen
import com.greenjourney.app.ui.screens.MapScreen
import com.greenjourney.app.ui.screens.NotificationsScreen
import com.greenjourney.app.ui.screens.ProfileScreen
import com.greenjourney.app.ui.screens.RewardsScreen
import com.greenjourney.app.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenJourneyTheme {
                val state = remember { AppState() }
                MainApp(state)
            }
        }
    }
}

private data class NavItem(val id: String, val label: String, val icon: ImageVector)

private val NAV = listOf(
    NavItem("home", "Home", Icons.Default.Home),
    NavItem("earn", "Scan", Icons.Default.QrCodeScanner),
    NavItem("rewards", "Perks", Icons.Default.CardGiftcard),
    NavItem("challenges", "Quests", Icons.Default.EmojiEvents),
    NavItem("map", "Airports", Icons.Default.Map),
    NavItem("profile", "ID", Icons.Default.Person),
)

@Composable
fun MainApp(state: AppState) {
    val context = LocalContext.current

    LaunchedEffect(state.toast) {
        state.toast?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            state.toast = null
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgDeep, BgBottom)))
    ) {
        Box(
            Modifier
                .size(340.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-140).dp)
                .background(
                    Brush.radialGradient(listOf(Color(0x2422D3EE), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            Modifier
                .size(300.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp, y = 40.dp)
                .background(
                    Brush.radialGradient(listOf(Color(0x1F10B981), Color.Transparent)),
                    CircleShape
                )
        )

        Column(Modifier.fillMaxSize().systemBarsPadding()) {
            TopBar(state)
            Box(Modifier.weight(1f)) {
                when (state.activeTab) {
                    "home" -> HomeScreen(state)
                    "earn" -> EarnScreen(state)
                    "rewards" -> RewardsScreen(state)
                    "challenges" -> ChallengesScreen(state)
                    "map" -> MapScreen(state)
                    "profile" -> ProfileScreen(state)
                    "notifications" -> NotificationsScreen(state)
                }
            }
            BottomNav(state)
        }
    }
}

@Composable
private fun TopBar(state: AppState) {
    val unread = state.notifications.count { !it.read }
    Surface(
        color = Color(0xB30B1425),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x17FFFFFF)),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(Color(0x4034D399), Color(0x3322D3EE))))
                    .border(1.dp, Color(0x4D6EE7B7), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Public, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    "GREEN JOURNEY",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 2.8.sp,
                )
                Text(
                    "SUSTAINABLE AVIATION",
                    color = Mint.copy(alpha = 0.85f),
                    fontSize = 8.5.sp,
                    letterSpacing = 2.4.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.weight(1f))
            Box {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x0BFFFFFF))
                        .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { state.activeTab = "notifications" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notification alert hub",
                        tint = if (unread > 0) Color(0xFF67E8F9) else TextSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                if (unread > 0) {
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(RoseHot),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(unread.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNav(state: AppState) {
    Surface(
        color = Color(0xCC0B1425),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x14FFFFFF)),
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 6.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NAV.forEach { item ->
                val active = state.activeTab == item.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { state.activeTab = item.id }
                        .padding(vertical = 5.dp)
                ) {
                    Box(
                        Modifier
                            .size(width = 46.dp, height = 30.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .then(
                                if (active)
                                    Modifier
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0x4034D399), Color(0x3322D3EE))
                                            )
                                        )
                                        .border(1.dp, Color(0x6622D3EE), RoundedCornerShape(11.dp))
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = if (active) Color(0xFF67E8F9) else TextSecondary,
                            modifier = Modifier.size(19.dp),
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        item.label,
                        fontSize = 9.5.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (active) TextPrimary else TextSecondary,
                    )
                    Spacer(Modifier.height(2.dp))
                    Box(
                        Modifier
                            .size(width = 22.dp, height = 2.5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (active) Brush.horizontalGradient(listOf(Mint, Cyan))
                                else Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                            )
                    )
                }
            }
        }
    }
}
