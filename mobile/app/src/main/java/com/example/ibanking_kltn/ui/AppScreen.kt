package com.example.ibanking_kltn.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.ibanking_kltn.data.dtos.BillPayload
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationStatus
import com.example.ibanking_kltn.data.dtos.PayLaterApplicationType
import com.example.ibanking_kltn.data.dtos.SavedReceiver
import com.example.ibanking_kltn.data.dtos.ServiceCategory
import com.example.ibanking_kltn.data.dtos.ServiceType
import com.example.ibanking_kltn.data.dtos.TabNavigation
import com.example.ibanking_kltn.data.dtos.TransactionStatus
import com.example.ibanking_kltn.data.dtos.TransferPayload
import com.example.ibanking_kltn.ui.screens.AllServiceScreen
import com.example.ibanking_kltn.ui.screens.AnalyticScreen
import com.example.ibanking_kltn.ui.screens.BillDetailScreen
import com.example.ibanking_kltn.ui.screens.BillHistoryScreen
import com.example.ibanking_kltn.ui.screens.BillingCycleScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordScreen
import com.example.ibanking_kltn.ui.screens.ChangePasswordSuccessfullyScreen
import com.example.ibanking_kltn.ui.screens.ConfirmPaymentScreen
import com.example.ibanking_kltn.ui.screens.CreateBillScreen
import com.example.ibanking_kltn.ui.screens.CreateVerificationRequestScreen
import com.example.ibanking_kltn.ui.screens.ForgotPasswordScreen
import com.example.ibanking_kltn.ui.screens.GatewayDeposit
import com.example.ibanking_kltn.ui.screens.HomeScreen
import com.example.ibanking_kltn.ui.screens.MyProfileScreen
import com.example.ibanking_kltn.ui.screens.PayBillScreen
import com.example.ibanking_kltn.ui.screens.PayLaterApplicationHistoryScreen
import com.example.ibanking_kltn.ui.screens.PayLaterApplicationScreen
import com.example.ibanking_kltn.ui.screens.PayLaterScreen
import com.example.ibanking_kltn.ui.screens.QRScannerScreen
import com.example.ibanking_kltn.ui.screens.SavedReceiverScreen
import com.example.ibanking_kltn.ui.screens.SettingScreen
import com.example.ibanking_kltn.ui.screens.SignInScreen
import com.example.ibanking_kltn.ui.screens.TermsAndConditionsScreen
import com.example.ibanking_kltn.ui.screens.TransactionDetailScreen
import com.example.ibanking_kltn.ui.screens.TransactionHistoryScreen
import com.example.ibanking_kltn.ui.screens.TransactionResultScreen
import com.example.ibanking_kltn.ui.screens.TransferScreen
import com.example.ibanking_kltn.ui.viewmodels.AllServiceViewModel
import com.example.ibanking_kltn.ui.viewmodels.AnalyticViewModel
import com.example.ibanking_kltn.ui.viewmodels.AppViewModel
import com.example.ibanking_kltn.ui.viewmodels.AuthViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillDetailViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillHistoryViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillViewModel
import com.example.ibanking_kltn.ui.viewmodels.BillingCycleViewModel
import com.example.ibanking_kltn.ui.viewmodels.ChangPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.ConfirmViewModel
import com.example.ibanking_kltn.ui.viewmodels.CreateBillViewModel
import com.example.ibanking_kltn.ui.viewmodels.CreateVerificationRequestViewModel
import com.example.ibanking_kltn.ui.viewmodels.DepositViewModel
import com.example.ibanking_kltn.ui.viewmodels.ForgotPasswordViewModel
import com.example.ibanking_kltn.ui.viewmodels.HomeViewModel
import com.example.ibanking_kltn.ui.viewmodels.MyProfileViewModel
import com.example.ibanking_kltn.ui.viewmodels.PayLaterApplicationHistoryViewModel
import com.example.ibanking_kltn.ui.viewmodels.PayLaterApplicationViewModel
import com.example.ibanking_kltn.ui.viewmodels.PayLaterViewModel
import com.example.ibanking_kltn.ui.viewmodels.QRScannerViewModel
import com.example.ibanking_kltn.ui.viewmodels.SavedReceiverViewModel
import com.example.ibanking_kltn.ui.viewmodels.SettingViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionDetailViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionHistoryViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransactionResultViewModel
import com.example.ibanking_kltn.ui.viewmodels.TransferViewModel
import com.example.ibanking_kltn.utils.GradientSnackBar
import com.example.ibanking_kltn.utils.NavigationBar
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.removeVietnameseAccents
import java.time.LocalDate

