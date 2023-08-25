package com.fourdevs.diuquestionbank.ui.ads

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.fourdevs.diuquestionbank.viewmodel.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun Ads() {

}

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adSize: AdSize,
    userViewModel: UserViewModel
) {
    var adUnitIdState by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(true) {
        userViewModel.fetchBannerAdId{ adUnitId ->
            adUnitId?.let {
                adUnitIdState = it
            }
        }
    }

    adUnitIdState?.let {
        Log.d("Afridi", "Banner -$it")
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                // on below line specifying ad view.
                AdView(context).apply {
                    // on below line specifying ad size
                    //adSize = AdSize.BANNER
                    // on below line specifying ad unit id
                    // currently added a test ad unit id.
                    setAdSize(adSize)
                    adUnitId = it
                    // calling load ad to load our ad.
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }


}


