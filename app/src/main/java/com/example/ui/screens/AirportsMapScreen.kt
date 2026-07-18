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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class GreenAirport(
    val code: String,
    val name: String,
    val location: String,
    val rating: String,
    val features: List<String>
)

@Composable
fun AirportsMapScreen(
    modifier: Modifier = Modifier
) {
    var selectedAirportCode by remember { mutableStateOf("DXB") }

    val airports = listOf(
        GreenAirport(
            "DXB",
            "Dubai International Airport",
            "Dubai, UAE (Terminal 3)",
            "LEED Gold Hub",
            listOf(
                "High-speed electric rail transit connection to city centre",
                "15,000 panel rooftop solar grid powering concourse",
                "Complete ban on single-use plastic cups, straws, and cutlery",
                "Advanced greywater recycling for airfield irrigation"
            )
        ),
        GreenAirport(
            "LHR",
            "London Heathrow Airport",
            "London, United Kingdom (Terminal 5)",
            "Carbon Neutral Accredited",
            listOf(
                "Direct Sustainable Aviation Fuel (SAF) supply pipelines",
                "100% renewable grid sourcing for terminal lighting",
                "Mandatory organic composting for all airside diners",
                "Zero-emission electric ground support fleet and tugs"
            )
        ),
        GreenAirport(
            "SIN",
            "Singapore Changi Airport",
            "Singapore (Jewel & T4)",
            "Green Mark Platinum",
            listOf(
                "Indoor climate-controlled Jewel forest dome for natural cooling",
                "Smart optical sensors to minimize air conditioning loss",
                "Kinetic power pads harvesting footstep energy",
                "Rainwater harvesting feeding massive indoor waterfalls"
            )
        ),
        GreenAirport(
            "SFO",
            "San Francisco International",
            "California, USA (Harvey Milk T1)",
            "LEED Platinum Certified",
            listOf(
                "First zero-landfill terminal design worldwide",
                "95% construction waste diverted from local landfills",
                "Massive electric vehicle rental lounge and chargers",
                "Strict local and organic catering ingredient sourcing standards"
            )
        )
    )

    val airlines = listOf(
        Pair("Emirates Skywards", "Pioneering biofuel testing on modern widebody fleets."),
        Pair("Qatar Privilege Club", "Offsetting 100% regional routes with certified forestry investments."),
        Pair("Singapore Airlines", "Committed to Net-Zero SAF fueling targets by 2050."),
        Pair("United MileagePlus", "Direct investor in domestic SAF refineries and electric air-taxis.")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("airports_map_scroll")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Map header description
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Participating Green Airports & Airlines",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Explore hubs offering verified Eco-action stations (hydration refilling kiosks, paperless gates, solar charging, or electric transit connections) to log points.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Airport Selector Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Certified Airport Hubs Directory",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    airports.forEach { airport ->
                        val selected = selectedAirportCode == airport.code
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) AviationBlue else SurfaceDark)
                                .border(1.dp, if (selected) AviationBlue else BorderColor, RoundedCornerShape(12.dp))
                                .clickable { selectedAirportCode = airport.code }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = airport.code,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (selected) Color.White else TextPrimary
                                )
                                Text(
                                    text = airport.rating.split(" ").firstOrNull() ?: "",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color(0xFF022C22).copy(alpha = 0.8f) else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selected Airport Detail Card
        item {
            val selectedAirport = airports.first { it.code == selectedAirportCode }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = selectedAirport.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = selectedAirport.location,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AviationBlue.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = selectedAirport.rating,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AviationBlue
                            )
                        }
                    }

                    Divider(color = BorderColor)

                    Text(
                        text = "Green Features & Stations:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AviationBlue
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        selectedAirport.features.forEach { feature ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = feature,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Green Alliance Partner Airlines
        item {
            Text(
                text = "Green Loyalty Alliance Partners",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(airlines) { airline ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AviationBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = AviationBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = airline.first,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = airline.second,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
