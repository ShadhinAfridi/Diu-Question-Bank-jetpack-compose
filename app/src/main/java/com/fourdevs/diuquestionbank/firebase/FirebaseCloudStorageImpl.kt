package com.fourdevs.diuquestionbank.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.utilities.await
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.Date
import javax.inject.Inject


class FirebaseCloudStorageImpl @Inject constructor(
    private val storageRef: StorageReference,
    private val apiClient: ApiClient,
    @ApplicationContext private val context: Context
) : FirebaseCloudStorage {

    override suspend fun downloadFile(fileName: String, path: String): Resource<Double> {
        val storageReference = storageRef.child(fileName)
        val rootPath = File(context.cacheDir, "/$path")
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val file = File(rootPath, fileName)
        return if (file.exists()) {
            Resource.Success(100.0)
        } else {
            return try {
                val task = storageReference.getFile(file).await()
                val progress = (100.0 * task.bytesTransferred) / task.totalByteCount
                Resource.Success(progress)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }
        }
    }

    override suspend fun uploadFile(
        uri: Uri,
        question: Question,
        taskSnapshot: (UploadTask.TaskSnapshot) -> Unit,
        callback: (Question) -> Unit
    ) {
        val timestamp: Long = System.currentTimeMillis()
        val questionId = "${timestamp}${question.uploaderId}"
        val filepath = storageRef
            .child("$questionId.pdf")
        question.link = filepath.name
        question.questionId = questionId
        callback(question)
        val uploadTask = filepath.putFile(uri).await()
        taskSnapshot(uploadTask)
    }

}