package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1,
    val name: String = "Captain Sarah Jenkins",
    val email: String = "sarah.jenkins@greenjourney.com",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=200",
    val tier: String = "Silver", // Bronze, Silver, Gold, Platinum
    val greenPoints: Int = 2850,
    val co2SavedKg: Float = 142.5f,
    val waterSavedLiters: Int = 320,
    val plasticAvoidedCount: Int = 48,
    val homeAirport: String = "DXB - Dubai International",
    val frequentFlyerId: String = "GJ-9824106"
)

@Entity(tableName = "verified_actions")
data class VerifiedAction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // Boarding Pass, Carbon Offset, Hydration Station, Digital Receipt, Airport Rail Transit, Mobile Check-in
    val pointsEarned: Int,
    val co2OffsetKg: Float,
    val status: String = "Verified",
    val airportCode: String,
    val referenceNo: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val pointsRequired: Int,
    val description: String,
    val discountValue: String,
    val imageUrl: String,
    val tierRequirement: String = "Bronze",
    val stock: Int = 100
)

@Entity(tableName = "user_vouchers")
data class UserVoucher(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rewardId: Int,
    val voucherCode: String,
    val status: String = "Active", // Active, Used, Expired
    val redeemedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + 90L * 24L * 60L * 60L * 1000L // 90 days validity
)

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val bonusPoints: Int,
    val category: String,
    val iconName: String, // Droplets, Smartphone, Globe, Train
    val targetCount: Int,
    val currentProgress: Int = 0,
    val completed: Boolean = false,
    val claimed: Boolean = false
)

@Entity(tableName = "linked_airlines")
data class LinkedAirline(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val airlineName: String,
    val airlineCode: String,
    val accountNumber: String,
    val status: String = "Connected",
    val pointsSynced: Int = 500,
    val lastSyncedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val type: String, // points, reward, tier, challenge, system
    val read: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
