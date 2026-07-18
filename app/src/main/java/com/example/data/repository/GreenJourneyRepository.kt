package com.example.data.repository

import com.example.data.dao.GreenJourneyDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class GreenJourneyRepository(private val dao: GreenJourneyDao) {

    val userStats: Flow<UserStats?> = dao.getUserStatsFlow()
    val verifiedActions: Flow<List<VerifiedAction>> = dao.getAllVerifiedActionsFlow()
    val rewards: Flow<List<Reward>> = dao.getAllRewardsFlow()
    val vouchers: Flow<List<UserVoucher>> = dao.getAllVouchersFlow()
    val challenges: Flow<List<Challenge>> = dao.getAllChallengesFlow()
    val linkedAirlines: Flow<List<LinkedAirline>> = dao.getAllLinkedAirlinesFlow()
    val notifications: Flow<List<Notification>> = dao.getAllNotificationsFlow()

    suspend fun checkAndSeedData() {
        val stats = dao.getUserStats()
        if (stats != null) return // Already seeded

        // Seed default stats
        dao.insertOrUpdateUserStats(
            UserStats(
                id = 1,
                name = "Captain Sarah Jenkins",
                email = "sarah.jenkins@greenjourney.com",
                greenPoints = 2850,
                co2SavedKg = 142.5f,
                waterSavedLiters = 320,
                plasticAvoidedCount = 48,
                homeAirport = "DXB - Dubai International",
                frequentFlyerId = "GJ-9824106"
            )
        )

        // Seed initial airlines
        dao.insertLinkedAirline(LinkedAirline(airlineName = "Emirates Skywards", airlineCode = "EK", accountNumber = "EK-882049112", pointsSynced = 1450))
        dao.insertLinkedAirline(LinkedAirline(airlineName = "Qatar Privilege Club", airlineCode = "QR", accountNumber = "QR-9018423", pointsSynced = 800))
        dao.insertLinkedAirline(LinkedAirline(airlineName = "Singapore Airlines KrisFlyer", airlineCode = "SQ", accountNumber = "SQ-4491028", pointsSynced = 600))

        // Seed verified actions
        dao.insertVerifiedAction(
            VerifiedAction(
                title = "Digital Boarding Pass - DXB to LHR",
                category = "Digital Boarding Pass",
                pointsEarned = 150,
                co2OffsetKg = 25.0f,
                airportCode = "DXB",
                referenceNo = "GJ-BP-8819",
                timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000
            )
        )
        dao.insertVerifiedAction(
            VerifiedAction(
                title = "Verified SAF Carbon Offset Purchase",
                category = "Verified Carbon Offset",
                pointsEarned = 350,
                co2OffsetKg = 85.0f,
                airportCode = "DXB",
                referenceNo = "GJ-CO2-9901",
                timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000
            )
        )
        dao.insertVerifiedAction(
            VerifiedAction(
                title = "Refilled Reusable Bottle at Gate B14",
                category = "Hydration Station",
                pointsEarned = 25,
                co2OffsetKg = 0.5f,
                airportCode = "DXB",
                referenceNo = "GJ-HYD-1022",
                timestamp = System.currentTimeMillis() - 32 * 60 * 60 * 1000
            )
        )
        dao.insertVerifiedAction(
            VerifiedAction(
                title = "Paperless Duty Free Digital Receipt",
                category = "Digital Receipt",
                pointsEarned = 40,
                co2OffsetKg = 1.2f,
                airportCode = "DXB",
                referenceNo = "GJ-REC-4410",
                timestamp = System.currentTimeMillis() - 48 * 60 * 60 * 1000
            )
        )

        // Seed rewards
        dao.insertRewards(
            listOf(
                Reward(1, "20% off Gourmet Airport Organic Café", "Café", 300, "Enjoy sustainable, locally sourced organic dining at participating terminal lounges and cafes.", "20% OFF", "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&q=80&w=600"),
                Reward(2, "High-Speed In-Flight Wi-Fi Flight Pass", "In-flight Wi-Fi", 800, "Unlimited streaming and messaging connection on all short and long-haul partner flights.", "FREE PASS", "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&q=80&w=600"),
                Reward(3, "Extra Legroom Preferred Seat Upgrade", "Seat Selection", 1200, "Reserve front row or exit row extra legroom seats with eco-leather ergonomic comfort.", "FREE UPGRADE", "https://images.unsplash.com/photo-1569154941061-e231b4725ef1?auto=format&fit=crop&q=80&w=600"),
                Reward(4, "15% Eco-Friendly Duty Free Voucher", "Duty Free", 500, "Save on sustainable cosmetics, recycled travel items, and certified zero-emission perfume lines.", "15% OFF", "https://images.unsplash.com/photo-1513151233558-d860c5398176?auto=format&fit=crop&q=80&w=600"),
                Reward(5, "VIP First Class Green Lounge Access Pass", "Lounge discounts", 2200, "Access tranquil, zero-single-use-plastic flagship VIP airport lounges worldwide with complimentary organic buffet.", "VIP PASS", "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=80&w=600"),
                Reward(6, "Recycled Ocean Plastics Travel Toiletry Kit", "Eco-friendly products", 1500, "Premium flight accessory set constructed from upcycled ocean polymer with biodegradable amenities.", "100% REDEEM", "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&q=80&w=600"),
                Reward(7, "Direct SAF Aviation Fuel Carbon Offset Token", "Carbon Offset", 1000, "Directly sponsor 100kg of Sustainable Aviation Fuel (SAF) into partner airport fueling systems.", "100kg SAF", "https://images.unsplash.com/photo-1464037866556-6812c9d1c72e?auto=format&fit=crop&q=80&w=600")
            )
        )

        // Seed challenges
        dao.insertChallenges(
            listOf(
                Challenge(1, "Plastic-Free Traveler", "Skip single-use cups and plastic bags on 3 consecutive flights using reusable bottles and digital passes.", 300, "Waste Elimination", "Droplets", 3, currentProgress = 2, completed = false),
                Challenge(2, "100% Digital Traveler", "Use zero printed paper passes, tags, or invoices across 5 journeys.", 500, "Paperless Flight", "Smartphone", 5, currentProgress = 5, completed = true, claimed = true),
                Challenge(3, "Green Explorer Extraordinaire", "Offset 100% carbon footprint and fly with eco-certified partner airlines on 3 multi-leg trips.", 750, "Carbon Offset", "Globe", 3, currentProgress = 1, completed = false),
                Challenge(4, "Zero-Emission Transit Pioneer", "Arrive at participating airports via high-speed electric rail or EV shuttle 4 times.", 400, "Ground Transit", "Train", 4, currentProgress = 3, completed = false)
            )
        )

        // Seed notifications
        dao.insertNotification(Notification(title = "You earned 150 Green Points!", message = "Digital boarding pass DXB -> LHR successfully verified. +25kg CO2 offset calculated.", type = "points", read = false))
        dao.insertNotification(Notification(title = "New Premium Reward Available!", message = "VIP First Class Green Lounge pass is now unlocked for your account.", type = "reward", read = false))
        dao.insertNotification(Notification(title = "Tier Progress Alert", message = "Only 1,150 Green Points left until reaching Gold Tier privileges!", type = "tier", read = true))
    }

    suspend fun verifyAction(
        category: String,
        airportCode: String,
        customTitle: String? = null,
        pointsOverride: Int? = null,
        co2Override: Float? = null
    ) {
        val currentStats = dao.getUserStats() ?: return

        val finalTitle = customTitle ?: "Verified Eco Action"
        val finalPoints = pointsOverride ?: when (category) {
            "Digital Boarding Pass" -> 150
            "Verified Carbon Offset" -> 300
            "Hydration Station" -> 30
            "Digital Receipt" -> 45
            "Airport Rail Transit" -> 90
            "Mobile Check-in" -> 60
            else -> 100
        }
        val finalCo2 = co2Override ?: when (category) {
            "Digital Boarding Pass" -> 22.5f
            "Verified Carbon Offset" -> 75.0f
            "Hydration Station" -> 0.8f
            "Digital Receipt" -> 1.8f
            "Airport Rail Transit" -> 12.0f
            "Mobile Check-in" -> 2.5f
            else -> 10.0f
        }

        val ref = "GJ-${Random.nextInt(100000, 999999)}"
        val action = VerifiedAction(
            title = finalTitle,
            category = category,
            pointsEarned = finalPoints,
            co2OffsetKg = finalCo2,
            airportCode = airportCode,
            referenceNo = ref
        )
        dao.insertVerifiedAction(action)

        // Update UserStats
        val newPoints = currentStats.greenPoints + finalPoints
        val newCo2 = currentStats.co2SavedKg + finalCo2
        val newPlastic = if (category == "Hydration Station") currentStats.plasticAvoidedCount + 1 else currentStats.plasticAvoidedCount
        val newWater = if (category == "Hydration Station") currentStats.waterSavedLiters + 10 else currentStats.waterSavedLiters

        // Tier thresholds: Bronze (<1500), Silver (1500-3499), Gold (3500-7499), Platinum (7500+)
        val newTier = when {
            newPoints >= 7500 -> "Platinum"
            newPoints >= 3500 -> "Gold"
            newPoints >= 1500 -> "Silver"
            else -> "Bronze"
        }

        val updatedStats = currentStats.copy(
            greenPoints = newPoints,
            co2SavedKg = newCo2,
            plasticAvoidedCount = newPlastic,
            waterSavedLiters = newWater,
            tier = newTier
        )
        dao.insertOrUpdateUserStats(updatedStats)

        // Insert notification
        dao.insertNotification(
            Notification(
                title = "Earned $finalPoints Green Points!",
                message = "$finalTitle verified at $airportCode. +${finalCo2}kg CO₂ offset added.",
                type = "points"
              )
        )

        // Notify if tier changed
        if (newTier != currentStats.tier) {
            dao.insertNotification(
                Notification(
                    title = "Promoted to $newTier Status!",
                    message = "Congratulations! Your eco impact score reached $newPoints points, unlocking premium benefits.",
                    type = "tier"
                )
            )
        }

        // Increment matching challenge progress
        val challengesList = dao.getAllChallengesFlow().firstOrNull() ?: emptyList()
        challengesList.forEach { ch ->
            if (!ch.completed) {
                var matches = false
                if (ch.id == 1 && category == "Hydration Station") matches = true
                if (ch.id == 2 && (category == "Digital Boarding Pass" || category == "Digital Receipt" || category == "Mobile Check-in")) matches = true
                if (ch.id == 3 && category == "Verified Carbon Offset") matches = true
                if (ch.id == 4 && category == "Airport Rail Transit") matches = true

                if (matches) {
                    val newProg = ch.currentProgress + 1
                    val completed = newProg >= ch.targetCount
                    val updatedCh = ch.copy(currentProgress = newProg, completed = completed)
                    dao.updateChallenge(updatedCh)

                    if (completed) {
                        dao.insertNotification(
                            Notification(
                                title = "Quest Completed: ${ch.title}!",
                                message = "Ready to claim your +${ch.bonusPoints} PTS bonus reward!",
                                type = "challenge"
                            )
                        )
                    }
                }
            }
        }
    }

    suspend fun redeemReward(rewardId: Int): Boolean {
        val stats = dao.getUserStats() ?: return false
        val reward = dao.getRewardById(rewardId) ?: return false

        if (stats.greenPoints < reward.pointsRequired) return false

        // Deduct points
        val newPoints = stats.greenPoints - reward.pointsRequired
        dao.insertOrUpdateUserStats(stats.copy(greenPoints = newPoints))

        // Create Voucher
        val code = "GJ-${reward.category.take(3).uppercase()}-${Random.nextInt(100000, 999999)}"
        dao.insertVoucher(
            UserVoucher(
                rewardId = reward.id,
                voucherCode = code
            )
        )

        // Create notification
        dao.insertNotification(
            Notification(
                title = "Reward Redeemed Successfully!",
                message = "Redeemed '${reward.title}' for ${reward.pointsRequired} Pts. Digital Voucher code: $code.",
                type = "reward"
            )
        )

        return true
    }

    suspend fun claimChallengeBonus(challengeId: Int): Boolean {
        val ch = dao.getChallengeById(challengeId) ?: return false
        if (!ch.completed || ch.claimed) return false

        val stats = dao.getUserStats() ?: return false

        // Mark as claimed
        dao.updateChallenge(ch.copy(claimed = true))

        // Add bonus points
        val newPoints = stats.greenPoints + ch.bonusPoints
        dao.insertOrUpdateUserStats(stats.copy(greenPoints = newPoints))

        // Create notification
        dao.insertNotification(
            Notification(
                title = "Claimed Challenge Reward!",
                message = "Unlocked +${ch.bonusPoints} PTS for completing the quest: '${ch.title}'.",
                type = "challenge"
            )
        )

        return true
    }

    suspend fun linkAirline(airlineName: String, code: String, accountNo: String) {
        val stats = dao.getUserStats() ?: return

        // 200 points bonus sign up!
        val bonus = 200
        val newPoints = stats.greenPoints + bonus
        dao.insertOrUpdateUserStats(stats.copy(greenPoints = newPoints))

        dao.insertLinkedAirline(
            LinkedAirline(
                airlineName = airlineName,
                airlineCode = code,
                accountNumber = accountNo,
                pointsSynced = 500
            )
        )

        dao.insertNotification(
            Notification(
                title = "Airline Linked: $airlineName",
                message = "Linked frequent flyer account $accountNo. Claimed +200 bonus Green Points!",
                type = "system"
            )
        )
    }

    suspend fun unlinkAirline(id: Int) {
        dao.deleteLinkedAirline(id)
    }

    suspend fun markAllNotificationsRead() {
        dao.markAllNotificationsRead()
    }

    suspend fun clearNotifications() {
        dao.clearAllNotifications()
    }

    suspend fun triggerDemoNotification() {
        val titles = listOf(
            "Extra 50 Green Points credited!",
            "Flight DXB -> SIN offset verified",
            "New Gold Lounge discount available",
            "Weekly Eco Summary: 12kg CO₂ reduced"
        )
        val title = titles.random()
        dao.insertNotification(
            Notification(
                title = title,
                message = "Automatic verification system logged eco activity on your travel route.",
                type = "points"
            )
        )
    }

    suspend fun updateProfile(name: String, email: String) {
        val stats = dao.getUserStats() ?: return
        dao.insertOrUpdateUserStats(stats.copy(name = name, email = email))
    }
}
