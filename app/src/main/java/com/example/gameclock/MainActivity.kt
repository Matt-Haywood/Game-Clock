package com.example.gameclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gameclock.ui.AppNavigation
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.theme.GameClockTheme

class MainActivity : ComponentActivity() {
    val clockViewModel: ClockViewModel = ClockViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameClockTheme(appTheme = clockViewModel.stateApp.theme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(clockViewModel = clockViewModel)
//                    HomeScreen(clockThemeList = ClockThemeList().loadThemes(), onThemeClick = {})
                    //BaseClockScreen(clockFont = Doppio)
                }
            }
        }
    }
}


