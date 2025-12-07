package com.example.ibanking_kltn.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ibanking_kltn.data.dtos.Service
import com.example.ibanking_kltn.data.dtos.TabNavigation
import com.example.ibanking_kltn.ui.screens.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordSuccessfullyScreen
import com.example.ibanking_kltn.ui.screens.ConfirmPaymentScreen
import com.example.ibanking_kltn.ui.screens.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.HomeScreen
import com.example.ibanking_kltn.ui.screens.PayBillScreen
import com.example.ibanking_kltn.ui.screens.SettingScreen
import com.example.ibanking_kltn.ui.screens.SignInScreen
import com.example.ibanking_kltn.ui.screens.TransferScreen
import com.example.ibanking_kltn.ui.screens.TransferSuccessfullyScreen
import com.example.ibanking_kltn.ui.uistates.SettingUiState
import com.example.ibanking_kltn.ui.viewmodels.AppViewModel
import com.example.ibanking_kltn.ui.viewmodels.AuthViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillViewModel
import com.example.ibanking_kltn.ui.viewmodels.ChangPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.ConfirmViewModel
import com.example.ibanking_kltn.ui.viewmodels.ForgotPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.HomeViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransferViewModel
import com.example.ibanking_kltn.utils.GradientSnackBar
import com.example.ibanking_kltn.utils.NavigationBar
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.removeVietnameseAccents