enum class Screens {
    SignIn, ChangePassword, ChangePasswordSuccess, ForgotPassword,

    Home, Settings, Analytic,
    TransactionResult, Transfer, ConfirmPayment,
    MyProfile, TermAndConditions,
    PayBill, CreateBill, BillHistory, BillDetail,
    TransactionHistory, TransactionHistoryDetail,
    Deposit, HandleDepositResult,
    QRScanner,
    AllService,
    VerificationRequest,
    SavedReceiver,
    PayLater, PayLaterApplication, PayLaterApplicationHistory, BillingCycle
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
    val createVerificationRequestViewModel: CreateVerificationRequestViewModel = hiltViewModel()
    val myProfileViewModel: MyProfileViewModel = hiltViewModel()
    val savedReceiverViewModel: SavedReceiverViewModel = hiltViewModel()
    val payLaterViewModel: PayLaterViewModel = hiltViewModel()
    val payLaterApplicationViewModel: PayLaterApplicationViewModel = hiltViewModel()
    val payLaterApplicationHistoryViewModel: PayLaterApplicationHistoryViewModel = hiltViewModel()
    val analyticViewModel: AnalyticViewModel = hiltViewModel()
    val billingCycleViewModel: BillingCycleViewModel = hiltViewModel()

    var service by remember { mutableStateOf(ServiceType.TRANSFER) }
    var tabNavigation by remember { mutableStateOf(TabNavigation.HOME) }

    val snackBarState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val onError: (String) -> Unit = { message ->
        appViewModel.showSnackBarMessage(
            message = message, type = SnackBarType.ERROR
        )
    }

    var lastBackPressed by remember { mutableLongStateOf(0L) }

    var isAtRoot by remember {
        mutableStateOf(true)
    }

    BackHandler(enabled = isAtRoot) {
        val now = System.currentTimeMillis()
        if (now - lastBackPressed < 2000) {
            (context as Activity).finish()
        } else {
            lastBackPressed = now
            Toast
                .makeText(context, "Vuốt back lần nữa để thoát", Toast.LENGTH_SHORT)
                .show()
        }
    }


