package com.example.ibanking_kltn.data.di

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Singleton
class AppCoroutineScope @Inject constructor() {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
