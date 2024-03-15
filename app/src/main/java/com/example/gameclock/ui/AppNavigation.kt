package com.example.gameclock.ui

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gameclock.R
import com.example.gameclock.data.ClockThemeList
import com.example.gameclock.model.AppTheme
import com.example.gameclock.ui.screens.BaseClockScreen
import com.example.gameclock.ui.screens.HomeScreen
import com.example.gameclock.ui.screens.SettingsScreen
import com.example.gameclock.ui.theme.GameClockTheme

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    Clock(title = R.string.clock),
    Settings(title = R.string.settings)
}

//TODO: Add logic for system back button to remove theming.
//TODO: Sort out animation from clock screen to home screen suddenly popping
//TODO: Add navigation tests + testTags
@Composable
fun AppNavigation(
    clockViewModel: ClockViewModel = viewModel(factory = ClockViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {

    GameClockTheme(appTheme = clockViewModel.uiState.collectAsState().value.theme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Home.name,
                enterTransition = {
                    expandIn(
                        expandFrom = Alignment.Center,
                    )
                    fadeIn()
                },
                exitTransition = {
                    shrinkOut(
                        shrinkTowards = Alignment.Center,
                    )
                    fadeOut(animationSpec =  tween( durationMillis = 10) )
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
                            navController.navigate(AppScreen.Clock.name)
                        }
                    )
                }
                composable(route = AppScreen.Clock.name,

                    ) {
                    BaseClockScreen(
                        clockViewModel = clockViewModel,
                        onBackClick = {
                            navController.navigateUp()
                            //changes the app theme back to default before displaying the home screen
                            clockViewModel.onThemeChange(
                                ThemeChangeEvent.ThemeChange(
                                    AppTheme.Default
                                )
                            )

                        },
                        onSettingsClick = { navController.navigate(AppScreen.Settings.name) }
                    )
                }

                composable(AppScreen.Settings.name) {
                    SettingsScreen(
                        clockViewModel = clockViewModel,
                        onBackClick = {
                            clockViewModel.saveThemePreferences()
                            navController.navigateUp() }
                    )
                }
            }
        }
    }
}