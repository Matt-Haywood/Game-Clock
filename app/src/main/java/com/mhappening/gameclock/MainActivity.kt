package com.mhappening.gameclock

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.ads.MobileAds
import com.mhappening.gameclock.ui.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ActivityContext val activityContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        MobileAds.initialize(this) {}

        setContent {
            AppNavigation()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}



