package com.example.ibanking_kltn.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.ibanking_kltn.dtos.definitions.AppGraph
import com.example.ibanking_kltn.dtos.definitions.NavKey
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationStatus
import com.example.ibanking_kltn.dtos.definitions.PayLaterApplicationType
import com.example.ibanking_kltn.dtos.definitions.Screens
import com.example.ibanking_kltn.dtos.definitions.ServiceCategory
import com.example.ibanking_kltn.dtos.definitions.TabNavigation
import com.example.ibanking_kltn.ui.navigation.changePasswordGraph
import com.example.ibanking_kltn.ui.navigation.depositGraph
import com.example.ibanking_kltn.ui.navigation.myProfileGraph
import com.example.ibanking_kltn.ui.navigation.signInGraph
import com.example.ibanking_kltn.ui.navigation.spendingGraph
import com.example.ibanking_kltn.ui.screens.TransactionDetailScreen
import com.example.ibanking_kltn.ui.screens.analytic.AnalyticEffect
import com.example.ibanking_kltn.ui.screens.analytic.AnalyticScreen
import com.example.ibanking_kltn.ui.screens.analytic.AnalyticViewModel
import com.example.ibanking_kltn.ui.screens.bill.create.CreateBillScreen
import com.example.ibanking_kltn.ui.screens.bill.create.CreateBillViewModel
import com.example.ibanking_kltn.ui.screens.bill.detail.BillDetailScreen
import com.example.ibanking_kltn.ui.screens.bill.detail.BillDetailViewModel
import com.example.ibanking_kltn.ui.screens.bill.history.BillHistoryScreen
import com.example.ibanking_kltn.ui.screens.bill.history.BillHistoryViewModel
import com.example.ibanking_kltn.ui.screens.bill.pay_bill.BillViewModel
import com.example.ibanking_kltn.ui.screens.bill.pay_bill.PayBillEffect
import com.example.ibanking_kltn.ui.screens.bill.pay_bill.PayBillScreen
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmContent
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmEffect
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmEvent
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmPaymentScreen
import com.example.ibanking_kltn.ui.screens.confirm_transaction.ConfirmViewModel
import com.example.ibanking_kltn.ui.screens.home.HomeEffect
import com.example.ibanking_kltn.ui.screens.home.HomeScreen
import com.example.ibanking_kltn.ui.screens.home.HomeViewModel
import com.example.ibanking_kltn.ui.screens.notification.NotificationEvent
import com.example.ibanking_kltn.ui.screens.notification.NotificationScreen
import com.example.ibanking_kltn.ui.screens.notification.NotificationViewModel
import com.example.ibanking_kltn.ui.screens.pay_later.application.create.PayLaterApplicationScreen
import com.example.ibanking_kltn.ui.screens.pay_later.application.create.PayLaterApplicationViewModel
import com.example.ibanking_kltn.ui.screens.pay_later.application.history.PayLaterApplicationHistoryScreen
import com.example.ibanking_kltn.ui.screens.pay_later.application.history.PayLaterApplicationHistoryViewModel
import com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle.BillingCycleEffect
import com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle.BillingCycleScreen
import com.example.ibanking_kltn.ui.screens.pay_later.billing_cycle.BillingCycleViewModel
import com.example.ibanking_kltn.ui.screens.pay_later.management.PayLaterScreen
import com.example.ibanking_kltn.ui.screens.pay_later.management.PayLaterViewModel
import com.example.ibanking_kltn.ui.screens.qr_scanner.QRScannerScreen
import com.example.ibanking_kltn.ui.screens.qr_scanner.QRScannerViewModel
import com.example.ibanking_kltn.ui.screens.qr_scanner.QrScannerEffect
import com.example.ibanking_kltn.ui.screens.service_management.AllServiceEffect
import com.example.ibanking_kltn.ui.screens.service_management.ServiceManagementScreen
import com.example.ibanking_kltn.ui.screens.service_management.ServiceManagementViewModel
import com.example.ibanking_kltn.ui.screens.setting.SettingEffect
import com.example.ibanking_kltn.ui.screens.setting.SettingScreen
import com.example.ibanking_kltn.ui.screens.setting.SettingViewModel
import com.example.ibanking_kltn.ui.screens.transaction_history.TransactionHistoryScreen
import com.example.ibanking_kltn.ui.screens.transaction_history.TransactionHistoryViewModel
import com.example.ibanking_kltn.ui.screens.transaction_result.TransactionResultEffect
import com.example.ibanking_kltn.ui.screens.transaction_result.TransactionResultScreen
import com.example.ibanking_kltn.ui.screens.transaction_result.TransactionResultViewModel
import com.example.ibanking_kltn.ui.screens.transfer.TransferEffect
import com.example.ibanking_kltn.ui.screens.transfer.TransferScreen
import com.example.ibanking_kltn.ui.screens.transfer.TransferViewModel
import com.example.ibanking_kltn.ui.theme.AppTypography
import com.example.ibanking_kltn.ui.theme.White1
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState
import com.example.ibanking_kltn.ui.viewmodels.AppViewModel
import com.example.ibanking_kltn.ui.screens.transaction_history.TransactionDetailViewModel
import com.example.ibanking_kltn.utils.DefaultImageProfile
import com.example.ibanking_kltn.utils.GradientSnackBar
import com.example.ibanking_kltn.utils.NavigationBar
import com.example.ibanking_kltn.utils.SnackBarType
import com.example.ibanking_kltn.utils.appSessionManager
import com.example.ibanking_kltn.utils.formatterDateString
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {

    var snackBarInstance by remember {
        mutableStateOf<SnackBarUiState?>(null)
    }

    LaunchedEffect(snackBarInstance) {
        if (snackBarInstance != null) {
            delay(3000L)
            snackBarInstance = null
        }
    }


    val appViewModel: AppViewModel = hiltViewModel()
    val billHistoryViewModel: BillHistoryViewModel = hiltViewModel()
    val billDetailViewModel: BillDetailViewModel = hiltViewModel()
    val transactionDetailViewModel: TransactionDetailViewModel = hiltViewModel()
    val createBillViewModel: CreateBillViewModel = hiltViewModel()
    val payLaterViewModel: PayLaterViewModel = hiltViewModel()
    val payLaterApplicationViewModel: PayLaterApplicationViewModel = hiltViewModel()
    val payLaterApplicationHistoryViewModel: PayLaterApplicationHistoryViewModel = hiltViewModel()
    var tabNavigation by remember { mutableStateOf(TabNavigation.HOME) }

    val appUiState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val onError: (String) -> Unit = { message ->
        appViewModel.showSnackBarMessage(
            message = message, type = SnackBarType.ERROR
        )
    }
    val onShowSnackBar: (SnackBarUiState) -> Unit = {
        snackBarInstance = it
    }


    val navigationBar: @Composable () -> Unit = {
        NavigationBar(
            bottomBarHeight = 100.dp,
            currentTab = tabNavigation,
            onNavigateToSettingScreen = {
                navController.navigate(Screens.Settings.name) {
                    popUpTo(Screens.Home.name) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },

            onNavigateToHomeScreen = {
                navController.popBackStack(
                    Screens.Home.name,
                    inclusive = false
                )
            },

            onNavigateToTransactionHistoryScreen = {
                navController.navigate(Screens.TransactionHistory.name) {
                    popUpTo(Screens.Home.name) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            onNavigateToAnalyticsScreen = {
                navController.navigate(Screens.Analytic.name) {
                    popUpTo(Screens.Home.name) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }

            },
            onNavigateToQRScanner = {
                navController.navigate(Screens.QRScanner.name) {
                    popUpTo(Screens.Home.name) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            })
    }
    val navigator = mapOf(
        ServiceCategory.MONEY_TRANSFER.name to {
            appViewModel.addRecentService(ServiceCategory.MONEY_TRANSFER)
            navController.navigate(Screens.Transfer.name)
        },
        ServiceCategory.DEPOSIT.name to {
            appViewModel.addRecentService(ServiceCategory.DEPOSIT)
            navController.navigate(AppGraph.DepositGraph.name)
        },
        ServiceCategory.BILL_PAYMENT.name to {
            appViewModel.addRecentService(ServiceCategory.BILL_PAYMENT)
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
        ServiceCategory.BILL_CREATE.name to {
            appViewModel.addRecentService(ServiceCategory.BILL_CREATE)
            createBillViewModel.init()
            navController.navigate(Screens.CreateBill.name)
        }


    )
    val userComponent: @Composable () -> Unit = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.Start
                ),

                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = White1,
                            shape = CircleShape
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (appUiState.avatarUrl == null) {
                        DefaultImageProfile(
                            modifier = Modifier
                                .size(100.dp),
                            name = appUiState.fullName
                        )
                    } else {

                        AsyncImage(
                            model = appUiState.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Hôm nay, ${formatterDateString(LocalDate.now())}",
                        color = White1,
                        style = AppTypography.bodySmall
                    )
                    Text(
                        text = "Xin chào, ${appUiState.fullName}!",
                        color = White1,
                        style = AppTypography.bodyMedium
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Screens.Notification.name)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .align(
                            Alignment.CenterVertically
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = White1,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        NavHost(
            navController = navController, startDestination = AppGraph.SpendingGraph.name
        ) {
            signInGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )
            composable(route = Screens.Home.name) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val homeUiState by homeViewModel.uiState.collectAsState()
                tabNavigation = TabNavigation.HOME
                LaunchedEffect(Unit) {
                    homeViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is HomeEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            is HomeEffect.NavigateToServiceScreen -> {
                                navigator[effect.service.name]?.invoke()
                            }

                            HomeEffect.NavigateToAllServiceScreen -> {
                                navController.navigate(Screens.AllService.name)
                            }
                        }
                    }
                }
                HomeScreen(
                    homeUiState = homeUiState,
                    userComponent = userComponent,
                    navigationBar = { navigationBar() },
                    onEvent = homeViewModel::onEvent,
                )
            }
            composable(route = Screens.Settings.name) {
                tabNavigation = TabNavigation.PROFILE
                val settingViewModel: SettingViewModel = hiltViewModel()
                val uiState by settingViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    settingViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is SettingEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            SettingEffect.NavigateToChangePasswordScreen -> {
                                navController.navigate(AppGraph.ChangePasswordGraph.name)
                            }

                            SettingEffect.NavigateToMyProfile -> {
                                navController.navigate(AppGraph.MyProfileGraph.name)
                            }

                            SettingEffect.Logout -> {
                                navController.navigate(Screens.SignIn.name) {
                                    popUpTo(Screens.SignIn.name) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
                SettingScreen(
                    uiState = uiState,
                    userComponent = userComponent,
                    navigationBar = { navigationBar() },
                    onEvent = settingViewModel::onEvent,
                )
            }
            changePasswordGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )
            myProfileGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )
            composable(
                route = Screens.Analytic.name
            ) {
                tabNavigation = TabNavigation.ANALYTICS
                val analyticViewModel: AnalyticViewModel = hiltViewModel()
                val uiState by analyticViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    analyticViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is AnalyticEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }
                        }
                    }
                }
                AnalyticScreen(
                    uiState = uiState,
                    userComponent = userComponent,
                    navigationBar = { navigationBar() },
                    onEvent = analyticViewModel::onEvent
                )
            }

            composable(route = Screens.AllService.name) {
                val allServiceViewModel: ServiceManagementViewModel = hiltViewModel()
                val uiState by allServiceViewModel.uiState.collectAsState()
                LaunchedEffect(
                    Unit
                ) {
                    allServiceViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is AllServiceEffect.ShowSnackBar -> snackBarInstance = effect.snackBar
                            is AllServiceEffect.NavigateToServiceScreen -> {
                                navigator[effect.service.name]?.invoke()
                            }

                        }
                    }
                }
                ServiceManagementScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEvent = allServiceViewModel::onEvent
                )
            }

            composable(
                route = "${Screens.Transfer.name}?toWalletNumber={toWalletNumber}&amount={amount}",
                arguments = listOf(
                    navArgument("toWalletNumber") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("amount") {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) { backStackEntry ->
                val transferViewModel: TransferViewModel = hiltViewModel()
                val transferUiState by transferViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    transferViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is TransferEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            is TransferEffect.NavigateToConfirmScreen -> {
                                backStackEntry.savedStateHandle[NavKey.CONFIRM_CONTENT.name] =
                                    effect.confirmContent
                                navController.navigate(
                                    Screens.ConfirmPayment.name
                                )
                            }
                        }
                    }
                }

                TransferScreen(
                    uiState = transferUiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEvent = transferViewModel::onEvent,
                )
            }
            composable(route = Screens.ConfirmPayment.name) { backStackEntry ->
                val confirmViewModel: ConfirmViewModel = hiltViewModel()
                val confirmUiState by confirmViewModel.uiState.collectAsState()

                val previousEntry = remember(backStackEntry) {
                    navController.previousBackStackEntry!!
                }
                val confirmContent =
                    previousEntry.savedStateHandle.get<ConfirmContent>(NavKey.CONFIRM_CONTENT.name)
                LaunchedEffect(confirmContent) {
                    confirmContent?.let {
                        confirmViewModel.onEvent(
                            ConfirmEvent.Init(confirmContent = it)
                        )
                        previousEntry.savedStateHandle.remove<ConfirmContent>(NavKey.CONFIRM_CONTENT.name)
                    }
                }


                LaunchedEffect(Unit) {


                    confirmViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is ConfirmEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            is ConfirmEffect.PaymentSuccess -> {
                                snackBarInstance = SnackBarUiState(
                                    message = "Thanh toán thành công",
                                    type = SnackBarType.SUCCESS
                                )
                                navController.navigate("${Screens.TransactionResult.name}/${effect.transactionId}") {
                                    popUpTo(Screens.Home.name) {
                                        inclusive = false
                                    }
                                }
                            }
                        }
                    }
                }


                ConfirmPaymentScreen(
                    uiState = confirmUiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEvent = confirmViewModel::onEvent,
                )
            }
            composable(
                route = "${Screens.PayBill.name}?billCode={billCode}",
                arguments = listOf(
                    navArgument("billCode") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val payBillViewModel: BillViewModel = hiltViewModel()
                val uiState by payBillViewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    payBillViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is PayBillEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            is PayBillEffect.NavigateToConfirmScreen -> {
                                backStackEntry.savedStateHandle[NavKey.CONFIRM_CONTENT.name] =
                                    effect.confirmContent
                                navController.navigate(
                                    Screens.ConfirmPayment.name
                                )
                            }
                        }
                    }
                }
                PayBillScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEvent = payBillViewModel::onEvent
                )
            }

            composable(route = Screens.QRScanner.name) { backStackEntry ->
                val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
                val uiState by qrScannerViewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    qrScannerViewModel.uiEffect.collect { effect ->
                        when (effect) {

                            is QrScannerEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }

                            is QrScannerEffect.Navigate -> {
                                navController.navigate(effect.route) {
                                    popUpTo(Screens.Home.name) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
                QRScannerScreen(
                    uiState = uiState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEvent = qrScannerViewModel::onEvent,
                )
            }
            composable(route = Screens.BillingCycle.name) { backStackEntry ->
                val billingCycleViewModel: BillingCycleViewModel = hiltViewModel()
                val uiState by billingCycleViewModel.uiState.collectAsState()
                val billingCycles =
                    billingCycleViewModel.billingCyclePager.collectAsLazyPagingItems()
                LaunchedEffect(Unit) {
                    billingCycleViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            is BillingCycleEffect.NavigateToConfirmScreen -> {
                                backStackEntry.savedStateHandle[NavKey.CONFIRM_CONTENT.name] =
                                    effect.confirmContent
                                navController.navigate(
                                    Screens.ConfirmPayment.name
                                )
                            }

                            is BillingCycleEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }
                        }
                    }
                }

                BillingCycleScreen(
                    uiState = uiState,
                    billingCycles = billingCycles,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onErrorLoading = {
                        snackBarInstance = SnackBarUiState(
                            message = it,
                            type = SnackBarType.ERROR
                        )
                    },
                    onEvent = billingCycleViewModel::onEvent

                )
            }
            composable(
                route = "${Screens.TransactionResult.name}/{${NavKey.TRANSACTION_ID.name}}",
                arguments = listOf(
                    navArgument(NavKey.TRANSACTION_ID.name) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val transactionResultViewModel: TransactionResultViewModel = hiltViewModel()
                val transactionResultUiState by transactionResultViewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    transactionResultViewModel.uiEffect.collect { effect ->
                        when (effect) {
                            TransactionResultEffect.InitFailed -> {
                                snackBarInstance = SnackBarUiState(
                                    message = "Không tìm thấy giao dịch",
                                    type = SnackBarType.ERROR
                                )
                                navController.navigate(Screens.Home.name) {
                                    popUpTo(Screens.Home.name) {
                                        inclusive = true
                                    }
                                }
                            }

                            is TransactionResultEffect.ShowSnackBar -> {
                                snackBarInstance = effect.snackBar
                            }
                        }
                    }
                }

                TransactionResultScreen(
                    onBackToHomeClick = {
                        navController.navigate(Screens.Home.name) {
                            popUpTo(Screens.Home.name) {
                                inclusive = true
                            }
                        }
                    },
                    uiState = transactionResultUiState,
                )
            }

            spendingGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )

            //todo done
            depositGraph(
                navController = navController,
                onShowSnackBar = onShowSnackBar
            )


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
                tabNavigation = TabNavigation.HISTORY
                val transactionHistoryViewModel: TransactionHistoryViewModel = hiltViewModel()
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
                    userComponent = userComponent,
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
                            onError = onError,
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Gửi yêu cầu khóa Paylater thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            }
                        )
                    },
                    onUnlockPaylater = {
                        payLaterViewModel.unlockAccountRequest(
                            onError = onError,
                            onSuccess = {
                                appViewModel.showSnackBarMessage(
                                    message = "Gửi yêu cầu mở khóa Paylater thành công",
                                    type = SnackBarType.SUCCESS
                                )
                            }
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



            composable(route = Screens.Notification.name) {
                val notificationViewModel: NotificationViewModel = hiltViewModel()
                val uiState by notificationViewModel.uiState.collectAsState()
                val notifications =
                    notificationViewModel.notificationPager.collectAsLazyPagingItems()
                NotificationScreen(
                    uiState = uiState,
                    notifications = notifications,
                    onChangType = {
                        notificationViewModel.onEvent(NotificationEvent.ChangeType)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onErrorLoading = {
                        appViewModel.showSnackBarMessage(
                            message = it,
                            type = SnackBarType.ERROR
                        )
                    }
                )
            }
        }



        if (appUiState.isVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                GradientSnackBar(
                    message = appUiState.message,
                    type = appUiState.type,
                    actionLabel = appUiState.actionLabel,
                    onAction = appUiState.onAction,
                )
            }
        }
        snackBarInstance?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                GradientSnackBar(
                    message = snackBarInstance!!.message,
                    type = snackBarInstance!!.type,
                    onAction = {
                        snackBarInstance = null
                    },
                )
            }
        }

    }

    var lastBackPressed by remember { mutableLongStateOf(0L) }
    var isAtRoot by remember {
        mutableStateOf(false)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(currentRoute) {
        isAtRoot = navController.currentBackStackEntry?.destination?.route in listOf(
            Screens.SignIn.name,
            Screens.Home.name,
            Screens.Settings.name,
            Screens.Analytic.name,
            Screens.TransactionHistory.name
        )
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
    val appSessionManager = remember {
        context.appSessionManager()
    }
    LaunchedEffect(Unit) {
        appSessionManager.timeout.collect {
            snackBarInstance = SnackBarUiState(
                message = "Phiên đăng nhập của bạn đã hết hạn, vui lòng đăng nhâp lại",
                type = SnackBarType.INFO
            )
            navController.navigate(AppGraph.SignInGraph.name) {
                popUpTo(0)
            }
        }
    }
}