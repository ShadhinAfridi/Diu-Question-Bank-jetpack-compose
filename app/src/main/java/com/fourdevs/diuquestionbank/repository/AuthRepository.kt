package com.fourdevs.diuquestionbank.repository

import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.core.InternetConnection
import com.fourdevs.diuquestionbank.core.SendEmail
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.ApiUserResponse
import com.fourdevs.diuquestionbank.utilities.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sendEmail: SendEmail,
    private val apiClient: ApiClient,
    private val internetConnection: InternetConnection
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






}