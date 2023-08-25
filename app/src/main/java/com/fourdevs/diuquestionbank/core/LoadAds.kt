package com.fourdevs.diuquestionbank.core

import android.app.Activity
import android.util.Log
import com.fourdevs.diuquestionbank.firebase.RealtimeDatabase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import javax.inject.Inject

class LoadAds @Inject constructor(
    private val realtimeDatabase: RealtimeDatabase
) {

    fun showInterstitialAd(activity: Activity) {
        loadInterstitialAd(activity) { interstitialAd ->
            if (interstitialAd != null) {
                // Ad loaded successfully, you can now show the interstitial ad
                interstitialAd.show(activity)
            } else {
                // Ad loading failed or was not loaded, handle accordingly
            }
        }
    }
    private fun loadInterstitialAd(activity: Activity, callback: (InterstitialAd?) -> Unit) {
        val adRequest = AdRequest.Builder().build()
        val TAG = "Afridi"

        realtimeDatabase.fetchInterstitialAdId { interstitialAdId ->
            if (interstitialAdId != null) {
                InterstitialAd.load(
                    activity,
                    interstitialAdId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, adError.toString())
                            callback(null)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(TAG, "Ad was loaded. $interstitialAdId")
                            callback(interstitialAd)
                        }
                    }
                )
            } else {
                callback(null)
            }
        }
    }
}