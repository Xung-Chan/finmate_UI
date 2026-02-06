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
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.ibanking_kltn.dtos.definitions.AppGraph
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.ui.screens.deposit.HandleDepositEffect
import com.example.ibanking_kltn.ui.screens.deposit.GatewayDeposit
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.screens.deposit.DepositViewModel
import com.example.ibanking_kltn.ui.screens.deposit.HandleDepositViewModel
import com.example.ibanking_kltn.utils.LoadingScaffold

fun NavGraphBuilder.depositGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.Deposit.name,
        route = AppGraph.DepositGraph.name,
    ) {
        composable(route = Screens.Deposit.name) {
            val depositViewModel: DepositViewModel = hiltViewModel()
            val uiState by depositViewModel.uiState.collectAsState()
            GatewayDeposit(
                uiState = uiState,
                onBackClick = {
                    navController.navigate(Screens.Home.name)
                },
                onEvent = depositViewModel::onEvent
            )
        }
        composable(
            route = "${Screens.HandleDepositResult.name}?vnp_TxnRef={vnp_TxnRef}&vnp_ResponseCode={vnp_ResponseCode}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "ibanking://vnpay-return?vnp_Amount={vnp_Amount}&vnp_BankCode={vnp_BankCode}&vnp_BankTranNo={vnp_BankTranNo}&vnp_CardType={vnp_CardType}&vnp_OrderInfo={vnp_OrderInfo}&vnp_PayDate={vnp_PayDate}&vnp_ResponseCode={vnp_ResponseCode}&vnp_TmnCode={vnp_TmnCode}&vnp_TransactionNo={vnp_TransactionNo}&vnp_TransactionStatus={vnp_TransactionStatus}&vnp_TxnRef={vnp_TxnRef}&vnp_SecureHash={vnp_SecureHash}"
                }),
            arguments = listOf(
                navArgument("vnp_TxnRef") { type = NavType.StringType; nullable = true },
                navArgument("vnp_ResponseCode") {
                    type = NavType.StringType; nullable = true
                })
        ) { backStackEntry ->
            val handleDepositViewModel = hiltViewModel<HandleDepositViewModel>()
            LaunchedEffect(Unit) {
                handleDepositViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is HandleDepositEffect.NavigateToTransactionResult -> {
                            navController.navigate(
                                "${Screens.TransactionResult.name}/${effect.transactionId}"
                            )
                        }

                        is HandleDepositEffect.ShowSnackBar -> {
                            onShowSnackBar(effect.snackBar)
                        }
                    }
                }
            }
            LoadingScaffold(
                isLoading = true
            ) {
                
            }

        }
    }
}