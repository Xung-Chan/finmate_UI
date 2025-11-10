package com.example.ibanking_kltn.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibanking_kltn.data.dtos.Service
import com.example.ibanking_kltn.ui.screens.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.ConfirmPaymentScreen
import com.example.ibanking_kltn.ui.screens.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.HomeScreen
import com.example.ibanking_kltn.ui.screens.PayBillScreen
import com.example.ibanking_kltn.ui.screens.SettingScreen
import com.example.ibanking_kltn.ui.screens.SignInScreen
import com.example.ibanking_kltn.ui.screens.TransferScreen
import com.example.ibanking_kltn.ui.screens.TransferSuccessfullyScreen
import com.example.ibanking_kltn.ui.viewmodels.AuthViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillViewModel
import com.example.ibanking_kltn.ui.viewmodels.ConfirmViewModel
import com.example.ibanking_kltn.ui.viewmodels.HomeViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransferViewModel
import com.example.ibanking_kltn.utils.removeVietnameseAccents

enum class Screens {
    SignIn, ChangePassword, ForgotPassword, Home,
    TransferSuccess, Transfer, ConfirmPayment,
    Settings, PayBill
}

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    val context: Context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val transferViewModel: TransferViewModel = hiltViewModel()
    val confirmViewModel: ConfirmViewModel = hiltViewModel()
    val payBillViewModel: BillViewModel = hiltViewModel()

    var service by remember { mutableStateOf(Service.TRANSFER) }
    NavHost(
        navController = navController,
        startDestination = Screens.SignIn.name
    ) {
        composable(route = Screens.SignIn.name) {
            val authUiState by authViewModel.uiState.collectAsState()
            SignInScreen(
                uiState = authUiState,
                onLoginClick = {
                    authViewModel.onLoginClick(
                        context = context,
                        navControler = navController,
                    )
                },
                onEmailChange = { it -> authViewModel.onEmailChange(it) },
                onPasswordChange = { it -> authViewModel.onPasswordChange(it) },
                onChangeVisiblePassword = {
                    authViewModel.onChangeVisiblePassword()
                },
                checkEnableLogin = {
                    authViewModel.checkEnableLogin()
                },
            )
        }
        composable(route = Screens.TransferSuccess.name) {
            val confirmUiState by confirmViewModel.uiState.collectAsState()
            TransferSuccessfullyScreen(
                onBackToHomeClick = {
                    navController.navigate(Screens.Home.name) {
                        popUpTo(Screens.Home.name) {
                            inclusive = true
                        }
                    }
                },
                amount = confirmUiState.amount ,
                toMerchantName = confirmUiState.toMerchantName
            )
        }
        composable(route = Screens.Home.name) {
            val homeUiState by homeViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                homeViewModel.init()
                authViewModel.clearState()
            }
            HomeScreen(
                homeUiState = homeUiState,
                onChangeVisibleBalance = {
                    homeViewModel.onChangeVisibleBalance()
                },
                onNavigateToTransferScreen = {
                    transferViewModel.clearState()
                    navController.navigate(Screens.Transfer.name)
                },

                onNavigateToSettingScreen = {
                    navController.navigate(Screens.Settings.name)
                },
                onNavigateToPayBillScreen = {
                    payBillViewModel.clearState()
                    navController.navigate(Screens.PayBill.name)
                }
            )
        }
        composable(route = Screens.Transfer.name) {
            val transferUiState by transferViewModel.uiState.collectAsState()
            service = Service.TRANSFER
            LaunchedEffect(Unit) {
                transferViewModel.init()
            }
            TransferScreen(
                uiState = transferUiState,
                onDoneWalletNumber = {
                    transferViewModel.onDoneWalletNumber()
                },
                onAccountTypeChange = { transferViewModel.onAccountTypeChange(it) },
                onExpenseTypeChange = {
                    transferViewModel.onExpenseTypeChange(it)
                },
                onChangeReceiveWalletNumber = { transferViewModel.onToWalletNumberChange(it) },
                onChangeAmount = { transferViewModel.onAmountChange(it) },
                onChangeDescription = { transferViewModel.onContentChange(it) },

                onBackClick = { navController.popBackStack() },
                onConfirmClick = {
                    confirmViewModel.clearState()
                    val transferData = transferViewModel.uiState.value
                    confirmViewModel.init(
                        accountType = transferData.accountType,
                        amount = transferData.amount,
                        toWalletNumber = transferData.toWalletNumber,
                        description = if (transferData.description.isNotEmpty()) removeVietnameseAccents(
                            transferData.description
                        ) else "Chuyen tien den ${transferData.toMerchantName}",
                        toMerchantName = transferData.toMerchantName,
                        expenseType = transferData.expenseType,
                        service = service
                    )
                    navController.navigate(Screens.ConfirmPayment.name)

                },
                isEnableContinue = transferViewModel.isEnableContinue()

            )
        }
        composable(route = Screens.ConfirmPayment.name) {
            val confirmUiState by confirmViewModel.uiState.collectAsState()

            ConfirmPaymentScreen(
                uiState = confirmUiState,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { confirmViewModel.onConfirmClick() },
                onOtpChange = {
                    confirmViewModel.onOtpChange(it, onSuccess = {
                        navController.navigate(Screens.TransferSuccess.name)
                    })
                },
                onOtpDismiss = {
                    confirmViewModel.onOtpDismiss()
                },
            )
        }


        composable(route = Screens.PayBill.name) {
            val uiState by payBillViewModel.uiState.collectAsState()
            service = Service.PAY_BILL
            LaunchedEffect(Unit) {
                payBillViewModel.init()
            }

            PayBillScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onCheckingBill = {
                    payBillViewModel.onCheckingBill()
                },
                onConfirmPayBill = {
                    confirmViewModel.clearState()
                    val billData=payBillViewModel.uiState.value
                    confirmViewModel.init(
                        accountType = billData.accountType,
                        amount = billData.amount,
                        toWalletNumber = billData.toWalletNumber,
                        description = if (billData.description.isNotEmpty()) removeVietnameseAccents(
                            billData.description
                        ) else "Chuyen tien den ${billData.toMerchantName}",
                        toMerchantName = billData.toMerchantName,
                        billCode = billData.billCode,
                        service = service
                    )
                    navController.navigate(Screens.ConfirmPayment.name)

                },
                onChangeBillCode ={
                    payBillViewModel.onChangeBillCode(it)
                },
                onChangeAccountType = {
                    payBillViewModel.onChangeAccountType(it)
                }
            )
        }
        composable(route = Screens.Settings.name) {
            SettingScreen()
        }
        composable(route = Screens.ForgotPassword.name) {
            ForgotPasswordScreen()
        }
        composable(route = Screens.ChangePassword.name) {
            ChangePasswordScreen()
        }

    }
}