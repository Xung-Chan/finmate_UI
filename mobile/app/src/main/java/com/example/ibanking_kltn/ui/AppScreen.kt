package com.example.ibanking_kltn.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibanking_kltn.ui.screens.TransferScreen
import com.example.ibanking_kltn.ui.screens.auth.AuthViewModel
import com.example.ibanking_kltn.ui.screens.auth.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.auth.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.auth.SignInScreen
import com.example.ibanking_kltn.ui.screens.auth.SignUpScreen
import com.example.ibanking_kltn.ui.screens.home.HomeScreen

enum class Screens {
    SignIn,SignUp,Transfer, ChangePassword, ForgotPassword, Home,
}

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    val context : Context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screens.SignIn.name
    ) {
        composable(route = Screens.SignIn.name) {
            val viewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            SignInScreen(
                uiState = uiState,
                onLoginClick = {
                    viewModel.onLoginClick(
                        context = context,
                        navControler = navController
                    )
                },
                onEmailChange = { it -> viewModel.onEmailChange(it) },
                onPasswordChange = { it -> viewModel.onPasswordChange(it) },
                onChangeVisiblePassword = {
                    viewModel.onChangeVisiblePassword()
                },
                checkEnableLogin = {viewModel.checkEnableLogin()}
            )
        }
        composable(route = Screens.SignUp.name) {
            SignUpScreen()
        }
        composable(route = Screens.ForgotPassword.name) {
            ForgotPasswordScreen()
        }
        composable(route = Screens.ChangePassword.name) {
            ChangePasswordScreen()
        }
        composable(route = Screens.Home.name) {
            HomeScreen()
        }
        composable(route = Screens.Transfer.name) {
            TransferScreen()
        }
        

    }
}