package com.example.ibanking_kltn.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.ibanking_kltn.ui.AppGraph
import com.example.ibanking_kltn.ui.Screens
import com.example.ibanking_kltn.ui.uistates.SnackBarUiState

fun NavGraphBuilder.myProfileGraph(
    navController: NavController,
    onShowSnackBar: (snakeBarUiState: SnackBarUiState) -> Unit,
) {
    this.navigation(
        startDestination = Screens.MyProfile.name,
        route = AppGraph.MyProfileGraph.name,
    ) {

//        composable(route = Screens.MyProfile.name) {
//            val myProfileViewModel: MyProfileViewModel = hiltViewModel()
//            val uiState by myProfileViewModel.uiState.collectAsState()
//            MyProfileScreen(
//                uiState = uiState,
//                //todo
////                    myWalletNumber = homeUiState.myWallet!!.walletNumber,
//                myWalletNumber = "0123456",
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onUpdateImageProfile = {
//                    myProfileViewModel.onUpdateImageProfile(
//                        uri = it,
//                        onSuccess = { uri ->
//                            appViewModel.showSnackBarMessage(
//                                message = "Cập nhật ảnh đại diện thành công",
//                                type = SnackBarType.SUCCESS,
//                                actionLabel = "Đóng",
//                                onAction = {
//                                    appViewModel.closeSnackBarMessage()
//                                }
//                            )
//                            appViewModel.updateUserInfo(
//                                avatarUrl = uri,
//
//                                )
//                        },
//                        onError = onError
//                    )
//                },
//                onRetry = {
//                    myProfileViewModel.loadUserInfo(
//                        onSuccess = { avatarUrl, fullName ->
//                            appViewModel.updateUserInfo(
//                                avatarUrl = avatarUrl,
//                                fullName = fullName
//
//                            )
//                        },
//                        onError = onError
//                    )
//                },
//                onNavigateMyContacts = {
//                    savedReceiverViewModel.init()
//                    navController.navigate(Screens.SavedReceiver.name)
//                },
//                onNavigateToVerificationRequest = {
//                    navController.navigate(Screens.VerificationRequest.name)
//                },
//                onNavigateToTermsAndConditions = {
//                    navController.navigate(Screens.TermAndConditions.name)
//                },
//                onSavedQrSuccess = {
//                    appViewModel.showSnackBarMessage(
//                        message = "Lưu QR thành công", type = SnackBarType.SUCCESS
//                    )
//                }
//            )
//        }
//
//        composable(route = Screens.VerificationRequest.name) { backStackEntry ->
//            val uiState by createVerificationRequestViewModel.uiState.collectAsState()
//            CreateVerificationRequestScreen(
//                uiState = uiState,
//                isEnableConfirm = createVerificationRequestViewModel.isEnableConfirm(),
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onAddFile = {
//                    createVerificationRequestViewModel.onAddFile(it)
//                },
//                onDeleteFile = {
//                    createVerificationRequestViewModel.onDeleteFile(it)
//                },
//                onConfirmClick = {
//                    createVerificationRequestViewModel.onConfirmClick(
//                        onSuccess = {
//                            appViewModel.showSnackBarMessage(
//                                message = "Tạo yêu cầu xác thực thành công",
//                                type = SnackBarType.SUCCESS
//                            )
//                            navController.navigate(Screens.Home.name) {
//                                popUpTo(Screens.VerificationRequest.name) {
//                                    inclusive = true
//                                }
//                            }
//                        },
//                        onError = onError
//                    )
//                },
//                onSelectType = {
//                    createVerificationRequestViewModel.onSelectType(it)
//                },
//                onChangeInvoiceDisplayName = {
//                    createVerificationRequestViewModel.onChangeInvoiceDisplayName(it)
//                },
//                onChangeBusinessName = {
//                    createVerificationRequestViewModel.onChangeBusinessName(it)
//                },
//                onChangeBusinessCode = {
//                    createVerificationRequestViewModel.onChangeBusinessCode(it)
//                },
//                onChangeBusinessAddress = {
//                    createVerificationRequestViewModel.onChangeBusinessAddress(it)
//                },
//                onChangeRepresentativeName = {
//                    createVerificationRequestViewModel.onChangeRepresentativeName(it)
//                },
//                onChangeRepresentativeIdNumber = {
//
//                    createVerificationRequestViewModel.onChangeRepresentativeIdNumber(it)
//                },
//                onChangeContactEmail = {
//                    createVerificationRequestViewModel.onChangeContactEmail(it)
//                },
//                onChangeContactPhone = {
//                    createVerificationRequestViewModel.onChangeContactPhone(it)
//                }
//
//
//            )
//        }
//        composable(
//            route = Screens.SavedReceiver.name
//        ) {
//            val savedReceiverViewModel: SavedReceiverViewModel = hiltViewModel()
//
//            val uiState by savedReceiverViewModel.uiState.collectAsState()
//
//            SavedReceiverScreen(
//                uiState = uiState,
//                onBackClick = {
//                    navController.popBackStack()
//                },
//                onDeleteReceiver = { walletNumber ->
//                    savedReceiverViewModel.onDeleteSavedReceiver(
//                        walletNumber = walletNumber,
//                        onSuccess = {
//                            appViewModel.showSnackBarMessage(
//                                message = "Xóa người nhận thành công",
//                                type = SnackBarType.SUCCESS
//                            )
//                        }
//                    )
//                },
//                onDoneWalletNumber = {
//                    savedReceiverViewModel.onDoneWalletNumber(
//                        onError = onError
//                    )
//                },
//                onAddReceiver = {
//                    savedReceiverViewModel.onAddSavedReceiver(
//                        onSuccess = {
//                            appViewModel.showSnackBarMessage(
//                                message = "Thêm người nhận thành công",
//                                type = SnackBarType.SUCCESS
//                            )
//                        },
//                        onError = { message ->
//                            appViewModel.showSnackBarMessage(
//                                message = message,
//                                type = SnackBarType.ERROR
//                            )
//                        }
//                    )
//                },
//                onSearchReceiver = {
//                    savedReceiverViewModel.onSearch()
//                },
//                onChangeKeyword = {
//                    savedReceiverViewModel.onChangeKeyword(it)
//                },
//                onChangeMemorableName = {
//                    savedReceiverViewModel.onChangeMemorableName(it)
//                },
//                onChangeToWalletNumber = {
//                    savedReceiverViewModel.onChangeToWalletNumber(it)
//                },
//                onClearAddReceiverDialog = {
//                    savedReceiverViewModel.onClearAddDialog()
//                },
//            )
//        }
//        composable(
//            route = Screens.TermAndConditions.name
//        ) {
//            TermsAndConditionsScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}