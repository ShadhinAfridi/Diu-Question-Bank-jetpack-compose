package com.fourdevs.diuquestionbank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.User
import com.fourdevs.diuquestionbank.modules.OfflineQualifier
import com.fourdevs.diuquestionbank.modules.OnlineQualifier
import com.fourdevs.diuquestionbank.repository.AuthRepository
import com.fourdevs.diuquestionbank.repository.CommonRepository
import com.fourdevs.diuquestionbank.repository.QuestionRepository
import com.fourdevs.diuquestionbank.utilities.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    @OnlineQualifier private val repositoryOnline: QuestionRepository,
    @OfflineQualifier private val repositoryOffline: QuestionRepository,
    private val commonRepository: CommonRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _questionCreateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val questionCreateFlow = _questionCreateFlow.asStateFlow()

    private val _questionDownloadFlow = MutableStateFlow<Resource<Double>?>(null)
    val questionDownloadFlow = _questionDownloadFlow.asStateFlow()

    var questions: Flow<PagingData<Question>>? = null

    private val _departmentCountFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val departmentCountFlow: StateFlow<Map<String, Int>> = _departmentCountFlow

    private val _courseCountFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val courseCountFlow: StateFlow<Map<String, Int>> = _courseCountFlow

    private val _userResponseFlow = MutableStateFlow<Map<String, User>>(emptyMap())
    val userResponseFlow: StateFlow<Map<String, User>> = _userResponseFlow

    val token = commonRepository.getSting(Constants.KEY_USER_TOKEN)


    fun createQuestion(newQuestion: Question) = viewModelScope.launch {
        _questionCreateFlow.value = Resource.Loading
        token?.let {
            _questionCreateFlow.value = repositoryOnline.createQuestion(newQuestion, it)
        }
    }


    fun downloadFile(fileName: String) = viewModelScope.launch {
        _questionDownloadFlow.value = Resource.Loading
        val result = repositoryOnline.downloadFile(fileName)
        _questionDownloadFlow.value = result
    }

    fun updateQuestion(id: String, isApproved: Int) =
        viewModelScope.launch {
            token?.let {
                repositoryOnline.updateQuestion(id, isApproved, it)
            }
        }


    fun getQuestionsByDepartment(department: String) = viewModelScope.launch {
        try {
            token?.let {
                questions =
                    repositoryOnline.getQuestionsByDepartment(department, it)
                        .cachedIn(viewModelScope)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getQuestionsByCourse(
        department: String,
        courseName: String,
        shift: String,
        exam: String
    ) = viewModelScope.launch {
        try {
            token?.let {
                questions =
                    repositoryOnline.getQuestionsByCourseName(
                        department,
                        courseName,
                        shift,
                        exam,
                        it
                    ).cachedIn(viewModelScope)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getQuestionCountByDepartment(department: String) = viewModelScope.launch {
        try {
            token?.let {
                val updatedCounts = repositoryOnline.getQuestionCountByDepartment(department, it)
                val updatedMap = _departmentCountFlow.value.toMutableMap()
                updatedMap[department] = updatedCounts
                _departmentCountFlow.value = updatedMap.toMap()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String
    ) = viewModelScope.launch {
        try {
            token?.let {
                val updatedCounts =
                    repositoryOnline.getQuestionCountByName(department, courseName, shift, exam, it)
                val updatedMap = _courseCountFlow.value.toMutableMap()
                updatedMap[courseName] = updatedCounts
                _courseCountFlow.value = updatedMap.toMap()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUploaderName(userId: String) = viewModelScope.launch {
        try {
            val userResponse = authRepository.getUserDataById(userId)
            _userResponseFlow.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[userId] = userResponse.message
                }
            }
        } catch (e: Exception) {
            // Handle the exception appropriately, e.g., log or show an error message
            e.printStackTrace()
        }
    }

}