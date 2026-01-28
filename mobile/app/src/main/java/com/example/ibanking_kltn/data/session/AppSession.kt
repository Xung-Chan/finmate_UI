package com.example.ibanking_kltn.data.session

import com.example.ibanking_kltn.ui.uistates.UserSessionUS
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@Singleton
class UserSession @Inject constructor(
) {
    private val _user = MutableStateFlow<UserSessionUS?>(null)
    val user: StateFlow<UserSessionUS?> = _user.asStateFlow()

    fun setUser(user: UserSessionUS) {
        _user.value = user
    }

    fun clear() {
        _user.value = null
    }

}