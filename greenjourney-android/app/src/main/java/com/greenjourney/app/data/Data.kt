package com.greenjourney.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserStats(
    val name: String,
    val email: String,
    val greenPoints: Int,
    val co2SavedKg: Float,
    val waterSavedLiters: Int,
    val plasticAvoidedCount: Int,
    val homeAirport: String,
    val frequentFlyerId: String,
    val tier: String,
)

data class VerifiedAction(
    val id: Long,
    val title: String,
    val category: String,
    val pointsEarned: Int,
    val co2OffsetKg: Float,
    val airportCode: String,
    val referenceNo: String,
    val timestamp: Long,
)

data class Reward(
    val id: Int,
    val title: String,
    val category: String,
    val cost: Int,
    val description: String,
    val badge: String,
    val image: String,
)

data class Voucher(val id: Long, val title: String, val code: String, val redeemedAt: Long)

data class Challenge(
    val id: Int,
    val title: String,
    val description: String,
    val bonusPoints: Int,
    val category: String,
    val iconKey: String,
    val targetCount: Int,
    val currentProgress: Int,
    val completed: Boolean,
    val claimed: Boolean,
)

data class LinkedAirline(
    val id: Long,
    val airlineName: String,
    val airlineCode: String,
    val accountNumber: String,
    val pointsSynced: Int,
)

data class AppNotification(
    val id: Long,
    val title: String,
    val message: String,
    val type: String,
    val read: Boolean,
    val timestamp: Long,
)

data class Airport(
    val code: String,
    val name: String,
    val city: String,
    val lat: Double,
    val lng: Double,
    val score: Int,
)

val airports = listOf(
    Airport("DXB", "Dubai International", "Dubai", 25.25, 55.36, 92),
    Airport("SIN", "Singapore Changi", "Singapore", 1.35, 103.99, 96),
    Airport("LHR", "London Heathrow", "London", 51.47, -0.45, 88),
    Airport("CDG", "Paris Charles de Gaulle", "Paris", 49.0, 2.55, 85),
    Airport("HND", "Tokyo Haneda", "Tokyo", 35.55, 139.78, 90),
    Airport("JFK", "New York JFK", "New York", 40.64, -73.78, 79),
    Airport("SFO", "San Francisco Intl", "San Francisco", 37.62, -122.38, 87),
    Airport("AMS", "Amsterdam Schiphol", "Amsterdam", 52.31, 4.76, 91),
    Airport("SYD", "Sydney Kingsford Smith", "Sydney", -33.94, 151.18, 84),
)

class AppState {

    var activeTab by mutableStateOf("home")
    var stats by mutableStateOf(
        UserStats(
            name = "Captain Sarah Jenkins",
            email = "sarah.jenkins@greenjourney.com",
            greenPoints = 2850,
            co2SavedKg = 142.5f,
            waterSavedLiters = 320,
            plasticAvoidedCount = 48,
            homeAirport = "DXB - Dubai International",
            frequentFlyerId = "GJ-9824106",
            tier = "Silver",
        )
    )

    val actions = mutableStateListOf<VerifiedAction>()
    val rewards = listOf(
        Reward(1, "20% off Gourmet Airport Organic Caf\u00e9", "Caf\u00e9", 300,
            "Enjoy sustainable, locally sourced organic dining at participating terminal lounges and cafes.",
            "20% OFF", "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&q=80&w=800"),
        Reward(2, "High-Speed In-Flight Wi-Fi Flight Pass", "In-flight Wi-Fi", 800,
            "Unlimited streaming and messaging connection on all short and long-haul partner flights.",
            "FREE PASS", "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&q=80&w=800"),
        Reward(3, "Extra Legroom Preferred Seat Upgrade", "Seat Selection", 1200,
            "Reserve front row or exit row extra legroom seats with eco-leather ergonomic comfort.",
            "FREE UPGRADE", "https://images.unsplash.com/photo-1569154941061-e231b4725ef1?auto=format&fit=crop&q=80&w=800"),
        Reward(4, "15% Eco-Friendly Duty Free Voucher", "Duty Free", 500,
            "Save on sustainable cosmetics, recycled travel items, and certified zero-emission perfume lines.",
            "15% OFF", "https://images.unsplash.com/photo-1513151233558-d860c5398176?auto=format&fit=crop&q=80&w=800"),
        Reward(5, "VIP First Class Green Lounge Access Pass", "Lounge discounts", 2200,
            "Access tranquil, zero-single-use-plastic flagship VIP airport lounges worldwide with complimentary organic buffet.",
            "VIP PASS", "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=80&w=800"),
        Reward(6, "Recycled Ocean Plastics Travel Toiletry Kit", "Eco-friendly products", 1500,
            "Premium flight accessory set constructed from upcycled ocean polymer with biodegradable amenities.",
            "100% REDEEM", "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&q=80&w=800"),
        Reward(7, "Direct SAF Aviation Fuel Carbon Offset Token", "Carbon Offset", 1000,
            "Directly sponsor 100kg of Sustainable Aviation Fuel (SAF) into partner airport fueling systems.",
            "100kg SAF", "https://images.unsplash.com/photo-1464037866556-6812c9d1c72e?auto=format&fit=crop&q=80&w=800"),
    )
    val vouchers = mutableStateListOf<Voucher>()
    val challenges = mutableStateListOf<Challenge>()
    val airlines = mutableStateListOf<LinkedAirline>()
    val notifications = mutableStateListOf<AppNotification>()

