package com.fourdevs.diuquestionbank.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.repository.AuthRepository
import com.fourdevs.diuquestionbank.utilities.Constants
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow= _loginFlow.asStateFlow()

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow = _signupFlow.asStateFlow()

    private var _networkResponseFlow = MutableStateFlow<Resource<Unit>?>(null)
    val networkResponseFlow = _networkResponseFlow.asStateFlow()

    private var _passwordChangeFlow = MutableStateFlow<Resource<Unit>?>(null)
    val passwordChangeFlow = _passwordChangeFlow.asStateFlow()

    private val _userInfoFlow = MutableStateFlow<UserInfo?>(null)
    val userInfoFlow = _userInfoFlow.asStateFlow()

    private val _countDownFlow = MutableStateFlow("Resend in 02:00")
    val countDownFlow = _countDownFlow.asStateFlow()


    private val timerSeconds =  mutableIntStateOf(120)



    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signupUser(name: String, email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun logout() {
        repository.logout()
        repository.clearPreferences()
        _loginFlow.value = null
        _signupFlow.value = null
    }

    fun getUserInfo(
        id:String
    ) = viewModelScope.launch {
        _userInfoFlow.value = repository.getUserInfo(id)
    }

    fun createUserInfo(
        userInfo: UserInfo
    ) = viewModelScope.launch {
        repository.createUserInfo(userInfo)
    }

    fun getIdToken(user: FirebaseUser) = viewModelScope.launch{
        val result = repository.getIdToken(user)
        putString(Constants.KEY_USER_TOKEN, result)
    }

    fun sentVerificationEmail(recipientName:String,recipientEmail: String, otp: String) = viewModelScope.launch {
        repository.sentVerificationEmail(recipientName, recipientEmail, otp)
    }

    fun generateRandomNumber(): Int = Random.nextInt(100000, 999999)


    fun sendRecoverEmail (recipientEmail: String, otp: String) = viewModelScope.launch {
        repository.sendRecoveryEmail(recipientEmail, otp)
    }

    fun updateUserPassword(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _passwordChangeFlow.value = Resource.Loading
        val result = repository.updateUserPassword(email, password)
        _passwordChangeFlow.value = result
    }

    fun verifyUser(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        _networkResponseFlow.value = Resource.Loading
        val result = repository.verifyUser(userId)
        _networkResponseFlow.value = result
    }

    fun getUserByEmail(email: String) = viewModelScope.launch(Dispatchers.IO) {
        _networkResponseFlow.value = Resource.Loading
        val result = repository.getUserByEmail(email)
        _networkResponseFlow.value = result
    }

    fun sendWelcomeEmail(recipientName:String,recipientEmail: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.sendWelcomeEmail(recipientName, recipientEmail)
    }

    fun checkInternetConnection() :Boolean = repository.checkInternetConnection()

    fun putString(key:String, value:String) {
        repository.putString(key, value)
    }

    fun putBoolean(key:String, value:Boolean) {
        repository.putBoolean(key, value)
    }

    fun getString(key: String) : String? = repository.getString(key)

    fun getBoolean(key: String) : Boolean = repository.getBoolean(key)

    fun bitmapFromEncodedString(encodedImage: String): Bitmap {
        return repository.bitmapFromEncodedString(encodedImage)
    }

    fun startCountDown() = viewModelScope.launch {
        timerSeconds.intValue = 120
        while (timerSeconds.intValue > 0) {
            delay(1000)
            timerSeconds.intValue -= 1
            // Update the UI with the countdown timer
            _countDownFlow.value = "Resend in " +
                    String.format("%02d", timerSeconds.intValue / 60) +
                    ":" +
                    String.format("%02d", timerSeconds.intValue % 60)
        }
    }


}