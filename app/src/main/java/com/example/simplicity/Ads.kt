package com.example.simplicity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdaptiveBanner(
    modifier: Modifier = Modifier,
) {
    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
    val applicationContext = LocalContext.current.applicationContext

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                // Using application context to avoid memory leak
                AdView(applicationContext).apply {
                    setAdSize(
                        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                            applicationContext,
                            deviceCurrentWidth,
                        ),
                    )
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            },
        )
    }
}
