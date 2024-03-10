package com.example.gameclock.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
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

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    Clock(title = R.string.clock),
    Settings(title = R.string.settings)
}

@Composable
fun AppNavigation(
    clockViewModel: ClockViewModel,
    navController: NavHostController = rememberNavController()
) {


    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,


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
        composable(AppScreen.Clock.name) {
            BaseClockScreen(
                clockViewModel = clockViewModel,
                onBackClick = {
                    //changes the app theme back to default before displaying the home screen
                    clockViewModel.onThemeChange(
                        ThemeChangeEvent.ThemeChange(
                            AppTheme.Default
                        )
                    )
                    navController.popBackStack()
                },
                onSettingsClick = {navController.navigate(AppScreen.Settings.name)}
            )
        }

        composable(AppScreen.Settings.name) {
            SettingsScreen(
                clockViewModel = clockViewModel,
                onBackClick = {navController.navigateUp()}
            )
        }
    }
}