    val navigationBar: @Composable () -> Unit = {
        NavigationBar(
            bottomBarHeight = 100.dp,
            currentTab = tabNavigation,
            onNavigateToSettingScreen = {
                navController.navigate(Screens.Settings.name) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            onNavigateToHomeScreen = {
                homeViewModel.loadWalletInfo(
                    onError = onError
                )
                homeViewModel.loadFavoriteAndRecentServices()
                navController.navigate(Screens.Home.name) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            onNavigateToTransactionHistoryScreen = {
                navController.navigate(Screens.TransactionHistory.name) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            onNavigateToAnalyticsScreen = {
                analyticViewModel.loadDistributionStatistic(
                    onError = onError
                )
                analyticViewModel.loadTrendStatistic(
                    onError = onError
                )
                navController.navigate(Screens.Analytic.name) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }

            },
            onNavigateToQRScanner = {
                navController.navigate(Screens.QRScanner.name) {
                    launchSingleTop = true
                }
            })
    }
    val navigator = mapOf(
        ServiceCategory.MONEY_TRANSFER.name to {
            appViewModel.addRecentService(ServiceCategory.MONEY_TRANSFER)
            transferViewModel.init(
                onError = onError
            )
            savedReceiverViewModel.loadSavedReceivers()
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
            payLaterViewModel.init(
                onError = onError
            )
            navController.navigate(Screens.PayLater.name)
        },
        ServiceCategory.AIR_PLANE.name to {
            appViewModel.addRecentService(ServiceCategory.AIR_PLANE)
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
        },
        ServiceCategory.VERIFICATION_REQUEST.name to {
            appViewModel.addRecentService(ServiceCategory.VERIFICATION_REQUEST)
            navController.navigate(Screens.VerificationRequest.name)
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
                                homeViewModel.init(
                                    onError
                                )
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
                                homeViewModel.init(
                                    onError
                                )
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
                    homeViewModel.loadWalletInfo(
                        onError = onError
                    )
                    navController.navigate(Screens.Home.name) {
                        popUpTo(Screens.Home.name) {
                            inclusive = true
                        }
                    }
                }, uiState = transactionResultUiState, onContactClick = {})
            }
            composable(route = Screens.Home.name) {
                isAtRoot = true
                val homeUiState by homeViewModel.uiState.collectAsState()
                tabNavigation = TabNavigation.HOME
                LaunchedEffect(Unit) {

                    homeViewModel.loadFavoriteAndRecentServices()
                }
                HomeScreen(
                    homeUiState = homeUiState, onChangeVisibleBalance = {
                        homeViewModel.onChangeVisibleBalance(
                            onError = onError
                        )
                    },

                    navigationBar = { navigationBar() },
                    onNavigateTo = navigator,
                    onNavigateServiceList = {
                        allServiceViewModel.init()
                        navController.navigate(Screens.AllService.name)
                    },
                    onRetry = {
                        homeViewModel.init(
                            onError
                        )
                    }
                )
            }
            composable(route = Screens.Transfer.name) {
                val transferUiState by transferViewModel.uiState.collectAsState()
                val savedReceiverUiState by savedReceiverViewModel.uiState.collectAsState()
                service = ServiceType.TRANSFER
                TransferScreen(
                    uiState = transferUiState,
                    onDoneWalletNumber = {
                        transferViewModel.onDoneWalletNumber(
                            onError = onError
                        )
                    },
                    onExpenseTypeChange = {
                        transferViewModel.onExpenseTypeChange(it)
                    },
                    onChangeReceiveWalletNumber = { transferViewModel.onToWalletNumberChange(it) },
                    onChangeAmount = { transferViewModel.onAmountChange(it) },
                    onChangeDescription = { transferViewModel.onContentChange(it) },

                    onBackClick = {
                        navController.popBackStack()
                    },
                    onConfirmClick = {
                        confirmViewModel.clearState()
                        val transferData = transferViewModel.uiState.value
                        confirmViewModel.init(
                            amount = transferData.amount,
                            toWalletNumber = transferData.toWalletNumber,
                            description =removeVietnameseAccents(transferData.description.ifEmpty { "Chuyen tien den ${transferData.toMerchantName}" }),
                            toMerchantName = transferData.toMerchantName,
                            expenseType = transferData.expenseType,
                            isVerified = transferData.isVerified,
                            service = service
                        )
                        if (transferData.isSaveReceiver) {
                            savedReceiverViewModel.onSaveReceiver(
                                SavedReceiver(
                                    memorableName = transferData.toMerchantName,
                                    toWalletNumber = transferData.toWalletNumber,
                                    toMerchantName = transferData.toMerchantName,
                                )
                            )
                        }

                        navController.navigate(Screens.ConfirmPayment.name)

                    },
                    isEnableContinue = transferViewModel.isEnableContinue(),
                    savedReceivers = savedReceiverUiState.savedReceivers,
                    onSelectSavedReceiver = {
                        transferViewModel.onSelectSavedReceiver(it)

                    },
                    onChangeSaveReceiver = {
                        transferViewModel.onChangeSaveReceiver()
                    }

                )
            }
            composable(route = Screens.ConfirmPayment.name) {
                val confirmUiState by confirmViewModel.uiState.collectAsState()

                ConfirmPaymentScreen(
                    uiState = confirmUiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onConfirmClick = {
                        confirmViewModel.onConfirmClick(
                            onSentOtp = {
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
                    onAccountTypeChange = {
                        confirmViewModel.onSelectAccountType(it)
                    },
                )
            }
            composable(route = Screens.PayBill.name) { backStackEntry ->

                val uiState by payBillViewModel.uiState.collectAsState()
                service = ServiceType.BILL_PAYMENT
//                LaunchedEffect(Unit) {
//                    payBillViewModel.init()
//                }

                PayBillScreen(
                    uiState = uiState, onBackClick = {
                        navController.popBackStack()
                    }, onCheckingBill = {
                        payBillViewModel.onCheckingBill()
                    }, onConfirmPayBill = {
                        confirmViewModel.clearState()
                        val billData = payBillViewModel.uiState.value
                        confirmViewModel.init(
                            amount = billData.amount,
                            toWalletNumber = billData.toWalletNumber,
                            description =removeVietnameseAccents(billData.description.ifEmpty { "Chuyen tien den ${billData.toMerchantName}" }),
                            toMerchantName = billData.toMerchantName,
                            billCode = billData.billCode,
                            isVerified = true,
                            service = service
                        )
                        navController.navigate(Screens.ConfirmPayment.name)

                    }, onChangeBillCode = {
                        payBillViewModel.onChangeBillCode(it)
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
                isAtRoot = true
                tabNavigation = TabNavigation.PROFILE
                val uiState by settingViewModel.uiState.collectAsState()
                val homeUiState by homeViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    settingViewModel.init()
                }
                SettingScreen(
                    uiState = uiState,
                    fullName= homeUiState.myProfile?.fullName?:"",
                    avatarUrl = homeUiState.myProfile?.avatarUrl,
                    onViewProfileClick = {
                        myProfileViewModel.init(onError = onError)
                        navController.navigate(Screens.MyProfile.name)
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
                        homeViewModel.loadWalletInfo(
                            onError = onError
                        )
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
                            )
                            navController.navigate(Screens.TransactionResult.name)
                        })
                }


            }
            composable(route = Screens.QRScanner.name) { backStackEntry ->
                val uiState by qrScannerViewModel.uiState.collectAsState()
                val onBillDetecting: (BillPayload) -> Unit = { payload ->
                    payBillViewModel.init()
                    navController.navigate(Screens.PayBill.name) {
                        launchSingleTop = true
                        popUpTo(Screens.Home.name)
                    }
                    payBillViewModel.onChangeBillCode(payload.billCode)
                    payBillViewModel.onCheckingBill()
                }
                val onTransferDetecting: (TransferPayload) -> Unit = { payload ->
                    transferViewModel.init(
                        onError = onError
                    )
                    transferViewModel.onToWalletNumberChange(payload.toWalletNumber)
                    payload.amount?.let { amount ->
                        transferViewModel.onAmountChange(amount.toString())
                    }
                    navController.navigate(Screens.Transfer.name) {
                        launchSingleTop = true
                        popUpTo(Screens.Home.name)

                    }
                    transferViewModel.onDoneWalletNumber(
                        onError = onError
                    )

                }
                QRScannerScreen(
                    uiState = uiState,
                    detecting = {
                        qrScannerViewModel.onDetecting(
                            qrCode = it,
                            onBillDetecting = onBillDetecting,
                            onTransferDetecting = onTransferDetecting,
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message, type = SnackBarType.INFO
                                )
                            }
                        )
                    },
                    onAnalyzeImage = {
                        qrScannerViewModel.onAnalyzeImage(
                            context = context, uri = it,
                            onBillDetecting = onBillDetecting,
                            onTransferDetecting = onTransferDetecting,
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message, type = SnackBarType.INFO
                                )
                            }
                        )
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
                val uiState by billDetailViewModel.uiState.collectAsState()
                BillDetailScreen(
                    uiState = uiState,
                    onSavedSuccess = {
                        appViewModel.showSnackBarMessage(
                            message = "Lưu hóa đơn thành công", type = SnackBarType.SUCCESS
                        )
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCancelBill = {
                        billDetailViewModel.onCancelBill(
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Hủy hóa đơn thành công",
                                    type = SnackBarType.SUCCESS
                                )
                                navController.navigate(Screens.BillHistory.name) {
                                    popUpTo(Screens.BillHistory.name) {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = onError
                        )
                    }
                )
            }
            composable(route = Screens.TransactionHistory.name) { backStackEntry ->
                isAtRoot = true
                tabNavigation = TabNavigation.HISTORY
                val uiState by transactionHistoryViewModel.uiState.collectAsState()
                val homeUiState by homeViewModel.uiState.collectAsState()
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
                    onApply = { selectedStatus, selectedService, selectedAccountType, selectedSort, selectedFromDate, selectedToDate ->
                        transactionHistoryViewModel.onApply(
                            onError = onError,
                            selectedStatus = selectedStatus,
                            selectedSort = selectedSort,
                            selectedService = selectedService,
                            selectedAccountType = selectedAccountType,
                            selectedFromDate = selectedFromDate,
                            selectedToDate = selectedToDate
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
                    navigationBar = {
                        navigationBar()
                    },
                    fullName = homeUiState.myWallet?.merchantName?:"",
                    avatarUrl =homeUiState.myProfile?.avatarUrl
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

            composable(route = Screens.VerificationRequest.name) { backStackEntry ->
                val uiState by createVerificationRequestViewModel.uiState.collectAsState()
                CreateVerificationRequestScreen(
                    uiState = uiState,
                    isEnableConfirm = createVerificationRequestViewModel.isEnableConfirm(),
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAddFile = {
                        createVerificationRequestViewModel.onAddFile(it)
                    },
                    onDeleteFile = {
                        createVerificationRequestViewModel.onDeleteFile(it)
                    },
                    onConfirmClick = {
                        createVerificationRequestViewModel.onConfirmClick(
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Tạo yêu cầu xác thực thành công",
                                    type = SnackBarType.SUCCESS
                                )
                                navController.navigate(Screens.Home.name) {
                                    popUpTo(Screens.VerificationRequest.name) {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = onError
                        )
                    },
                    onSelectType = {
                        createVerificationRequestViewModel.onSelectType(it)
                    },
                    onChangeInvoiceDisplayName = {
                        createVerificationRequestViewModel.onChangeInvoiceDisplayName(it)
                    },
                    onChangeBusinessName = {
                        createVerificationRequestViewModel.onChangeBusinessName(it)
                    },
                    onChangeBusinessCode = {
                        createVerificationRequestViewModel.onChangeBusinessCode(it)
                    },
                    onChangeBusinessAddress = {
                        createVerificationRequestViewModel.onChangeBusinessAddress(it)
                    },
                    onChangeRepresentativeName = {
                        createVerificationRequestViewModel.onChangeRepresentativeName(it)
                    },
                    onChangeRepresentativeIdNumber = {

                        createVerificationRequestViewModel.onChangeRepresentativeIdNumber(it)
                    },
                    onChangeContactEmail = {
                        createVerificationRequestViewModel.onChangeContactEmail(it)
                    },
                    onChangeContactPhone = {
                        createVerificationRequestViewModel.onChangeContactPhone(it)
                    }


                )
            }
            composable(route = Screens.MyProfile.name) {
                val uiState by myProfileViewModel.uiState.collectAsState()
                val homeUiState by homeViewModel.uiState.collectAsState()
                MyProfileScreen(
                    uiState = uiState,
                    myWalletNumber = homeUiState.myWallet!!.walletNumber,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onUpdateImageProfile = {
                        myProfileViewModel.onUpdateImageProfile(
                            uri = it,
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Cập nhật ảnh đại diện thành công",
                                    type = SnackBarType.SUCCESS,
                                    actionLabel = "Đóng",
                                    onAction = {
                                        appViewModel.closeSnackBarMessage()
                                    }
                                )
                            },
                            onError = onError
                        )
                    },
                    onRetry = {
                        myProfileViewModel.loadUserInfo(
                            onError = onError
                        )
                    },
                    onNavigateMyContacts = {
                        savedReceiverViewModel.init()
                        navController.navigate(Screens.SavedReceiver.name)
                    },
                    onNavigateToVerificationRequest = {
                        navigator[ServiceCategory.VERIFICATION_REQUEST.name]?.invoke()
                    },
                    onNavigateToTermsAndConditions = {
                        navController.navigate(Screens.TermAndConditions.name)
                    },
                    onSavedQrSuccess = {
                        appViewModel.showSnackBarMessage(
                            message = "Lưu QR thành công", type = SnackBarType.SUCCESS
                        )
                    }
                )
            }
            composable(route = Screens.PayLater.name) {
                val uiState by payLaterViewModel.uiState.collectAsState()
                PayLaterScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onActivePaylater = {
                        payLaterApplicationViewModel.init(type = PayLaterApplicationType.ACTIVATION)
                        navController.navigate(Screens.PayLaterApplication.name)
                    },
                    onLockPaylater = {
                        payLaterViewModel.lockAccountRequest(
                            onError = onError
                        )
                    },
                    onUnlockPaylater = {
                        payLaterViewModel.unlockAccountRequest(
                            onError = onError
                        )
                    },
                    onRetry = {
                        payLaterViewModel.init(
                            onError = onError
                        )
                    },
                    onNavigateToApplicationHistory = {
                        payLaterApplicationHistoryViewModel.clearState()
                        navController.navigate(Screens.PayLaterApplicationHistory.name)
                    },
                    onNavigateToBillingCycleHistory = {
                        billingCycleViewModel.clearState()
                        navController.navigate(Screens.BillingCycle.name)
                    }

                )
            }
            composable(route = Screens.PayLaterApplication.name) {
                val uiState by payLaterApplicationViewModel.uiState.collectAsState()
                PayLaterApplicationScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLimitChange = {
                        payLaterApplicationViewModel.onChangeRequestLimit(it)
                    },
                    onChangeReason = {
                        payLaterApplicationViewModel.onChangeReason(it)
                    },
                    onConfirmClick = {
                        payLaterApplicationViewModel.onClickConfirm(
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Gửi yêu cầu thành công",
                                    type = SnackBarType.SUCCESS
                                )
                                navController.navigate(Screens.Home.name) {
                                    popUpTo(Screens.PayLaterApplication.name) {
                                        inclusive = true
                                    }
                                }
                            },
                            onError = onError
                        )
                    },
                )
            }
            composable(route = Screens.PayLaterApplicationHistory.name) {
                val uiState by payLaterApplicationHistoryViewModel.uiState.collectAsState()
                val applications =
                    payLaterApplicationHistoryViewModel.payLaterApplicationHistoryPager.collectAsLazyPagingItems()
                PayLaterApplicationHistoryScreen(
                    uiState = uiState,
                    applications = applications,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onErrorLoading = {
                        appViewModel.showSnackBarMessage(
                            message = it,
                            type = SnackBarType.ERROR
                        )
                    },
                    onApply = { status: PayLaterApplicationStatus?, type: PayLaterApplicationType?, fromDate: LocalDate, toDate: LocalDate ->
                        payLaterApplicationHistoryViewModel.onApply(
                            selectedStatus = status,
                            selectedType = type,
                            fromDate = fromDate,
                            toDate = toDate
                        )
                    }
                )
            }
            composable(
                route = Screens.SavedReceiver.name
            ) {
                val uiState by savedReceiverViewModel.uiState.collectAsState()

                SavedReceiverScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDeleteReceiver = { walletNumber ->
                        savedReceiverViewModel.onDeleteSavedReceiver(
                            walletNumber = walletNumber,
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Xóa người nhận thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            }
                        )
                    },
                    onDoneWalletNumber = {
                        savedReceiverViewModel.onDoneWalletNumber(
                            onError = onError
                        )
                    },
                    onAddReceiver = {
                        savedReceiverViewModel.onAddSavedReceiver(
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Thêm người nhận thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            },
                            onError = { message ->
                                appViewModel.showSnackBarMessage(
                                    message = message,
                                    type = SnackBarType.ERROR
                                )
                            }
                        )
                    },
                    onSearchReceiver = {
                        savedReceiverViewModel.onSearch()
                    },
                    onChangeKeyword = {
                        savedReceiverViewModel.onChangeKeyword(it)
                    },
                    onChangeMemorableName = {
                        savedReceiverViewModel.onChangeMemorableName(it)
                    },
                    onChangeToWalletNumber = {
                        savedReceiverViewModel.onChangeToWalletNumber(it)
                    },
                    onClearAddReceiverDialog = {
                        savedReceiverViewModel.onClearAddDialog()
                    },
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
            composable(
                route = Screens.Analytic.name
            ) {
                isAtRoot = true
                tabNavigation = TabNavigation.ANALYTICS

                val uiState by analyticViewModel.uiState.collectAsState()
                val homeUiState by homeViewModel.uiState.collectAsState()
                AnalyticScreen(
                    uiState = uiState,
                    navigationBar = { navigationBar() },
                    onRetry = {
                        analyticViewModel.init(
                            onError = onError
                        )
                    },
                    onAnalyze = {
                        analyticViewModel.onAnalyze(
                            onError = onError
                        )
                    },
                    onPlusMonth = {
                        analyticViewModel.onPlusMonth(
                            onError = onError
                        )
                    },
                    onMinusMonth = {
                        analyticViewModel.onMinusMonth(
                            onError = onError
                        )
                    },
                    onChangeMoneyFlowType = {
                        analyticViewModel.onChangeMoneyFlowType(
                            flowType = it,
                            onError = onError
                        )
                    },
                    fullName = homeUiState.myWallet?.merchantName?:"",
                    avatarUrl =homeUiState.myProfile?.avatarUrl
                )
            }
            composable(route = Screens.BillingCycle.name) {
                val uiState by billingCycleViewModel.uiState.collectAsState()
                val billingCycles =billingCycleViewModel.billingCyclePager.collectAsLazyPagingItems()
                BillingCycleScreen(
                    uiState = uiState,
                    billingCycles = billingCycles,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onErrorLoading = {
                        appViewModel.showSnackBarMessage(
                            message = it,
                            type = SnackBarType.ERROR
                        )
                    },
                    onChangeSortOption = {
                        billingCycleViewModel.onChangeSortOption()
                    },
                    onSelectBillingCycle = {
                        billingCycleViewModel.onSelectBillingCycle(it)
                    },
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