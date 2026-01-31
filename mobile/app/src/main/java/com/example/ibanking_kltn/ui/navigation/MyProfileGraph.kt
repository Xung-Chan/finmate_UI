package com.example.ibanking_kltn.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.ibanking_kltn.dtos.definitions.AppGraph
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.ui.event.CreateVerificationEffect
import com.example.ibanking_kltn.ui.event.MyProfileEffect
import com.example.ibanking_kltn.ui.event.SavedReceiverEffect
import com.example.ibanking_kltn.ui.screens.CreateVerificationRequestScreen
import com.example.ibanking_kltn.ui.screens.MyProfileScreen
import com.example.ibanking_kltn.ui.screens.SavedReceiverScreen
import com.example.ibanking_kltn.ui.screens.TermsAndConditionsScreen
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.viewmodels.CreateVerificationRequestViewModel
import com.example.ibanking_kltn.ui.viewmodels.MyProfileViewModel
import com.example.ibanking_kltn.ui.viewmodels.SavedReceiverViewModel
import com.example.ibanking_kltn.utils.SnackBarType

fun NavGraphBuilder.myProfileGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.MyProfile.name,
        route = AppGraph.MyProfileGraph.name,
    ) {
        composable(route = Screens.MyProfile.name) {
            val myProfileViewModel: MyProfileViewModel = hiltViewModel()
            val uiState by myProfileViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                myProfileViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is MyProfileEffect.ShowSnackBar -> {
                            onShowSnackBar(effect.snackBar)
                        }

                        MyProfileEffect.NavigateToVerificationRequest -> {
                            navController.navigate(Screens.VerificationRequest.name)
                        }
                    }
                }
            }

            MyProfileScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = myProfileViewModel::onEvent,
                onNavigateMyContacts = {
                    navController.navigate(Screens.SavedReceiver.name)
                },
                onNavigateToTermsAndConditions = {
                    navController.navigate(Screens.TermAndConditions.name)
                },
            )
        }

        composable(route = Screens.VerificationRequest.name) {
            val createVerificationRequestViewModel: CreateVerificationRequestViewModel =
                hiltViewModel()
            val uiState by createVerificationRequestViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                createVerificationRequestViewModel.uiEffect.collect { effect ->

                    when (effect) {
                        is CreateVerificationEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                        CreateVerificationEffect.SubmitSuccess ->{
                            onShowSnackBar(
                                SnackBarUiState(
                                    message = "Gửi yêu cầu xác thực thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                }
            }
            CreateVerificationRequestScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = createVerificationRequestViewModel::onEvent

            )
        }
        composable(
            route = Screens.SavedReceiver.name
        ) {
            val savedReceiverViewModel: SavedReceiverViewModel = hiltViewModel()
            val uiState by savedReceiverViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                savedReceiverViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is SavedReceiverEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                    }
                }
            }

            SavedReceiverScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = savedReceiverViewModel::onEvent
            )
        }
        composable(
            route = Screens.TermAndConditions.name
        ) {
            TermsAndConditionsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}