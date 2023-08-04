package com.fourdevs.diuquestionbank.firebase

import android.content.Context
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.utilities.await
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FirebaseCloudStorageImpl @Inject constructor(
    private val storageRef: StorageReference,
    @ApplicationContext private val context: Context
) : FirebaseCloudStorage {

    override suspend fun downloadFile(fileName: String, path:String): Resource<Double> {
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

}