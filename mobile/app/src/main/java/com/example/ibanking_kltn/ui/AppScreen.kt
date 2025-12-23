package com.example.ibanking_kltn.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.TabNavigation
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.ui.screens.AllServiceScreen
import com.example.ibanking_kltn.ui.screens.BillDetailScreen
import com.example.ibanking_kltn.ui.screens.BillHistoryScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordSuccessfullyScreen
import com.example.ibanking_kltn.ui.screens.ConfirmPaymentScreen
import com.example.ibanking_kltn.ui.screens.CreateBillScreen
import com.example.ibanking_kltn.ui.screens.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.GatewayDeposit
import com.example.ibanking_kltn.ui.screens.HomeScreen
import com.example.ibanking_kltn.ui.screens.PayBillScreen
import com.example.ibanking_kltn.ui.screens.QRScannerScreen
import com.example.ibanking_kltn.ui.screens.SettingScreen
import com.example.ibanking_kltn.ui.screens.SignInScreen
import com.example.ibanking_kltn.ui.screens.TransactionDetailScreen
import com.example.ibanking_kltn.ui.screens.TransactionHistoryScreen
import com.example.ibanking_kltn.ui.screens.TransactionResultScreen
import com.example.ibanking_kltn.ui.screens.TransferScreen
import com.example.ibanking_kltn.ui.viewmodels.AllServiceViewModel
import com.example.ibanking_kltn.ui.viewmodels.AppViewModel
import com.example.ibanking_kltn.ui.viewmodels.AuthViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillDetailViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillHistoryViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillViewModel
import com.example.ibanking_kltn.ui.viewmodels.ChangPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.ConfirmViewModel
import com.example.ibanking_kltn.ui.viewmodels.CreateBillViewModel
import com.example.ibanking_kltn.ui.viewmodels.DepositViewModel
import com.example.ibanking_kltn.ui.viewmodels.ForgotPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.HomeViewModel
import com.example.ibanking_kltn.ui.viewmodels.QRScannerViewModel
import com.example.ibanking_kltn.ui.viewmodels.SettingViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionDetailViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionHistoryViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionResultViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransferViewModel
import com.example.ibanking_kltn.utils.GradientSnackBar
import com.example.ibanking_kltn.utils.NavigationBar
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.removeVietnameseAccents

enum class Screens {
    SignIn, ChangePassword, ChangePasswordSuccess, ForgotPassword,

    Home,
    TransactionResult, Transfer, ConfirmPayment,
    Settings,
    PayBill, CreateBill, BillHistory, BillDetail,
    TransactionHistory, TransactionHistoryDetail,
    Deposit, HandleDepositResult,
    QRScanner,
    AllService,
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
    val depositViewModel: DepositViewModel = hiltViewModel()
    val transactionResultViewModel: TransactionResultViewModel = hiltViewModel()
    val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
    val billHistoryViewModel: BillHistoryViewModel = hiltViewModel()
    val billDetailViewModel: BillDetailViewModel = hiltViewModel()
    val transactionHistoryViewModel: TransactionHistoryViewModel = hiltViewModel()
    val transactionDetailViewModel: TransactionDetailViewModel = hiltViewModel()
    val allServiceViewModel: AllServiceViewModel = hiltViewModel()
    val createBillViewModel: CreateBillViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()

    var service by remember { mutableStateOf(ServiceType.TRANSFER) }
    var tabNavigation by remember { mutableStateOf(TabNavigation.HOME) }

    val snackBarState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val onError: (String) -> Unit = { message ->
        appViewModel.showSnackBarMessage(
            message = message, type = SnackBarType.ERROR
        )
    }

