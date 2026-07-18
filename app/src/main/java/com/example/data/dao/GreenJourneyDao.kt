package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GreenJourneyDao {

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    fun getUserStatsFlow(): Flow<UserStats?>

    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStats(): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStats(stats: UserStats)

    @Query("SELECT * FROM verified_actions ORDER BY timestamp DESC")
    fun getAllVerifiedActionsFlow(): Flow<List<VerifiedAction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerifiedAction(action: VerifiedAction)

    @Query("SELECT * FROM rewards ORDER BY pointsRequired ASC")
    fun getAllRewardsFlow(): Flow<List<Reward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<Reward>)

    @Query("SELECT * FROM rewards WHERE id = :id LIMIT 1")
    suspend fun getRewardById(id: Int): Reward?

    @Query("SELECT * FROM user_vouchers ORDER BY redeemedAt DESC")
    fun getAllVouchersFlow(): Flow<List<UserVoucher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoucher(voucher: UserVoucher)

    @Query("SELECT * FROM challenges ORDER BY id ASC")
    fun getAllChallengesFlow(): Flow<List<Challenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<Challenge>)

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Query("SELECT * FROM challenges WHERE id = :id LIMIT 1")
    suspend fun getChallengeById(id: Int): Challenge?

    @Query("SELECT * FROM linked_airlines ORDER BY id DESC")
    fun getAllLinkedAirlinesFlow(): Flow<List<LinkedAirline>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkedAirline(airline: LinkedAirline)

    @Query("DELETE FROM linked_airlines WHERE id = :id")
    suspend fun deleteLinkedAirline(id: Int)

    @Query("SELECT * FROM notifications ORDER BY createdAt DESC")
    fun getAllNotificationsFlow(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET `read` = 1")
    suspend fun markAllNotificationsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()
}
