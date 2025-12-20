package com.example.ibanking_kltn.data.di

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.dtos.ServiceItem
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json


@Singleton
class ServiceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val FAVORITE_SERVICE = "FAVORITE_SERVICE"
    private val RECENT_SERVICE = "RECENT_SERVICE"
    private val FAVORITE_SIZE = 4

    fun updateFavorite(services: List<ServiceItem>) {
        val serviceJson = Json.encodeToString(services)
        if (services.size == FAVORITE_SIZE) {
            sharedPreferences.edit {
                putString(FAVORITE_SERVICE, serviceJson)
            }
        }
    }

    fun getFavoriteServices(): List<ServiceItem> {
        val default=listOf(
            ServiceItem(
                service = ServiceCategory.MONEY_TRANSFER.name,
                lastUsed = System.currentTimeMillis(),
            ),
            ServiceItem(
                service = ServiceCategory.DEPOSIT.name,
                lastUsed = System.currentTimeMillis()
            ),
            ServiceItem(
                service = ServiceCategory.BILL_PAYMENT.name,
                lastUsed = System.currentTimeMillis()
            ),
            ServiceItem(
                service = ServiceCategory.BILL_HISTORY.name,
                lastUsed = System.currentTimeMillis()
            ),
        )
        val serviceJson = sharedPreferences.getString(FAVORITE_SERVICE, null)
        return if (serviceJson != null) {
            Json.decodeFromString(serviceJson)
        } else {
            updateFavorite(default)
            default
        }
    }

    fun addRecentService(service: ServiceItem) {
        val recentServices = getRecentServices().toMutableList()
        recentServices.removeAll { it.service == service.service }
        recentServices.add(0, service)
        updateRecentService(recentServices.subList(0, 4))
    }

    fun updateRecentService(services: List<ServiceItem>) {
        val serviceJson = Json.encodeToString(services)
        sharedPreferences.edit {
            putString(RECENT_SERVICE, serviceJson)
        }
    }

    fun getRecentServices(): List<ServiceItem> {
        val serviceJson = sharedPreferences.getString(RECENT_SERVICE, null)
        return if (serviceJson != null) {
            val services=Json.decodeFromString(serviceJson) as List<ServiceItem>
            services.sortedBy { -it.lastUsed  }
        } else {
            listOf(
                ServiceItem(
                    service = ServiceCategory.MONEY_TRANSFER.name,
                    lastUsed = System.currentTimeMillis(),
                ),
                ServiceItem(
                    service = ServiceCategory.DEPOSIT.name,
                    lastUsed = System.currentTimeMillis()
                ),
                ServiceItem(
                    service = ServiceCategory.BILL_PAYMENT.name,
                    lastUsed = System.currentTimeMillis()
                ),
                ServiceItem(
                    service = ServiceCategory.BILL_HISTORY.name,
                    lastUsed = System.currentTimeMillis()
                ),
            )
        }
    }
}