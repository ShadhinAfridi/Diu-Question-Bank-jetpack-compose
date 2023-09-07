package com.fourdevs.diuquestionbank.ui.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
    var adUnitIdState by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(true) {
        userViewModel.fetchBannerAdId{ adUnitId ->
            adUnitId?.let {
                adUnitIdState = it
            }
        }
    }

    adUnitIdState?.let {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(adSize)
                    adUnitId = it
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }


}


