package com.mhappening.gameclock.ui

import android.app.Activity
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mhappening.gameclock.R
import com.mhappening.gameclock.data.clockthemes.ClockThemeList
import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.ui.alarm.AlarmViewModel
import com.mhappening.gameclock.ui.screens.BaseClockScreen
import com.mhappening.gameclock.ui.screens.HomeScreen
import com.mhappening.gameclock.ui.screens.SettingsScreen
import com.mhappening.gameclock.ui.theme.GameClockTheme
import com.mhappening.gameclock.ui.timer.TimerViewModel

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    Clock(title = R.string.clock),
    Settings(title = R.string.settings)
}


//TODO: Add navigation tests + testTags


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    clockViewModel: ClockViewModel = viewModel(),
    alarmViewModel: AlarmViewModel = viewModel(),
    timerViewModel: TimerViewModel = viewModel(),
) {

    val context = LocalContext.current
    val window = remember { (context as Activity).window }
    val isFullscreen = clockViewModel.clockUiState.collectAsState().value.isFullScreen

    LaunchedEffect(isFullscreen) {
        WindowCompat.setDecorFitsSystemWindows(window, !isFullscreen)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            if (isFullscreen) {
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    GameClockTheme(appTheme = clockViewModel.clockUiState.collectAsState().value.theme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Clock.name,
                enterTransition = {
                    scaleIn()
                    fadeIn()
                },
                exitTransition = {
                    scaleOut()
                    fadeOut(animationSpec = tween(durationMillis = 500))
                }
            ) {
                composable(AppScreen.Home.name) {
                    HomeScreen(
                        clockThemeList = ClockThemeList().loadThemes(),
                        onThemeClick = { appTheme: AppTheme ->
                            clockViewModel.onThemeChange(
                                ThemeChangeEvent.ThemeChange(
                                    appTheme
                                )
                            )
                            clockViewModel.resetHideButtonsTimer()
                            navController.navigate(AppScreen.Clock.name)
                        }
                    )
                }
                composable(route = AppScreen.Clock.name) {
                    BaseClockScreen(
                        clockViewModel = clockViewModel,
                        alarmViewModel = alarmViewModel,
                        timerViewModel = timerViewModel,
                        onBackClick = {
                            navController.navigate(AppScreen.Home.name)
                        },
                        onSettingsClick = { navController.navigate(AppScreen.Settings.name) }
                    )
                }

                composable(AppScreen.Settings.name) {
                    SettingsScreen(
                        clockViewModel = clockViewModel,
                        onBackClick = {
                            clockViewModel.saveThemePreferences()
                            clockViewModel.resetHideButtonsTimer()
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}