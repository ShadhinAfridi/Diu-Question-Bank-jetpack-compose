package com.fourdevs.diuquestionbank.firebase

import android.net.Uri
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.google.firebase.storage.UploadTask

interface FirebaseCloudStorage {
    suspend fun downloadFile(fileName: String, path: String): Resource<Double>
    suspend fun uploadFile(
        uri: Uri,
        question: Question,
        taskSnapshot: (UploadTask.TaskSnapshot) -> Unit,
        callback: (Question) -> Unit
    )
}