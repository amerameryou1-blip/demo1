package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VerifiedAction
import com.example.ui.GreenJourneyViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarnPointsScreen(
    viewModel: GreenJourneyViewModel,
    actions: List<VerifiedAction>,
    modifier: Modifier = Modifier
) {
    var customCodeInput by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var showScanSuccessDialog by remember { mutableStateOf(false) }
    var simulatedScanDetails by remember { mutableStateOf("") }

    val categories = listOf("All", "Digital Boarding Pass", "Verified Carbon Offset", "Hydration Station", "Digital Receipt", "Airport Rail Transit")

    val filteredActions = remember(actions, selectedCategoryFilter) {
        if (selectedCategoryFilter == "All") {
            actions
        } else {
            actions.filter { it.category == selectedCategoryFilter }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("earn_points_scroll")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Scanning simulator header card
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Travel Pass & QR Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Scan digital boarding passes, hydration kiosks, and express transit QR codes to instantly verify your eco-impact.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    // Simulated Camera Scanning Window
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.5.dp, AviationBlue, RoundedCornerShape(12.dp))
                            .background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        // Corner borders to simulate camera crosshairs
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .border(2.dp, AviationBlue.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = AviationBlue,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ready to Scan",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AviationBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // QR presets selectors
                    Text(
                        text = "Simulate Kiosk Scans (TAP PRESET TO LOG):",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                simulatedScanDetails = "Digital Boarding Pass - Flight EK201 (DXB -> LHR)"
                                viewModel.simulateQrScan("GJ-BOARDING-DXB-EK201") {
                                    showScanSuccessDialog = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceLightDark),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Smartphone, contentDescription = null, tint = PrimaryGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Boarding Pass", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                simulatedScanDetails = "Reusable Bottle Refill at Gate B14"
                                viewModel.simulateQrScan("GJ-WATER-DXB") {
                                    showScanSuccessDialog = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceLightDark),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.LocalDrink, contentDescription = null, tint = PrimaryLight)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Hydration", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                simulatedScanDetails = "Airport Electric Transit Express"
                                viewModel.simulateQrScan("GJ-TRANSIT-SIN") {
                                    showScanSuccessDialog = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceLightDark),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.DirectionsTransit, contentDescription = null, tint = GoldTier)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Airport Rail", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Custom Code simulation input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customCodeInput,
                            onValueChange = { customCodeInput = it },
                            placeholder = { Text("Or paste QR payload manually", fontSize = 12.sp, color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = AviationBlue,
                                unfocusedBorderColor = BorderColor,
                                focusedContainerColor = CardBackground,
                                unfocusedContainerColor = CardBackground
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            onClick = {
                                if (customCodeInput.isNotBlank()) {
                                    simulatedScanDetails = "Manual payload simulation: $customCodeInput"
                                    viewModel.simulateQrScan(customCodeInput) {
                                        showScanSuccessDialog = true
                                        customCodeInput = ""
                                    }
                                }
                            },
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AviationBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("SIMULATE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section header and Filter categories
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Action History & Log Ledger",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val selected = selectedCategoryFilter == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selected) AviationBlue else SurfaceDark)
                                .border(1.dp, if (selected) AviationBlue else BorderColor, RoundedCornerShape(20.dp))
                                .clickable { selectedCategoryFilter = category }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = if (selected) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Action logs list items
        if (filteredActions.isEmpty()) {
            item {
                EmptyStateCard(message = "No matching verified actions found. Select 'All' or trigger a simulation above to log actions!")
            }
        } else {
            items(filteredActions) { action ->
                ActionRowItem(action = action)
            }
        }
    }

    if (showScanSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showScanSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eco Action Logged!", fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Green Journey has verified and authorized your carbon credit action.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceLightDark)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = simulatedScanDetails,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showScanSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AviationBlue)
                ) {
                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
