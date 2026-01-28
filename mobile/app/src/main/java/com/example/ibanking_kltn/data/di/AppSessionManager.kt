package com.example.ibanking_kltn.data.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class AppSessionManager @Inject constructor(
    private val tokenManager: TokenManager
) : DefaultLifecycleObserver {

    private var lastBackgroundTime = 0L

    private val _timeout = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val timeout = _timeout.asSharedFlow()

    override fun onStop(owner: LifecycleOwner) {
        lastBackgroundTime = System.currentTimeMillis()
    }

    override fun onStart(owner: LifecycleOwner) {
        val diff = System.currentTimeMillis() - lastBackgroundTime
        if (lastBackgroundTime != 0L && diff > TIMEOUT) {
            tokenManager.clearToken()
            _timeout.tryEmit(Unit)
        }
    }

    companion object {
        private const val TIMEOUT = 5 * 60 * 1000L
    }
}
