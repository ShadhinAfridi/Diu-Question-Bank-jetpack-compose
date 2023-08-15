package com.fourdevs.diuquestionbank.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.repository.CommonRepository
import com.fourdevs.diuquestionbank.repository.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repositoryCommon: CommonRepository
) : ViewModel(){

    private val _apiResponseFlow = MutableStateFlow<String?>(null)
    val apiResponseFlow = _apiResponseFlow.asStateFlow()

    fun createUserInfo(
        userInfo: UserInfo
    ) = viewModelScope.launch {
        _apiResponseFlow.value = userRepository.createUserInfo(userInfo)
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
        repositoryCommon.putString(key, value)
    }

    fun putBoolean(key:String, value:Boolean) {
        repositoryCommon.putBoolean(key, value)
    }

    fun getString(key: String) : String? = repositoryCommon.getSting(key)

    fun getBoolean(key: String) : Boolean = repositoryCommon.getBoolean(key)

    fun bitmapFromEncodedString(encodedImage: String): Bitmap {
        return userRepository.bitmapFromEncodedString(encodedImage)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        return userRepository.bitmapToBase64(bitmap)
    }

}