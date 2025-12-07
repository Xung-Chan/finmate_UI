package com.example.ibanking_kltn.ui.uistates

import com.example.ibanking_kltn.utils.SnackBarType

data class  SnackBarState(
    val isVisible: Boolean = false,
    val message: String = "",
    val type: SnackBarType = SnackBarType.INFO,
    val actionLabel: String? = null,
    val onAction: ()-> Unit={}
)