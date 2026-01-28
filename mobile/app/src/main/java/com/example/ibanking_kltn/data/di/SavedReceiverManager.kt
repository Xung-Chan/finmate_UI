package com.example.ibanking_kltn.data.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ibanking_kltn.data.dtos.SavedReceiverInfo
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json


@Singleton
class SavedReceiverManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val _savedReceivers = MutableStateFlow(getAll())
    val savedReceivers = _savedReceivers.asStateFlow()
    private val SAVED_RECEIVER = "SAVED_RECEIVER"
    private val SIZE = 20

    fun add(receiver: SavedReceiverInfo): Boolean {

        val savedReceivers = getAll().toMutableList()
        if (savedReceivers.size < SIZE) {
            savedReceivers.add(receiver)
            val payload = Json.encodeToString(savedReceivers)
            sharedPreferences.edit {
                putString(SAVED_RECEIVER, payload)
            }
            _savedReceivers.value=savedReceivers
            return true
        }
        return false

    }

    fun deleteByWalletNumber(walletNumber: String) {
        val savedReceivers = getAll().toMutableList()
        savedReceivers.removeAll { it.toWalletNumber == walletNumber }
        val payload = Json.encodeToString(savedReceivers)
        sharedPreferences.edit {
            putString(SAVED_RECEIVER, payload)
        }
        _savedReceivers.value=savedReceivers
    }

    fun getAll(): List<SavedReceiverInfo> {
        val serviceJson = sharedPreferences.getString(SAVED_RECEIVER, null)
        return if (serviceJson != null) {
            Json.decodeFromString(serviceJson)
        } else {
            emptyList()
        }
    }

}