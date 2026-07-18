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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LinkedAirline
import com.example.data.model.UserStats
import com.example.ui.GreenJourneyViewModel
import com.example.ui.theme.*

@Composable
fun UserProfileScreen(
    viewModel: GreenJourneyViewModel,
    userStats: UserStats?,
    airlines: List<LinkedAirline>,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showLinkAirlineDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("user_profile_scroll")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Digital boarding flyer ticket card
        item {
            userStats?.let { stats ->
                DigitalIDCard(stats = stats, onEditClick = { showEditProfileDialog = true })
            }
        }

        // Section header for linked accounts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Linked Airlines Accounts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Button(
                    onClick = { showLinkAirlineDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AviationBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("LINK NEW", color = AviationBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // List airlines
        if (airlines.isEmpty()) {
            item {
                EmptyStateCard(message = "No airline accounts synced yet. Link your skywards accounts to synchronise travel itineraries and claim +200 bonus PTS per link!")
            }
        } else {
            items(airlines) { airline ->
                LinkedAirlineItem(
                    airline = airline,
                    onUnlinkClick = { viewModel.unlinkAirline(airline.id) }
                )
            }
        }

        // App options or help card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Aviation Environmental Disclosures",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Green Journey is partnered with verified SAF (Sustainable Aviation Fuel) biofuel refineries and Gold Standard forestry carbon brokers to issue and audit loyalty credits.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }

    if (showEditProfileDialog && userStats != null) {
        var nameInput by remember { mutableStateOf(userStats.name) }
        var emailInput by remember { mutableStateOf(userStats.email) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text("Edit Account Details", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Display Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AviationBlue,
                            unfocusedBorderColor = BorderColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email Address") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AviationBlue,
                            unfocusedBorderColor = BorderColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(nameInput, emailInput)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AviationBlue)
                ) {
                    Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showLinkAirlineDialog) {
        var selectedAirlineName by remember { mutableStateOf("Emirates Skywards") }
        var selectedAirlineCode by remember { mutableStateOf("EK") }
        var ffNoInput by remember { mutableStateOf("") }

        val airlineOptions = listOf(
            Pair("Emirates Skywards", "EK"),
            Pair("Qatar Privilege Club", "QR"),
            Pair("Singapore Airlines KrisFlyer", "SQ"),
            Pair("United MileagePlus", "UA"),
            Pair("Lufthansa Miles & More", "LH")
        )

        AlertDialog(
            onDismissRequest = { showLinkAirlineDialog = false },
            title = {
                Text("Link Airline Loyalty Account", fontWeight = FontWeight.Bold, color = TextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Unlocks automatic flight itinerary synchronization and grants +200 Green Points bonus on successful synchronization.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    Column {
                        Text("SELECT PARTNER AIRLINE:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                                .background(CardBackground)
                        ) {
                            airlineOptions.forEach { (name, code) ->
                                val active = selectedAirlineName == name
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedAirlineName = name
                                            selectedAirlineCode = code
                                        }
                                        .background(if (active) PrimaryGreen.copy(alpha = 0.1f) else Color.Transparent)
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(name, color = if (active) PrimaryGreen else TextPrimary, fontSize = 13.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal)
                                    if (active) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = ffNoInput,
                        onValueChange = { ffNoInput = it },
                        placeholder = { Text("Frequent Flyer Number (e.g. FF-9008)", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AviationBlue,
                            unfocusedBorderColor = BorderColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (ffNoInput.isNotBlank()) {
                            viewModel.linkAirline(selectedAirlineName, selectedAirlineCode, ffNoInput)
                            showLinkAirlineDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AviationBlue),
                    enabled = ffNoInput.isNotBlank()
                ) {
                    Text("LINK & CLAIM BONUS", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLinkAirlineDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun DigitalIDCard(
    stats: UserStats,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = stats.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = stats.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceLightDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = AviationBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Divider(color = BorderColor)

            // Barcode Rendering Loop
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Repeating visual barcode lines of varying widths
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val barcodePatterns = listOf(2, 4, 1, 3, 2, 1, 4, 2, 3, 1, 2, 4, 1, 3, 2, 1, 2, 4, 1, 3, 2)
                    barcodePatterns.forEach { widthWeight ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(widthWeight.dp)
                                .background(Color.Black)
                        )
                        Spacer(modifier = Modifier.width((if (widthWeight % 2 == 0) 2 else 3).dp))
                    }
                }

                Text(
                    text = "* ${stats.frequentFlyerId} *",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = 4.sp
                )
            }

            // Loyalty tier indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MEMBERSHIP LEVEL", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(stats.tier.uppercase(), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("ECO INDEX LEVEL", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text("${stats.co2SavedKg.toInt()} KG CO₂", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AviationBlue)
                }
            }
        }
    }
}

@Composable
fun LinkedAirlineItem(
    airline: LinkedAirline,
    onUnlinkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = airline.airlineCode,
                        fontWeight = FontWeight.Black,
                        color = PrimaryGreen,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = airline.airlineName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = airline.accountNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text("•", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(
                            text = "Synced +${airline.pointsSynced} Pts",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                }
            }

            IconButton(
                onClick = onUnlinkClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Unlink Account",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}
