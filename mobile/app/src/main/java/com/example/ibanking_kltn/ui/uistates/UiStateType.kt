package com.example.ibanking_kltn.ui.uistates

sealed class StateType {
    data class FAILED(val message: String) : StateType()
    object SUCCESS : StateType()
    object NONE : StateType()
    object LOADING : StateType()
}