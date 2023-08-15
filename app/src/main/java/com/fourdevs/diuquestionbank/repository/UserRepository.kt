package com.fourdevs.diuquestionbank.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.models.UserInfo
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun createUserInfo(
        userInfo: UserInfo
    ) : String {
        return apiClient.createUserInfo(userInfo)
    }

    suspend fun getUserInfo(
        id: String
    ) : UserInfo? {
        return apiClient.getUserInfo(id)
    }

    suspend fun updateUserInfo(
        id: String,
        department: String,
        about: String
    ) : String {
        return apiClient.updateUserInfo(id, department, about)
    }

    suspend fun updateUserImage(
        id: String,
        image: String
    ) : String {
        return apiClient.updateUserImage(id, image)
    }

    fun bitmapFromEncodedString(encodedImage: String): Bitmap {
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}