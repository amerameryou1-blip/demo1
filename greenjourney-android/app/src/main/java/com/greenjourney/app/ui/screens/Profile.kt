package com.greenjourney.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenjourney.app.data.AppState
import com.greenjourney.app.ui.theme.*

@Composable
fun ProfileScreen(state: AppState) {
    var editing by remember { mutableStateOf(false) }
    var linking by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xF0162138), Color(0xF00A1022))))
                    .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(24.dp))
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0B1425))
                                .border(2.dp, Brush.linearGradient(listOf(Mint, Cyan, Sky)), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                state.stats.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                                color = Color(0xFFA5F3FC), fontSize = 18.sp, fontWeight = FontWeight.Black,
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(state.stats.name, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black, maxLines = 1)
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(16.dp))
                            }
                            Text(state.stats.email, color = TextSecondary, fontSize = 12.sp, maxLines = 1)
                            Spacer(Modifier.height(5.dp))
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0x1FFBBF24))
                                    .border(1.dp, Color(0x4DFBBF24), RoundedCornerShape(50))
                                    .padding(horizontal = 9.dp, vertical = 3.dp)
                            ) {
                                Text(state.stats.tier.uppercase() + " TIER", color = Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            }
                        }
                        Box(
                            Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(10.dp))
                                .clickable { editing = true },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.Edit, null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoTile(Icons.Default.Shield, "FREQUENT FLYER ID", state.stats.frequentFlyerId, Modifier.weight(1f))
                        InfoTile(Icons.Default.Place, "HOME AIRPORT", state.stats.homeAirport.take(3), Modifier.weight(1f))
                    }
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ImpactTile(Icons.Default.Air, "%.1f".format(state.stats.co2SavedKg), "kg CO\u2082 saved", Color(0xFF67E8F9), Modifier.weight(1f))
                ImpactTile(Icons.Default.LocalDrink, state.stats.waterSavedLiters.toString(), "L water saved", Sky, Modifier.weight(1f))
                ImpactTile(Icons.Default.Recycling, state.stats.plasticAvoidedCount.toString(), "Items avoided", Mint, Modifier.weight(1f))
            }
        }

        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 18.dp) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FlightTakeoff, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Linked Airline Programs", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x0AFFFFFF))
                                .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { linking = true }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text("+ Link", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    state.airlines.forEach { a ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x08FFFFFF))
                                .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    Modifier
                                        .size(width = 44.dp, height = 36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Brush.linearGradient(listOf(Color(0x2622D3EE), Color(0x1A34D399))))
                                        .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(a.airlineCode, color = Color(0xFFA5F3FC), fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(a.airlineName, color = TextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        "${a.accountNumber} \u2022 ${"%,d".format(a.pointsSynced)} pts synced",
                                        color = TextMuted, fontSize = 10.5.sp,
                                    )
                                }
                            }
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { state.unlinkAirline(a.id) }
                                    .padding(6.dp)
                            ) {
                                Icon(Icons.Default.DeleteOutline, null, tint = TextMuted, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }

        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 18.dp) {
                MenuRow(Icons.Default.EmojiEvents, "Achievements & Badges", "12 earned", null)
                MenuRow(Icons.Default.Settings, "Preferences", "Notifications, units", null)
                MenuRow(Icons.Default.Email, "Contact Support", "24/7 concierge", null)
                MenuRow(Icons.Default.ExitToApp, "Sign Out", "", Rose, isLast = true)
            }
        }
    }

    if (editing) {
        EditProfileDialog(
            name = state.stats.name,
            email = state.stats.email,
            onDismiss = { editing = false },
            onSave = { n, e -> state.updateProfile(n, e); editing = false },
        )
    }
    if (linking) {
        LinkAirlineDialog(
            onDismiss = { linking = false },
            onLink = { n, c, a -> state.linkAirline(n, c, a); linking = false },
        )
    }
}

@Composable
private fun MenuRow(icon: ImageVector, label: String, hint: String, danger: Color?, isLast: Boolean = false) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (danger != null) Color(0x14FB7185) else Color(0x0AFFFFFF))
                        .border(1.dp, if (danger != null) Color(0x33FB7185) else Color(0x14FFFFFF), RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, null, tint = danger ?: TextSecondary, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(label, color = danger ?: TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(hint, color = TextMuted, fontSize = 11.sp)
        }
        if (!isLast) Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x0DFFFFFF)))
    }
}

@Composable
private fun InfoTile(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x08FFFFFF))
            .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = TextMuted, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, color = TextMuted, fontSize = 9.5.sp, letterSpacing = 0.8.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun ImpactTile(icon: ImageVector, value: String, label: String, tint: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color(0xE312182D), Color(0xDF080D1C))))
            .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp),
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(value, color = tint, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(label, color = TextSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun FieldStyle() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = Color(0x0AFFFFFF),
    unfocusedContainerColor = Color(0x08FFFFFF),
    focusedBorderColor = Cyan.copy(alpha = 0.5f),
    unfocusedBorderColor = Color(0x1AFFFFFF),
    focusedLabelColor = TextSecondary,
    unfocusedLabelColor = TextSecondary,
    cursorColor = Cyan,
)

@Composable
private fun EditProfileDialog(name: String, email: String, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var n by remember { mutableStateOf(name) }
    var e by remember { mutableStateOf(email) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0D1529),
        shape = RoundedCornerShape(24.dp),
        title = { Text("Edit Profile", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Full Name") }, singleLine = true, colors = FieldStyle(), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = e, onValueChange = { e = it }, label = { Text("Email Address") }, singleLine = true, colors = FieldStyle(), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
                PrimaryMintButton(onClick = { onSave(n, e) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Save Changes", color = MintDeep, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
    )
}

@Composable
private fun LinkAirlineDialog(onDismiss: () -> Unit, onLink: (String, String, String) -> Unit) {
    var n by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0D1529),
        shape = RoundedCornerShape(24.dp),
        title = { Text("Link New Airline", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("Airline Name") }, singleLine = true, colors = FieldStyle(), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = c, onValueChange = { c = it }, label = { Text("Airline Code") }, singleLine = true, colors = FieldStyle(), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("Account Number") }, singleLine = true, colors = FieldStyle(), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth())
                PrimaryMintButton(onClick = { onLink(n, c, a) }, enabled = n.isNotBlank() && c.isNotBlank() && a.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
                    Text("Link Program", color = MintDeep, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
    )
}
