package com.mhappening.gameclock.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd() {

    AndroidView(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        factory = { context ->
            AdView(context).apply {
                // on below line specifying ad size
                setAdSize(AdSize.BANNER)
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }
        }
    )
    //test code: ca-app-pub-3940256099942544/6300978111
// real code: ca-app-pub-4097959135623476/8738801968

}