package com.mhappening.gameclock.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mhappening.gameclock.MainActivity
import com.mhappening.gameclock.ui.AppNavigation
import com.mhappening.gameclock.ui.AppScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var navController: TestNavHostController


    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        // Create a ComposeNavigator and set it as the navigator for your TestNavHostController
        val composeNavigator = ComposeNavigator()
        navController.navigatorProvider.addNavigator(composeNavigator)

        composeTestRule.setContent {
            AppNavigation(navController = navController)
        }
    }

    @Test
    fun testNavigationToSettingsScreen() {
        // Perform click action on settings button
        composeTestRule.onNodeWithContentDescription("Settings").performClick()

        // Verify that we navigated to the settings screen
        assert(navController.currentDestination?.id == AppScreen.Settings.title)
    }

    @Test
    fun testNavigationToHomeScreen() {
        // Perform click action on home button
        composeTestRule.onNodeWithContentDescription("Theme choice").performClick()

        // Verify that we navigated to the settings screen
        assert(navController.currentDestination?.id == AppScreen.Home.title)
    }
}