package com.example.ibanking_kltn.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ibanking_kltn.ui.AppGraph
import com.example.ibanking_kltn.ui.Screens
import com.example.ibanking_kltn.ui.event.ChangePasswordEffect
import com.example.ibanking_kltn.ui.screens.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordSuccessfullyScreen
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.viewmodels.ChangPasswordViewModel

fun NavGraphBuilder.changePasswordGraph(
    navController: NavController,
    onShowSnackBar: (SnackBarUiState) -> Unit

) {
    this.navigation(
        startDestination = Screens.ChangePassword.name,
        route = AppGraph.ChangePasswordGraph.name,
    ) {
        composable(route = Screens.ChangePassword.name) {
            val changePasswordViewModel: ChangPasswordViewModel = hiltViewModel()

            val uiState by changePasswordViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                changePasswordViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        ChangePasswordEffect.ChangePasswordSuccess -> {
                            navController.navigate(Screens.ChangePasswordSuccess.name) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }

                        is ChangePasswordEffect.ShowSnackBar -> {
                            onShowSnackBar(effect.snackBar)
                        }
                    }
                }
            }
            ChangePasswordScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onEvent = changePasswordViewModel::onEvent
            )
        }
        composable(
            route = Screens.ChangePasswordSuccess.name
        ) {
            ChangePasswordSuccessfullyScreen(
                onBackToLogin = {
                    navController.navigate(Screens.SignIn.name) {
                        popUpTo(Screens.SignIn.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}