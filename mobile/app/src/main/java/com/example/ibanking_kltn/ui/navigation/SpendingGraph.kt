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
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.dtos.definitions.AppGraph
import com.example.ibanking_kltn.dtos.definitions.NavKey
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.ui.event.SpendingManagementEffect
import com.example.ibanking_kltn.ui.screens.SpendingManagement
import com.example.ibanking_kltn.ui.screens.SpendingSnapshotDetail
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.viewmodels.SpendingDetailViewModel
import com.example.ibanking_kltn.ui.viewmodels.SpendingManagementViewModel

fun NavGraphBuilder.spendingGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.SpendingManagement.name,
        route = AppGraph.SpendingGraph.name,
    ) {
        composable(route = Screens.SpendingManagement.name) {
            val spendingManagementViewModel = hiltViewModel<SpendingManagementViewModel>()
            val uiState by spendingManagementViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                spendingManagementViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is SpendingManagementEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                        is SpendingManagementEffect.NavigateToDetail -> {
                            navController.navigate(route = effect.route)
                        }
                    }
                }
            }
            SpendingManagement(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onEvent = spendingManagementViewModel::onEvent
            )
        }
        composable(
            route = "${Screens.SpendingSnapshotDetail.name}/{${NavKey.SPENDING_SNAPSHOT_ID.name}}",
            arguments = listOf(
                navArgument(NavKey.SPENDING_SNAPSHOT_ID.name) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val spendingDetailVM = hiltViewModel<SpendingDetailViewModel>()
            val uiState by spendingDetailVM.uiState.collectAsState()
            val records = spendingDetailVM.records.collectAsLazyPagingItems()
            SpendingSnapshotDetail(
                uiState = uiState,
                records = records,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = spendingDetailVM::onEvent

            )
        }

    }
}