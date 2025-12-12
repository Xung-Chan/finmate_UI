package com.example.ibanking_kltn

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.ibanking_kltn.ui.AppScreen
import com.example.ibanking_kltn.ui.theme.IBanking_KLTNTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //request camera permission
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        enableEdgeToEdge()
        setContent {
            IBanking_KLTNTheme {
                AppScreen()
            }
        }
    }
}