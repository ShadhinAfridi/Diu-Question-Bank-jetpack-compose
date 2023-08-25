package com.fourdevs.diuquestionbank.repository

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.core.LoadAds
import com.fourdevs.diuquestionbank.core.Permissions
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.firebase.RealtimeDatabase
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.modules.OnlineQualifier
import kotlinx.coroutines.flow.Flow
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val loadAds: LoadAds,
    private val realtimeDatabase: RealtimeDatabase,
    @OnlineQualifier
    private val repositoryOnline: QuestionRepository,
    private val repositoryCommon: CommonRepository,
    private val permission: Permissions
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

    fun showInterstitialAd(activity: Activity) {
        loadAds.showInterstitialAd(activity)
    }

    fun fetchBannerAdId(callback: (String?) -> Unit) {
        realtimeDatabase.fetchBannerAdId(callback)
    }

    suspend fun getQuestionsByUser(userId: String, token: String): Flow<PagingData<Question>> {
        return repositoryOnline.getQuestionsByUser(userId, token)
    }

    fun putString(key:String, value:String) {
        repositoryCommon.putString(key, value)
    }

    fun putBoolean(key:String, value:Boolean) {
        repositoryCommon.putBoolean(key, value)
    }

    fun getString(key: String) : String? = repositoryCommon.getSting(key)

    fun getBoolean(key: String) : Boolean = repositoryCommon.getBoolean(key)

    fun checkPermissions(): Boolean {
        return permission.checkPermissions()
    }
    fun askPermission(activity: Activity) {
        permission.askPermission(activity)
    }




}