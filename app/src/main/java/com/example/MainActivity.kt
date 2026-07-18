package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.GreenJourneyViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    val viewModel: GreenJourneyViewModel = viewModel()
    val activeTab by viewModel.activeTab.collectAsState()
    val userStats by viewModel.userStats.collectAsState()
    val verifiedActions by viewModel.verifiedActions.collectAsState()
    val rewards by viewModel.rewards.collectAsState()
    val vouchers by viewModel.vouchers.collectAsState()
    val challenges by viewModel.challenges.collectAsState()
    val linkedAirlines by viewModel.linkedAirlines.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    // Collect Toast Messages from SharedFlow safely
    LaunchedEffect(key1 = true) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = AviationBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GREEN JOURNEY",
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                    }
                },
                actions = {
                    val unreadCount = notifications.count { !it.read }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { viewModel.setActiveTab("notifications") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification alert hub",
                            tint = if (unreadCount > 0) AviationBlue else TextSecondary,
                            modifier = Modifier.size(26.dp)
                        )
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unreadCount.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            CustomBottomBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.setActiveTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "home" -> HomeDashboard(
                    viewModel = viewModel,
                    userStats = userStats,
                    recentActions = verifiedActions
                )
                "earn" -> EarnPointsScreen(
                    viewModel = viewModel,
                    actions = verifiedActions
                )
                "rewards" -> RewardsMarketplaceScreen(
                    viewModel = viewModel,
                    userStats = userStats,
                    rewardsList = rewards,
                    vouchersList = vouchers
                )
                "challenges" -> ChallengesScreen(
                    viewModel = viewModel,
                    challengesList = challenges
                )
                "map" -> AirportsMapScreen()
                "profile" -> UserProfileScreen(
                    viewModel = viewModel,
                    userStats = userStats,
                    airlines = linkedAirlines
                )
                "notifications" -> NotificationsScreen(
                    viewModel = viewModel,
                    notificationsList = notifications
                )
            }
        }
    }
}

@Composable
fun CustomBottomBar(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem("home", "Home", Icons.Default.Home),
        NavigationItem("earn", "Scan", Icons.Default.QrCodeScanner),
        NavigationItem("rewards", "Perks", Icons.Default.CardGiftcard),
        NavigationItem("challenges", "Quests", Icons.Default.EmojiEvents),
        NavigationItem("map", "Airports", Icons.Default.Map),
        NavigationItem("profile", "ID", Icons.Default.Person)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark)
            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = activeTab == item.id
            val tintColor = if (isSelected) AviationBlue else TextSecondary

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onTabSelected(item.id) }
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .testTag("nav_tab_${item.id}"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = tintColor,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = item.label,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = tintColor
                )
            }
        }
    }
}

data class NavigationItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)
