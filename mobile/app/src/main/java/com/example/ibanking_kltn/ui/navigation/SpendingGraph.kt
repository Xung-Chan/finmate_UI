package com.example.ibanking_kltn.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.ibanking_kltn.ui.screens.spending.category_management.CategoryManagement
import com.example.ibanking_kltn.ui.screens.spending.category_management.CategoryManagementEffect
import com.example.ibanking_kltn.ui.screens.spending.category_management.CategoryManagementViewModel
import com.example.ibanking_kltn.ui.screens.spending.snapshot_detail.SpendingCategory
import com.example.ibanking_kltn.ui.screens.spending.snapshot_detail.SpendingDetailEffect
import com.example.ibanking_kltn.ui.screens.spending.snapshot_detail.SpendingDetailViewModel
import com.example.ibanking_kltn.ui.screens.spending.snapshot_detail.SpendingSnapshotDetail
import com.example.ibanking_kltn.ui.screens.spending.spending_management.SpendingManagement
import com.example.ibanking_kltn.ui.screens.spending.spending_management.SpendingManagementEffect
import com.example.ibanking_kltn.ui.screens.spending.spending_management.SpendingManagementViewModel
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState

fun NavGraphBuilder.spendingGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
//        startDestination = "${Screens.SpendingSnapshotDetail.name}/012",
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

                        SpendingManagementEffect.NavigateToCategoryManagement -> {
                            navController.navigate(Screens.CategoryManagement.name)
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
            LaunchedEffect(Unit) {
                spendingDetailVM.uiEffect.collect { effect ->
                    when (effect) {
                        SpendingDetailEffect.NavigateToAddTransaction -> {
                            navController.navigate(Screens.DefineTransaction.name)
                        }

                        SpendingDetailEffect.NavigateToCategory -> {
                            navController.navigate(Screens.SpendingCategory.name)
                        }

                        is SpendingDetailEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                    }
                }
            }
            SpendingSnapshotDetail(
                uiState = uiState,
                records = records,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = spendingDetailVM::onEvent,
                onRecordClick = { record ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("record", record)
                    navController.navigate(Screens.RecordDetail.name)
                }
            )
        }
        composable(route = Screens.SpendingCategory.name) { backStackEntry ->
            val previousEntry = remember(backStackEntry) {
                navController.previousBackStackEntry!!
            }
            val spendingDetailVM = hiltViewModel<SpendingDetailViewModel>(previousEntry)
            val uiState by spendingDetailVM.uiState.collectAsState()
            LaunchedEffect(Unit) {
                spendingDetailVM.uiEffect.collect { effect ->
                    when (effect) {
                        is SpendingDetailEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                        SpendingDetailEffect.NavigateToAddTransaction -> {
                            // no-op
                        }

                        SpendingDetailEffect.NavigateToCategory -> {
                            // no-op
                        }
                    }
                }
            }
            SpendingCategory(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEvent = spendingDetailVM::onEvent
            )

        }

        composable(route = Screens.RecordDetail.name) { backStackEntry ->
            val previousEntry = remember(backStackEntry) {
                navController.previousBackStackEntry!!
            }
            val record = previousEntry.savedStateHandle.get<com.example.ibanking_kltn.dtos.responses.SpendingRecordResponse>("record")

            if (record != null) {
                com.example.ibanking_kltn.ui.screens.spending.record_detail.RecordDetailScreen(
                    record = record,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = Screens.CategoryManagement.name) {
            val categoryManagementViewModel = hiltViewModel<CategoryManagementViewModel>()
            val uiState by categoryManagementViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                categoryManagementViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is CategoryManagementEffect.ShowSnackBar -> onShowSnackBar(effect.snackBar)
                    }
                }
            }
            CategoryManagement(
                uiState = uiState,
                onEvent = categoryManagementViewModel::onEvent,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}