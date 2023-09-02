package com.fourdevs.diuquestionbank.repository

import android.graphics.Bitmap
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.core.InternetConnection
import com.fourdevs.diuquestionbank.core.SendEmail
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.ApiUserResponse
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.utilities.PreferenceManager
import com.fourdevs.diuquestionbank.utilities.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sendEmail: SendEmail,
    private val apiClient: ApiClient,
    private val internetConnection: InternetConnection,
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) {

    //Firebase Auth
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun getIdToken(user: FirebaseUser): String {
        return try {
            val result = user.getIdToken(true).await()
            result.token!!
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    //Email
    suspend fun sentVerificationEmail(recipientName:String,recipientEmail: String, otp: String) {
        sendEmail.sendVerificationEmail(recipientName, recipientEmail, otp)
    }
    suspend fun sendRecoveryEmail(recipientEmail: String, otp: String) {
        sendEmail.sendPassRecoverEmail(recipientEmail, otp)
    }
    suspend fun sendWelcomeEmail(recipientName: String, recipientEmail: String) {
        sendEmail.sendWelcomeEmail(recipientName, recipientEmail)
    }



    //K-tor
    suspend fun updateUserPassword(email: String, password: String): Resource<Unit> {
        return apiClient.updateUserPassword(email, password)
    }

    suspend fun getUserByEmail(email: String): Resource<Unit> {
        return apiClient.getUserByEmail(email)
    }

    suspend fun verifyUser(userId: String): Resource<Unit> {
        return apiClient.verifyUser(userId)
    }

    suspend fun getUserDataById(userId: String): ApiUserResponse {
        return apiClient.getUserById(userId)
    }



    //Others
    fun checkInternetConnection(): Boolean {
        return internetConnection.checkForInternet()
    }

    fun bitmapFromEncodedString(encodedImage: String): Bitmap {
        return userRepository.bitmapFromEncodedString(encodedImage)
    }

    fun putString(key:String, value:String) {
        preferenceManager.putString(key, value)
    }

    fun putBoolean(key:String, value:Boolean) {
        preferenceManager.putBoolean(key, value)
    }

    fun getString(key: String) : String? = preferenceManager.getString(key)

    fun getBoolean(key: String) : Boolean = preferenceManager.getBoolean(key)

    fun clearPreferences() {
        preferenceManager.clear()
    }

    suspend fun getUserInfo(id:String) : UserInfo?{
        return userRepository.getUserInfo(id)
    }

    suspend fun createUserInfo(userInfo: UserInfo) {
        userRepository.createUserInfo(userInfo)
    }






}