enum class Screens {
    SignIn, ChangePassword, ChangePasswordSuccess, ForgotPassword, Home,
    TransferSuccess, Transfer, ConfirmPayment,
    Settings, PayBill
}

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    val appViewModel: AppViewModel = hiltViewModel()

    val authViewModel: AuthViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val transferViewModel: TransferViewModel = hiltViewModel()
    val confirmViewModel: ConfirmViewModel = hiltViewModel()
    val payBillViewModel: BillViewModel = hiltViewModel()
    val changePasswordViewModel: ChangPasswordViewModel = hiltViewModel()
    val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()

    var service by remember { mutableStateOf(Service.TRANSFER) }
    var tabNavigation by remember { mutableStateOf(TabNavigation.HOME) }

    val snackBarState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current


    val navigationBar: @Composable () -> Unit = {
        NavigationBar(
            bottomBarHeight = 100.dp,
            currentTab = tabNavigation,
            onNavigateToSettingScreen = {
                navController.navigate(Screens.Settings.name)
            },
            onNavigateToHomeScreen = {
                navController.navigate(Screens.Home.name)
            },
            onNavigateToWalletScreen = {},
            onNavigateToAnalyticsScreen = {}
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        NavHost(
            navController = navController,
            startDestination = Screens.SignIn.name
        ) {
            composable(route = Screens.SignIn.name) {
                val authUiState by authViewModel.uiState.collectAsState()
                SignInScreen(
                    uiState = authUiState,
                    onLoginClick = {
//                    authViewModel.onLoginClick(
//                        onSuccess = {
//                            navController.navigate(Screens.Home.name) {
////                                popUpTo(Screens.SignIn.name) {
////                                    inclusive = true
////                                }
//                            }
//                        }
//                    )
                        navController.navigate(Screens.Home.name)
                        appViewModel.showSnackBarMessage(
                            message = "Đăng nhập thành công",
                            type = SnackBarType.SUCCESS,
                            actionLabel = "Đóng",
                            onAction = {
                                appViewModel.closeSnackBarMessage()
                            }
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

                    onForgotPasswordClick = {
                        navController.navigate(Screens.ForgotPassword.name)
                    },
                    onBiometricClick = {
                        authViewModel.onBiometricClick(
                            fragmentActivity = context as FragmentActivity,
                            onSuccess = {
                                navController.navigate(Screens.Home.name)
                                appViewModel.showSnackBarMessage(
                                    message = "Đăng nhập thành công",
                                    type = SnackBarType.SUCCESS,
                                    actionLabel = "Đóng",
                                    onAction = {
                                        appViewModel.closeSnackBarMessage()
                                    }
                                )
                            },
                            onError = {
                                message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )

                    }
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
                    amount = confirmUiState.amount,
                    toMerchantName = confirmUiState.toMerchantName
                )
            }
            composable(route = Screens.Home.name) {
                val homeUiState by homeViewModel.uiState.collectAsState()
                tabNavigation = TabNavigation.HOME
                LaunchedEffect(Unit) {
//                homeViewModel.init()
//                authViewModel.clearState()
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

                    onNavigateToPayBillScreen = {
                        payBillViewModel.clearState()
                        navController.navigate(Screens.PayBill.name)
                    },
                    navigationBar = { navigationBar() },
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
                        val billData = payBillViewModel.uiState.value
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
                    onChangeBillCode = {
                        payBillViewModel.onChangeBillCode(it)
                    },
                    onChangeAccountType = {
                        payBillViewModel.onChangeAccountType(it)
                    }
                )
            }
            composable(route = Screens.ChangePassword.name) {
                val uiState by changePasswordViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    changePasswordViewModel.clearState()
                }
                ChangePasswordScreen(
                    uiState = uiState,
                    onChangeOldPassword = {
                        changePasswordViewModel.onChangeOldPassword(it)
                    },
                    onChangeNewPassword = { changePasswordViewModel.onChangeNewPassword(it) },

                    onConfirmChangePassword = {
                        changePasswordViewModel.onConfirmChangePassword(
                            onChangeSuccess = {
                                navController.navigate(Screens.ChangePasswordSuccess.name)
                            },
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onBackClick = { navController.popBackStack() },

                    isEnableConfirm = changePasswordViewModel.isEnableChangePassword(),
                    onChangeVisibleOldPassword = {
                        changePasswordViewModel.onChangeVisibilityOldPassword()
                    },
                    onChangeVisibleNewPassword = {
                        changePasswordViewModel.onChangeVisibilityNewPassword()
                    },
                )
            }
            composable(route = Screens.ChangePasswordSuccess.name) {
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
            composable(route = Screens.Settings.name) {
                tabNavigation = TabNavigation.PROFILE
                SettingScreen(
                    uiState = SettingUiState(),
                    onViewProfileClick = {},
                    onChangePasswordClick = {
                        navController.navigate(Screens.ChangePassword.name)
                    },
                    clickBiometric = {},
                    navigationBar = { navigationBar() }

                )
            }
            composable(route = Screens.ForgotPassword.name) {
                val uiState by forgotPasswordViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    forgotPasswordViewModel.clearState()
                }
                ForgotPasswordScreen(
                    uiState = uiState,
                    onFindUsernameClick = {
                        forgotPasswordViewModel.onFindUsernameClick(
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onUsernameChange = {
                        forgotPasswordViewModel.onUsernameChange(it)
                    },
                    onSendOtpClick = {
                        forgotPasswordViewModel.onSendOtpClick(
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onEmailChange = {

                        forgotPasswordViewModel.onEmailChange(it)
                    },
                    onConfirmOtpClick = {
                        forgotPasswordViewModel.onConfirmOtpClick(
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onOtpChange = {
                        forgotPasswordViewModel.onOtpChange(it)
                    },
                    onResetPasswordClick = {
                        forgotPasswordViewModel.onResetPasswordClick(
                            onSuccess = {
                                navController.navigate(Screens.SignIn.name)
                                appViewModel.showSnackBarMessage(
                                    message = "Đổi mật khẩu thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            }, onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onNewPasswordChange = {
                        forgotPasswordViewModel.onNewPasswordChange(it)
                    },
                    onBackToEnterEmailClick = {
                        forgotPasswordViewModel.onBackToEnterEmailClick()
                    },
                    onBackToEnterUsernameClick = {
                        forgotPasswordViewModel.onBackToEnterUsernameClick()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onChangeVisiblePassword = {
                        forgotPasswordViewModel.onChangeVisiblePassword()
                    }
                )
            }

        }
        if (snackBarState.isVisible) {
            GradientSnackBar(
                message = snackBarState.message,
                type = snackBarState.type,
                actionLabel = snackBarState.actionLabel,
                onAction = snackBarState.onAction,
            )
        }

    }

}