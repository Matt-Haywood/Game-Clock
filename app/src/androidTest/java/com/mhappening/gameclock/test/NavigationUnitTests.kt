package com.mhappening.gameclock.test


import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController

import com.mhappening.gameclock.ui.AppNavigation
import com.mhappening.gameclock.ui.AppScreen

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationUnitTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            AppNavigation(navController = navController)
        }
    }

    @Test
    fun homeScreenNavigatesToClockScreenOnThemeClick() {
        composeTestRule.onNodeWithTag("ThemeItem").performClick()
navController.assertCurrentRouteName(AppScreen.Clock.name)
    }

//    @Unroll
//    @ComposeTestRule
//    fun `ClockScreen navigates back to HomeScreen on back click`() {
//        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        val clockViewModel: ClockViewModel = mock()
//
//        composeTestRule.setContent {
//            AppNavigation(clockViewModel, navController)
//        }
//
//        navController.navigate(AppScreen.Clock.name)
//
//        onNodeWithTag("BackButton").performClick()
//
//        assertThat(navController.currentDestination?.route).isEqualTo(AppScreen.Home.name)
//    }
//
//    @Unroll
//    @ComposeTestRule
//    fun `ClockScreen navigates to SettingsScreen on settings click`() {
//        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        val clockViewModel: ClockViewModel = mock()
//
//        composeTestRule.setContent {
//            AppNavigation(clockViewModel, navController)
//        }
//
//        navController.navigate(AppScreen.Clock.name)
//
//        onNodeWithTag("SettingsButton").performClick()
//
//        assertThat(navController.currentDestination?.route).isEqualTo(AppScreen.Settings.name)
//    }
//
//    @Unroll
//    @ComposeTestRule
//    fun `SettingsScreen navigates back to HomeScreen on back click`() {
//        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        val clockViewModel: ClockViewModel = mock()
//
//        composeTestRule.setContent {
//            AppNavigation(clockViewModel, navController)
//        }
//
//        navController.navigate(AppScreen.Settings.name)
//
//        onNodeWithTag("BackButton").performClick()
//
//        assertThat(navController.currentDestination?.route).isEqualTo(AppScreen.Home.name)
//    }
}