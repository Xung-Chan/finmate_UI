package com.example.ibanking_kltn.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.ibanking_kltn.ui.AppGraph
import com.example.ibanking_kltn.ui.Screens
import com.example.ibanking_kltn.ui.event.ForgotPasswordEffect
import com.example.ibanking_kltn.ui.event.LoginEffect
import com.example.ibanking_kltn.ui.screens.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.SignInScreen
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.viewmodels.AuthViewModel
import com.example.ibanking_kltn.ui.viewmodels.ForgotPasswordViewModel
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.getViewModel

fun NavGraphBuilder.signInGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.SignIn.name,
        route = AppGraph.SignInGraph.name,
    ) {
        composable(route = Screens.SignIn.name) {
            val authViewModel: AuthViewModel = hiltViewModel()

            val authUiState by authViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                authViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        LoginEffect.LoginSuccess -> {
                            onShowSnackBar(
                                SnackBarUiState(
                                    message = "Đăng nhập thành công",
                                    type = SnackBarType.SUCCESS,
                                )
                            )
                            navController.navigate(
                                Screens.Home.name
                            ) {
                                popUpTo(Screens.SignIn.name) {
                                    inclusive = true
                                }
                            }
                        }

                        is LoginEffect.ShowSnackBar -> {
                            onShowSnackBar(effect.snackBar)
                        }

                        is LoginEffect.RequestOtp -> {
                            navController.navigate("${Screens.ForgotPassword.name}/${effect.purpose.name}")
                        }
                    }
                }
            }

            SignInScreen(
                uiState = authUiState,
                onEvent = authViewModel::onEvent
            )

        }
        composable(
            route = "${Screens.ForgotPassword.name}/{purpose}",
            arguments = listOf(
                navArgument("purpose") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val forgotPasswordViewModel: ForgotPasswordViewModel = backStackEntry.getViewModel(
                navController,
                "${Screens.ForgotPassword.name}/{purpose}"
            )

            val uiState by forgotPasswordViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                forgotPasswordViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        ForgotPasswordEffect.ResetPasswordSuccess -> {
                            onShowSnackBar(
                                SnackBarUiState(
                                    message = "Đặt lại mật khẩu thành công. Vui lòng đăng nhập lại.",
                                    type = SnackBarType.SUCCESS,
                                )
                            )
                            navController.popBackStack()
                        }

                        is ForgotPasswordEffect.ShowSnackBar -> {
                            onShowSnackBar(effect.snackBar)
                        }

                        ForgotPasswordEffect.WrongPurpose -> {
                            onShowSnackBar(
                                SnackBarUiState(
                                    message = "Mục đích không hợp lệ",
                                    type = SnackBarType.ERROR,
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                }
            }

            ForgotPasswordScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = forgotPasswordViewModel::onEvent,
            )
        }
    }
}