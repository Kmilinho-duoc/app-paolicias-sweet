package com.example.paoliciassweet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.paoliciassweet.ui.navigation.AppNavigation
import com.example.paoliciassweet.ui.theme.PaoliciasSweetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaoliciasSweetTheme {
                AppNavigation()
            }
        }
    }
}
