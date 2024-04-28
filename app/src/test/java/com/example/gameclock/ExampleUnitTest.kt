package com.example.gameclock

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gameclock.ui.AppNavigation
import com.example.gameclock.ui.AppScreen
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

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeTestRule.setContent {
            AppNavigation(navController = navController)
        }
    }

    @Test
    fun testNavigationToSettingsScreen() {
        // Perform click action on settings button
        composeTestRule.onNodeWithTag("settingsButton").performClick()

        // Verify that we navigated to the settings screen
        assert(navController.currentDestination?.id == AppScreen.Settings.title)
    }

    @Test
    fun testNavigationToHomeScreen() {
        // Perform click action on home button
        composeTestRule.onNodeWithTag("homeButton").performClick()

        // Verify that we navigated to the settings screen
        assert(navController.currentDestination?.id == AppScreen.Home.title)
    }
}