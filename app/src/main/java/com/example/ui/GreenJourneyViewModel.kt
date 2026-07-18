package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.*
import com.example.data.repository.GreenJourneyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GreenJourneyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GreenJourneyRepository

    val userStats: StateFlow<UserStats?>
    val verifiedActions: StateFlow<List<VerifiedAction>>
    val rewards: StateFlow<List<Reward>>
    val vouchers: StateFlow<List<UserVoucher>>
    val challenges: StateFlow<List<Challenge>>
    val linkedAirlines: StateFlow<List<LinkedAirline>>
    val notifications: StateFlow<List<Notification>>

    // Navigation and UX State
    private val _activeTab = MutableStateFlow("home")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    private val _scannedCode = MutableStateFlow<String?>(null)
    val scannedCode: StateFlow<String?> = _scannedCode.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = GreenJourneyRepository(database.greenJourneyDao())

        // Hot Flows with StateIn
        userStats = repository.userStats
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        verifiedActions = repository.verifiedActions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        rewards = repository.rewards
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        vouchers = repository.vouchers
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        challenges = repository.challenges
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        linkedAirlines = repository.linkedAirlines
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        notifications = repository.notifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initialize and seed database
        viewModelScope.launch {
            repository.checkAndSeedData()
        }
    }

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    fun verifyEcoAction(
        category: String,
        airportCode: String,
        customTitle: String? = null,
        points: Int? = null,
        co2: Float? = null
    ) {
        viewModelScope.launch {
            repository.verifyAction(category, airportCode, customTitle, points, co2)
            _toastMessage.emit("Verified $category successfully! Points added.")
        }
    }

    fun redeemReward(rewardId: Int, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.redeemReward(rewardId)
            if (success) {
                _toastMessage.emit("Reward redeemed successfully! View in Wallet.")
                onSuccess()
            } else {
                _toastMessage.emit("Error: Insufficient Green Points balance.")
                onError("Insufficient points")
            }
        }
    }

    fun claimChallengeBonus(challengeId: Int) {
        viewModelScope.launch {
            val success = repository.claimChallengeBonus(challengeId)
            if (success) {
                _toastMessage.emit("Bonus claimed successfully! +Points added.")
            }
        }
    }

    fun linkAirline(airlineName: String, code: String, accountNo: String) {
        viewModelScope.launch {
            repository.linkAirline(airlineName, code, accountNo)
            _toastMessage.emit("Linked $airlineName! +200 bonus Green Points claimed.")
        }
    }

    fun unlinkAirline(id: Int) {
        viewModelScope.launch {
            repository.unlinkAirline(id)
            _toastMessage.emit("Airline unlinked successfully.")
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun clearNotifications() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }

    fun triggerDemoNotification() {
        viewModelScope.launch {
            repository.triggerDemoNotification()
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            repository.updateProfile(name, email)
            _toastMessage.emit("Profile updated successfully.")
        }
    }

    fun simulateQrScan(code: String, onScanned: () -> Unit) {
        viewModelScope.launch {
            _scannedCode.value = code
            // Auto parse code
            // Format example: GJ-TRANSIT-DXB, GJ-WATER-DXB, GJ-RECEIPT-DXB, GJ-BOARDING-DXB-EK201
            val parts = code.split("-")
            if (parts.size >= 3 && parts[0] == "GJ") {
                val actionType = parts[1]
                val airport = parts[2]
                when (actionType) {
                    "TRANSIT" -> verifyEcoAction("Airport Rail Transit", airport, "Airport Electric Express Train", 90, 12.0f)
                    "WATER" -> verifyEcoAction("Hydration Station", airport, "Reusable Bottle Hydration Refill", 30, 0.8f)
                    "RECEIPT" -> verifyEcoAction("Digital Receipt", airport, "Paperless Duty Free Digital Receipt", 40, 1.2f)
                    "BOARDING" -> {
                        val flight = parts.getOrNull(3) ?: "FLIGHT"
                        verifyEcoAction("Digital Boarding Pass", airport, "Digital Boarding Pass - Flight $flight", 150, 25.0f)
                    }
                    "OFFSET" -> verifyEcoAction("Verified Carbon Offset", airport, "Verified SAF Biofuel Purchase", 350, 85.0f)
                    else -> verifyEcoAction("Mobile Check-in", airport, "Verified Mobile Eco Check-in", 60, 2.5f)
                }
            } else {
                verifyEcoAction("Mobile Check-in", "DXB", "Eco Airport Mobile Check-in", 60, 2.5f)
            }
            onScanned()
        }
    }

    fun clearScannedCode() {
        _scannedCode.value = null
    }
}
