package com.fourdevs.diuquestionbank.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.random.Random

class RealtimeDatabase @Inject constructor() {
    private val database = Firebase.database.reference
    private val bannerQuery = database.child("banner")
    private val interstitialQuery = database.child("interstitial")
    private val openAppQuery = database.child("openapp")
    private val settingsQuery = database.child("setting")


    fun fetchBannerAdId(callback: (String?) -> Unit) {

        bannerQuery
            .child("${generateRandomNumber()}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val interstitialAdId = dataSnapshot.getValue(String::class.java)
                callback(interstitialAdId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun fetchInterstitialAdId(callback: (String?) -> Unit) {
        interstitialQuery
            .child("${generateRandomNumber()}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val interstitialAdId = dataSnapshot.getValue(String::class.java)
                    callback(interstitialAdId)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun fetchOpenAppAdId(callback: (String?) -> Unit) {
        openAppQuery
            .child("${generateRandomNumber()}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val interstitialAdId = dataSnapshot.getValue(String::class.java)
                    callback(interstitialAdId)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun getVersionCode(versionCode: (Int) -> Unit) {
        settingsQuery.child("versionCode")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val version = dataSnapshot.getValue(Int::class.java)
                    version?.let{
                        versionCode(it)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    versionCode(0)
                }
            })
    }


    private fun generateRandomNumber() : Int {
        return Random.nextInt(101) // Generates a random number between 0 (inclusive) and 100 (exclusive)
    }

}