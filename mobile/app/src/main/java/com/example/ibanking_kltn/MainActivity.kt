package com.example.ibanking_kltn

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.example.ibanking_kltn.ui.AppScreen
import com.example.ibanking_kltn.ui.theme.IBanking_KLTNTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IBanking_KLTNTheme {
                AppScreen()
            }
        }
    }
}