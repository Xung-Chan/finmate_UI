package com.example.ibanking_kltn

import android.app.Application
import com.example.ibanking_kltn.data.di.AppSessionManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class IBanking: Application() {
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppSessionEntryPoint {
    fun appSessionManager(): AppSessionManager
}