    val navigationBar: @Composable () -> Unit = {
        NavigationBar(
            bottomBarHeight = 100.dp,
            currentTab = tabNavigation,
            onNavigateToSettingScreen = {
                navController.navigate(Screens.Settings.name){
                    launchSingleTop=true
                }
            },
            onNavigateToHomeScreen = {
                navController.navigate(Screens.Home.name){
                    launchSingleTop=true
                }
            },
            onNavigateToTransactionHistoryScreen = {
                navController.navigate(Screens.TransactionHistory.name){
                    launchSingleTop=true
                }
            },
            onNavigateToAnalyticsScreen = {},
            onNavigateToQRScanner = {
                navController.navigate(Screens.QRScanner.name){
                    launchSingleTop=true
                }
            })
    }
    val navigator = mapOf(
        ServiceCategory.MONEY_TRANSFER.name to {
            appViewModel.addRecentService(ServiceCategory.MONEY_TRANSFER)
            transferViewModel.clearState()
            navController.navigate(Screens.Transfer.name)
        },
        ServiceCategory.DEPOSIT.name to {
            appViewModel.addRecentService(ServiceCategory.DEPOSIT)
            navController.navigate(Screens.Deposit.name)
        },
        ServiceCategory.BILL_PAYMENT.name to {
            appViewModel.addRecentService(ServiceCategory.BILL_PAYMENT)
            payBillViewModel.init()
            navController.navigate(Screens.PayBill.name)
        },

        ServiceCategory.BILL_HISTORY.name to {
            appViewModel.addRecentService(ServiceCategory.BILL_HISTORY)
            billHistoryViewModel.clearState()
            navController.navigate(Screens.BillHistory.name)
        },
        ServiceCategory.PAY_LATER.name to {
            appViewModel.addRecentService(ServiceCategory.PAY_LATER)
            //TODO
            navController.navigate(Screens.BillHistory.name)
        },
        ServiceCategory.AIR_PLANE.name to {
            appViewModel.addRecentService(ServiceCategory.AIR_PLANE)
            //TODO
            navController.navigate(Screens.BillHistory.name)
        },
        ServiceCategory.ANALYTIC.name to {
            appViewModel.addRecentService(ServiceCategory.ANALYTIC)
            //TODO
            navController.navigate(Screens.BillHistory.name)
        },
        ServiceCategory.BILL_CREATE.name to {
            appViewModel.addRecentService(ServiceCategory.BILL_CREATE)
            createBillViewModel.init()
            navController.navigate(Screens.CreateBill.name)
        },
        ServiceCategory.HOTEL.name to {
            appViewModel.addRecentService(ServiceCategory.HOTEL)
            //TODO
            navController.navigate(Screens.BillHistory.name)
        }

    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        NavHost(
            navController = navController, startDestination = Screens.SignIn.name
        ) {
            composable(route = Screens.SignIn.name) {
                val authUiState by authViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    authViewModel.clearState()
                    authViewModel.loadLastLoginUser()
                }
                SignInScreen(
                    uiState = authUiState,
                    onLoginClick = {

                        //api
                        authViewModel.onLoginClick(
                            onSuccess = {
                                homeViewModel.init()
                                navController.navigate(Screens.Home.name) {
                                    popUpTo(Screens.SignIn.name) {
                                        inclusive = true
                                    }
                                }
                                appViewModel.showSnackBarMessage(
                                    message = "Đăng nhập thành công",
                                    type = SnackBarType.SUCCESS,
                                    actionLabel = "Đóng",
                                    onAction = {
                                        appViewModel.closeSnackBarMessage()
                                    })
                            },
                            onError = { message ->
                                onError(message)
                            },
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
                                    })
                            },
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message, type = SnackBarType.ERROR
                                )
                            })

                    },
                    onDeleteLastLoginUser = {
                        authViewModel.onDeleteLastLoginUser()
                    }
                )

            }
            composable(route = Screens.TransactionResult.name) {
                val transactionResultUiState by transactionResultViewModel.uiState.collectAsState()
                TransactionResultScreen(onBackToHomeClick = {
                    navController.navigate(Screens.Home.name) {
                        popUpTo(Screens.Home.name) {
                            inclusive = true
                        }
                    }
                }, uiState = transactionResultUiState, onContactClick = {})
            }
            composable(route = Screens.Home.name) {
                val homeUiState by homeViewModel.uiState.collectAsState()
                tabNavigation = TabNavigation.HOME
                LaunchedEffect(Unit) {

                    homeViewModel.init()
                    authViewModel.clearState()
                }
                HomeScreen(
                    homeUiState = homeUiState, onChangeVisibleBalance = {
                        homeViewModel.onChangeVisibleBalance()
                    },

                    navigationBar = { navigationBar() },
                    onNavigateTo = navigator,
                    onNavigateServiceList = {
                        allServiceViewModel.init()
                        navController.navigate(Screens.AllService.name)
                    }
                )
            }
            composable(route = Screens.Transfer.name) {
                val transferUiState by transferViewModel.uiState.collectAsState()
                service = ServiceType.TRANSFER
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
                    onConfirmClick = {
                        confirmViewModel.onConfirmClick(onSentOtp = {
                            appViewModel.showSnackBarMessage(
                                message = "Đã gửi mã OTP đến email của bạn",
                                type = SnackBarType.INFO,

                                )
                        }, onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
                    },
                    onOtpChange = {
                        confirmViewModel.onOtpChange(otp = it, onSuccess = {
                            transactionResultViewModel.init(
                                status = TransactionStatus.COMPLETED,
                                service = service.name,
                                amount = confirmUiState.amount,
                                toMerchantName = confirmUiState.toMerchantName
                            )
                            navController.navigate(Screens.TransactionResult.name)
                        }, onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
                    },
                    onOtpDismiss = {
                        confirmViewModel.onOtpDismiss()
                    },
                )
            }
            composable(route = Screens.PayBill.name) { backStackEntry ->

                val uiState by payBillViewModel.uiState.collectAsState()
                service = ServiceType.BILL_PAYMENT
                LaunchedEffect(Unit) {
                    payBillViewModel.init()
                }

                PayBillScreen(uiState = uiState, onBackClick = {
                    navController.popBackStack()
                }, onCheckingBill = {
                    payBillViewModel.onCheckingBill()
                }, onConfirmPayBill = {
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

                }, onChangeBillCode = {
                    payBillViewModel.onChangeBillCode(it)
                }, onChangeAccountType = {
                    payBillViewModel.onChangeAccountType(it)
                })
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
                        changePasswordViewModel.onConfirmChangePassword(onChangeSuccess = {
                            navController.navigate(Screens.ChangePasswordSuccess.name)
                        }, onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
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
                    })
            }
            composable(route = Screens.Settings.name) {
                tabNavigation = TabNavigation.PROFILE
                val uiState by settingViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    settingViewModel.init()
                }
                SettingScreen(
                    uiState = uiState,
                    onViewProfileClick = {
                        //TODO
                    },
                    onChangePasswordClick = {
                        navController.navigate(Screens.ChangePassword.name)
                    },
                    onClickBiometric = {
                        settingViewModel.onSwitchBiometric(
                            onSuccess = { isEnable ->
                                val message = if (isEnable) {
                                    "Bật đăng nhập bằng sinh trắc học thành công"
                                } else {
                                    "Tắt đăng nhập bằng sinh trắc học thành công"
                                }
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.INFO
                                )
                            },
                            onError = onError
                        )
                    },
                    onLogout = {
                        authViewModel.onLogout()
                        navController.navigate(Screens.SignIn.name) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    },
                    navigationBar = { navigationBar() },
                    onChangePassword = {
                        settingViewModel.onChangePasswordConfirm(it)
                    },
                    onError = onError

                )
            }
            composable(route = Screens.ForgotPassword.name) {
                val uiState by forgotPasswordViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    forgotPasswordViewModel.clearState()
                }
                ForgotPasswordScreen(uiState = uiState, onFindUsernameClick = {
                    forgotPasswordViewModel.onFindUsernameClick(
                        onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
                }, onUsernameChange = {
                    forgotPasswordViewModel.onUsernameChange(it)
                }, onSendOtpClick = {
                    forgotPasswordViewModel.onSendOtpClick(
                        onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
                }, onEmailChange = {

                    forgotPasswordViewModel.onEmailChange(it)
                }, onConfirmOtpClick = {
                    forgotPasswordViewModel.onConfirmOtpClick(
                        onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.ERROR
                            )
                        })
                }, onOtpChange = {
                    forgotPasswordViewModel.onOtpChange(it)
                }, onResetPasswordClick = {
                    forgotPasswordViewModel.onResetPasswordClick(onSuccess = {
                        navController.navigate(Screens.SignIn.name)
                        appViewModel.showSnackBarMessage(
                            message = "Đổi mật khẩu thành công", type = SnackBarType.SUCCESS
                        )
                    }, onError = { message ->
                        appViewModel.showSnackBarMessage(
                            message = message, type = SnackBarType.ERROR
                        )
                    })
                }, onNewPasswordChange = {
                    forgotPasswordViewModel.onNewPasswordChange(it)
                }, onBackToEnterEmailClick = {
                    forgotPasswordViewModel.onBackToEnterEmailClick()
                }, onBackToEnterUsernameClick = {
                    forgotPasswordViewModel.onBackToEnterUsernameClick()
                }, onBackClick = {
                    navController.popBackStack()
                }, onChangeVisiblePassword = {
                    forgotPasswordViewModel.onChangeVisiblePassword()
                })
            }
            composable(route = Screens.Deposit.name) {
                val uiState by depositViewModel.uiState.collectAsState()
                GatewayDeposit(
                    onBackClick = {
                        navController.navigate(Screens.Home.name)
                    }, onContinuePayment = {
                        depositViewModel.onContinuePayment(
                            context = context, onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message, type = SnackBarType.ERROR
                                )
                            })
                    }, uiState = uiState, onChangeAmount = {
                        depositViewModel.onAmountChange(it)
                    }, isEnableContinue = depositViewModel.isEnableContinuePayment()
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

                LaunchedEffect(Unit) {
                    val txnRef = backStackEntry.arguments?.getString("vnp_TxnRef") ?: ""
                    val responseCode = backStackEntry.arguments?.getString("vnp_ResponseCode") ?: ""

                    depositViewModel.handleVNPayReturn(
                        vnp_ResponseCode = responseCode,
                        vnp_TxnRef = txnRef,
                        onError = {
                            appViewModel.showSnackBarMessage(
                                message = it, type = SnackBarType.ERROR
                            )
                        },
                        onNavigateToTransactionResult = {
                            transactionResultViewModel.init(
                                status = it.status,
                                service = it.service,
                                amount = it.amount,
                                toMerchantName = homeViewModel.uiState.value.myWallet?.merchantName
                                    ?: "UNKNOWN"
                            )
                            navController.navigate(Screens.TransactionResult.name)
                        })
                }


            }
            composable(route = Screens.QRScanner.name) { backStackEntry ->
                //TODO
                val uiState by qrScannerViewModel.uiState.collectAsState()
                QRScannerScreen(
                    uiState = uiState,
                    detecting = {
                        qrScannerViewModel.onDetecting(qrCode = it, onBillDetecting = { payload ->
                            payBillViewModel.init()
                            navController.navigate(Screens.PayBill.name) {
                                launchSingleTop = true
                                popUpTo(Screens.Home.name)
                            }
                            payBillViewModel.onChangeBillCode(payload.billCode)
                            payBillViewModel.onCheckingBill()
                        }, onTransferDetecting = { payload ->
                            transferViewModel.init()
                            transferViewModel.onToWalletNumberChange(payload.toWalletNumber)
                            payload.amount?.let { amount ->
                                transferViewModel.onAmountChange(amount.toString())
                            }
                            navController.navigate(Screens.Transfer.name) {
                                launchSingleTop = true
                                popUpTo(Screens.Home.name)

                            }
                            transferViewModel.onDoneWalletNumber()

                        }, onError = { message ->
                            appViewModel.showSnackBarMessage(
                                message = message, type = SnackBarType.INFO
                            )
                        })
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screens.CreateBill.name) { backStackEntry ->
                val uiState by createBillViewModel.uiState.collectAsState()
                CreateBillScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    isEnableContinue = createBillViewModel.isEnableCreateBill(),
                    onContinueClick = {
                        createBillViewModel.onContinueClick(
                            onSucess = {
                                billDetailViewModel.init(bill = it)
                                navController.navigate(Screens.BillDetail.name) {
                                    launchSingleTop = true
                                    popUpTo(Screens.Home.name)
                                }
                            },
                            onError = onError
                        )
                    },
                    onChangeAmount = {
                        createBillViewModel.onAmountChange(it)
                    },
                    onChangeDescription = {
                        createBillViewModel.onDescriptionChange(it)
                    },
                    onExpenseTypeChange = {
                        createBillViewModel.onExpenseTypeChange(it)
                    },
                    onDateChange = {
                        createBillViewModel.onExpiryDateChange(it)
                    }
                )
            }
            composable(route = Screens.BillHistory.name) { backStackEntry ->
                val uiState by billHistoryViewModel.uiState.collectAsState()
                val bills = billHistoryViewModel.billPager.collectAsLazyPagingItems()
                BillHistoryScreen(uiState = uiState, bills = bills, onClickFilter = {
                    billHistoryViewModel.onClickFilter()
                }, onResetAll = {
                    billHistoryViewModel.onResetAll(
                        onError = onError
                    )
                }, onApply = { selectedStatus, selectedSort ->
                    billHistoryViewModel.onApply(
                        onError = onError,
                        selectedStatus = selectedStatus,
                        selectedSort = selectedSort,
                    )
                }, onErrorLoading = {
                    appViewModel.showSnackBarMessage(
                        message = it, type = SnackBarType.ERROR
                    )
                }, onViewDetail = { bill ->
                    billDetailViewModel.init(bill = bill)
                    navController.navigate(Screens.BillDetail.name)
                }, onBackClick = {
                    navController.popBackStack()
                })
            }
            composable(route = Screens.BillDetail.name) { backStackEntry ->
                val bill by billDetailViewModel.uiState.collectAsState()
                BillDetailScreen(bill = bill, onSavedSuccess = {
                    appViewModel.showSnackBarMessage(
                        message = "Lưu hóa đơn thành công", type = SnackBarType.SUCCESS
                    )
                }, onBackClick = {
                    navController.popBackStack()
                })
            }
            composable(route = Screens.TransactionHistory.name) { backStackEntry ->
                tabNavigation = TabNavigation.HISTORY
                val uiState by transactionHistoryViewModel.uiState.collectAsState()
                val transactions =
                    transactionHistoryViewModel.transactionHistoryPager.collectAsLazyPagingItems()
                TransactionHistoryScreen(
                    uiState = uiState,
                    transactions = transactions,
                    onClickFilter = {
                        transactionHistoryViewModel.onClickFilter()
                    },
                    onResetAll = {
                        transactionHistoryViewModel.onResetAll(
                            onError = onError
                        )
                    },
                    onApply = { selectedStatus, selectedService, selectedAccountType, selectedSort ->
                        transactionHistoryViewModel.onApply(
                            onError = onError,
                            selectedStatus = selectedStatus,
                            selectedSort = selectedSort,
                            selectedService = selectedService,
                            selectedAccountType = selectedAccountType
                        )
                    },
                    onErrorLoading = {
                        appViewModel.showSnackBarMessage(
                            message = it,
                            type = SnackBarType.ERROR
                        )
                    },
                    onViewDetail = { transaction ->
                        transactionDetailViewModel.init(transaction = transaction)
                        navController.navigate(Screens.TransactionHistoryDetail.name)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screens.TransactionHistoryDetail.name) { backStackEntry ->
                val transaction by transactionDetailViewModel.uiState.collectAsState()
                TransactionDetailScreen(
                    transaction = transaction,
                    onBackClick = {
                        navController.popBackStack()
                    })
            }
            composable(route = Screens.AllService.name) { backStackEntry ->
                val uiState by allServiceViewModel.uiState.collectAsState()
                AllServiceScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onChangeToModifyState = {
                        allServiceViewModel.onChangeModifyFavorite()
                    },
                    onSaveFavoriteServices = {
                        allServiceViewModel.onSaveFavoriteServices(it)
                    },
                    navigator = navigator
                )
            }
        }



        if (snackBarState.isVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                GradientSnackBar(
                    message = snackBarState.message,
                    type = snackBarState.type,
                    actionLabel = snackBarState.actionLabel,
                    onAction = snackBarState.onAction,
                )
            }
        }

    }

}