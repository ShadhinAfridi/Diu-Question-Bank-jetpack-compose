package com.fourdevs.diuquestionbank.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.User
import com.fourdevs.diuquestionbank.repository.AuthRepository
import com.fourdevs.diuquestionbank.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val repositoryOnline: QuestionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _questionDownloadFlow = MutableStateFlow<Resource<Double>?>(null)
    val questionDownloadFlow = _questionDownloadFlow.asStateFlow()

    var questions: Flow<PagingData<Question>>? = null

    private val _departmentCountFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val departmentCountFlow = _departmentCountFlow.asStateFlow()

    private val _courseCountFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val courseCountFlow = _courseCountFlow.asStateFlow()

    private val _userResponseFlow = MutableStateFlow<Map<String, User>>(emptyMap())
    val userResponseFlow = _userResponseFlow.asStateFlow()

    private val token = repositoryOnline.getToken()

    private val _uploadProgressFlow =  MutableStateFlow<String?>(null)
    val uploadProgressFlow =  _uploadProgressFlow.asStateFlow()

    private val _uploadCompleteFlow =  MutableStateFlow<Boolean?>(null)
    val uploadCompleteFlow =  _uploadCompleteFlow.asStateFlow()

    private val _uploadLoadingFlow =  MutableStateFlow(false)
    val uploadLoadingFlow =  _uploadLoadingFlow.asStateFlow()



    fun downloadFile(fileName: String) = viewModelScope.launch {
        _questionDownloadFlow.value = Resource.Loading
        val result = repositoryOnline.downloadFile(fileName)
        _questionDownloadFlow.value = result
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

    fun uploadFile(
        uri: Uri,
        question: Question
    ) = viewModelScope.launch {
        token?.let { key ->
            repositoryOnline.uploadFile(
                uri,
                question,
                taskSnapshot = { taskSnapshot->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    _uploadProgressFlow.value = "$progress% Uploaded"
                    _uploadLoadingFlow.value = taskSnapshot.task.isInProgress
                }
            ) { question->
                viewModelScope.launch {
                    repositoryOnline.createQuestion(question, key) {
                        _uploadCompleteFlow.value = it
                        viewModelScope.launch {
                            delay(200)
                            _uploadProgressFlow.value = null
                            _uploadCompleteFlow.value = null
                        }
                    }
                }
            }
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