    var toast by mutableStateOf<String?>(null)

    private var idCounter = 1000L
    private fun nextId() = ++idCounter

    init { seed() }

    private fun seed() {
        val now = System.currentTimeMillis()
        actions.addAll(
            listOf(
                VerifiedAction(nextId(), "Digital Boarding Pass - DXB to LHR", "Digital Boarding Pass", 150, 25.0f, "DXB", "GJ-BP-8819", now - 4 * 60 * 60 * 1000),
                VerifiedAction(nextId(), "Verified SAF Carbon Offset Purchase", "Verified Carbon Offset", 350, 85.0f, "DXB", "GJ-CO2-9901", now - 24 * 60 * 60 * 1000),
                VerifiedAction(nextId(), "Refilled Reusable Bottle at Gate B14", "Hydration Station", 25, 0.5f, "DXB", "GJ-HYD-1022", now - 32 * 60 * 60 * 1000),
                VerifiedAction(nextId(), "Paperless Duty Free Digital Receipt", "Digital Receipt", 40, 1.2f, "DXB", "GJ-REC-4410", now - 48 * 60 * 60 * 1000),
            )
        )
        challenges.addAll(
            listOf(
                Challenge(1, "Plastic-Free Traveler",
                    "Skip single-use cups and plastic bags on 3 consecutive flights using reusable bottles and digital passes.",
                    300, "Waste Elimination", "Droplets", 3, 2, completed = false, claimed = false),
                Challenge(2, "100% Digital Traveler",
                    "Use zero printed paper passes, tags, or invoices across 5 journeys.",
                    500, "Paperless Flight", "Smartphone", 5, 5, completed = true, claimed = true),
                Challenge(3, "Green Explorer Extraordinaire",
                    "Offset 100% carbon footprint and fly with eco-certified partner airlines on 3 multi-leg trips.",
                    750, "Carbon Offset", "Globe", 3, 1, completed = false, claimed = false),
                Challenge(4, "Zero-Emission Transit Pioneer",
                    "Arrive at participating airports via high-speed electric rail or EV shuttle 4 times.",
                    400, "Ground Transit", "Train", 4, 3, completed = false, claimed = false),
            )
        )
        airlines.addAll(
            listOf(
                LinkedAirline(1, "Emirates Skywards", "EK", "EK-882049112", 1450),
                LinkedAirline(2, "Qatar Privilege Club", "QR", "QR-9018423", 800),
                LinkedAirline(3, "Singapore Airlines KrisFlyer", "SQ", "SQ-4491028", 600),
            )
        )
        notifications.addAll(
            listOf(
                AppNotification(nextId(), "You earned 150 Green Points!",
                    "Digital boarding pass DXB -> LHR successfully verified. +25kg CO2 offset calculated.", "points", false, now - 4 * 60 * 60 * 1000),
                AppNotification(nextId(), "New Premium Reward Available!",
                    "VIP First Class Green Lounge pass is now unlocked for your account.", "reward", false, now - 12 * 60 * 60 * 1000),
                AppNotification(nextId(), "Tier Progress Alert",
                    "Only 1,150 Green Points left until reaching Gold Tier privileges!", "tier", true, now - 26 * 60 * 60 * 1000),
            )
        )
    }

    private fun tierFor(points: Int) = when {
        points >= 7500 -> "Platinum"
        points >= 3500 -> "Gold"
        points >= 1500 -> "Silver"
        else -> "Bronze"
    }

    private fun pushNotification(title: String, message: String, type: String) {
        notifications.add(0, AppNotification(nextId(), title, message, type, false, System.currentTimeMillis()))
    }

