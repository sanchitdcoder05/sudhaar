package com.san.sudhaar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.san.sudhaar.navigation.SudhaarApp
import com.san.sudhaar.ui.theme.SudhaarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudhaarTheme {
                SudhaarApp()
            }
        }
    }
}