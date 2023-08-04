package com.fourdevs.diuquestionbank.firebase

import com.fourdevs.diuquestionbank.data.Resource

interface FirebaseCloudStorage {
    suspend fun downloadFile(fileName : String, path:String) : Resource<Double>
}