    private fun bumpChallenges(category: String) {
        challenges.forEachIndexed { i, ch ->
            if (ch.completed) return@forEachIndexed
            val matches = when (ch.id) {
                1 -> category == "Hydration Station"
                2 -> category == "Digital Boarding Pass" || category == "Digital Receipt" || category == "Mobile Check-in"
                3 -> category == "Verified Carbon Offset"
                4 -> category == "Airport Rail Transit"
                else -> false
            }
            if (!matches) return@forEachIndexed
            val newProg = ch.currentProgress + 1
            val completed = newProg >= ch.targetCount
            challenges[i] = ch.copy(currentProgress = newProg, completed = completed)
            if (completed) {
                pushNotification("Quest Completed: ${ch.title}!", "Ready to claim your +${ch.bonusPoints} PTS bonus reward!", "challenge")
            }
        }
    }

    fun verifyEcoAction(
        category: String,
        airportCode: String,
        customTitle: String? = null,
        points: Int? = null,
        co2: Float? = null,
    ) {
        val defaultPoints = mapOf(
            "Digital Boarding Pass" to 150,
            "Verified Carbon Offset" to 300,
            "Hydration Station" to 30,
            "Digital Receipt" to 45,
            "Airport Rail Transit" to 90,
            "Mobile Check-in" to 60,
        )
        val defaultCo2 = mapOf(
            "Digital Boarding Pass" to 22.5f,
            "Verified Carbon Offset" to 75.0f,
            "Hydration Station" to 0.8f,
            "Digital Receipt" to 1.8f,
            "Airport Rail Transit" to 12.0f,
            "Mobile Check-in" to 2.5f,
        )
        val p = points ?: defaultPoints[category] ?: 100
        val c = co2 ?: defaultCo2[category] ?: 10f
        val ref = "GJ-${(100000..999999).random()}"
        val title = customTitle ?: "Verified Eco Action"

        actions.add(0, VerifiedAction(nextId(), title, category, p, c, airportCode, ref, System.currentTimeMillis()))

        val s = stats
        val newPoints = s.greenPoints + p
        val newTier = tierFor(newPoints)
        stats = s.copy(
            greenPoints = newPoints,
            co2SavedKg = s.co2SavedKg + c,
            plasticAvoidedCount = if (category == "Hydration Station") s.plasticAvoidedCount + 1 else s.plasticAvoidedCount,
            waterSavedLiters = if (category == "Hydration Station") s.waterSavedLiters + 10 else s.waterSavedLiters,
            tier = newTier,
        )
        pushNotification("Earned $p Green Points!", "$title verified at $airportCode. +${"%.1f".format(c)}kg CO\u2082 offset added.", "points")
        if (newTier != s.tier) {
            pushNotification("Promoted to $newTier Status!", "Congratulations! Your eco impact score reached $newPoints points, unlocking premium benefits.", "tier")
        }
        bumpChallenges(category)
        toast = "Verified $category successfully! Points added."
    }

    fun redeemReward(id: Int): Boolean {
        val r = rewards.find { it.id == id } ?: return false
        if (stats.greenPoints < r.cost) {
            toast = "Error: Insufficient Green Points balance."
            return false
        }
        stats = stats.copy(greenPoints = stats.greenPoints - r.cost)
        vouchers.add(0, Voucher(nextId(), r.title, "GJV-${(100000..999999).random()}", System.currentTimeMillis()))
        toast = "Reward redeemed successfully! View in Wallet."
        return true
    }

    fun claimChallengeBonus(id: Int) {
        challenges.forEachIndexed { i, ch ->
            if (ch.id == id && ch.completed && !ch.claimed) {
                stats = stats.copy(greenPoints = stats.greenPoints + ch.bonusPoints)
                challenges[i] = ch.copy(claimed = true)
                toast = "Bonus claimed successfully! +Points added."
            }
        }
    }

    fun linkAirline(name: String, code: String, account: String) {
        airlines.add(LinkedAirline(nextId(), name, code, account, 200))
        stats = stats.copy(greenPoints = stats.greenPoints + 200)
        toast = "Linked $name! +200 bonus Green Points claimed."
    }

    fun unlinkAirline(id: Long) {
        airlines.removeAll { it.id == id }
        toast = "Airline unlinked successfully."
    }

    fun markAllNotificationsRead() {
        notifications.forEachIndexed { i, n ->
            if (!n.read) notifications[i] = n.copy(read = true)
        }
    }

    fun clearNotifications() = notifications.clear()

    fun updateProfile(name: String, email: String) {
        stats = stats.copy(name = name, email = email)
        toast = "Profile updated successfully."
    }
}
