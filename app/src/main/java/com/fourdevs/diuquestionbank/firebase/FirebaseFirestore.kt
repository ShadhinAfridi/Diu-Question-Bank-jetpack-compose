package com.fourdevs.diuquestionbank.firebase

import com.fourdevs.diuquestionbank.models.HelpRequest
import com.fourdevs.diuquestionbank.utilities.await
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseFirestore @Inject constructor() {
    private val db = Firebase.firestore

    fun addHelpResponse(helpRequest: HelpRequest, callback: (Boolean) -> Unit) {
        try{
            db.collection("helps")
                .add(helpRequest)
                .addOnSuccessListener { callback(true) }
                .addOnFailureListener { e ->
                    run {
                        e.printStackTrace()
                        callback(false)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false)
        }

    }

}