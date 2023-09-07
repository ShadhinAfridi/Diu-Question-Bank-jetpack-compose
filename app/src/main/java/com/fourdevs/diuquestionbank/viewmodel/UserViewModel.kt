package com.fourdevs.diuquestionbank.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.models.HelpRequest
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.repository.UserRepository
import com.fourdevs.diuquestionbank.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){

    private val _apiResponseFlow = MutableStateFlow<String?>(null)
    val apiResponseFlow = _apiResponseFlow.asStateFlow()

    private val _userInfoFlow = MutableStateFlow<UserInfo?>(null)
    val userInfoFlow = _userInfoFlow.asStateFlow()

    private val _addHelpFlow = MutableStateFlow(true)
    val addHelpFlow = _addHelpFlow.asStateFlow()

    var questions: Flow<PagingData<Question>> = emptyFlow()

    private val _systemThemeFlow = MutableStateFlow(userRepository.getThemeBoolean(Constants.KEY_SYSTEM_MODE))
    val systemThemeFlow = _systemThemeFlow.asStateFlow()

    fun updateTheme(systemTheme:Boolean) {
        _systemThemeFlow.value = systemTheme
        userRepository.putBoolean(Constants.KEY_SYSTEM_MODE, systemTheme)
    }

    fun getUserInfo(
        id:String
    ) = viewModelScope.launch {
        _userInfoFlow.value = userRepository.getUserInfo(id)
    }
    fun updateUserInfo(
        id:String,
        department: String,
        about: String
    ) = viewModelScope.launch {
        _apiResponseFlow.value = userRepository.updateUserInfo(id, department, about)
    }

    fun updateUserImage(
        id:String,
        image: String
    ) = viewModelScope.launch {
        _apiResponseFlow.value = userRepository.updateUserImage(id, image)
    }

    fun putString(key:String, value:String) {
        userRepository.putString(key, value)
    }

    fun getString(key: String) : String? = userRepository.getString(key)

    fun bitmapFromEncodedString(encodedImage: String): Bitmap {
        return userRepository.bitmapFromEncodedString(encodedImage)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        return userRepository.bitmapToBase64(bitmap)
    }

    fun getQuestionsByUser(userId: String) = viewModelScope.launch {
        try {
            userRepository.getString(Constants.KEY_USER_TOKEN)?.let {
                questions =
                    userRepository.getQuestionsByUser(userId, it).cachedIn(viewModelScope)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showInterstitialAd(activity: Activity) {
        userRepository.showInterstitialAd(activity)
    }

    fun fetchBannerAdId(callback: (String?) -> Unit) {
        userRepository.fetchBannerAdId(callback)
    }

    fun checkPermissions() = userRepository.checkPermissions()
    fun askPermission(activity: Activity) {
        userRepository.askPermission(activity)
    }

    fun checkInternetConnection() = userRepository.checkInternetConnection()

    fun addHelpRequest(helpRequest: HelpRequest) = viewModelScope.launch {
        userRepository.addHelpResponse(helpRequest) {
            _addHelpFlow.value = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun checkForAppUpdates(context: Context, updateListener: (Boolean) -> Unit) {

        val pInfo = context.packageManager.getPackageInfo(Constants.KEY_PACKAGE_NAME, 0)

        val versionCode = pInfo.longVersionCode.toInt()

        userRepository.getVersionCode {
            if(it > versionCode) {
                updateListener(true)
            } else {
                updateListener(false)
            }
        }

    }



}