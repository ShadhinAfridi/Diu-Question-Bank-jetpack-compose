package com.fourdevs.diuquestionbank.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.modules.OfflineQualifier
import com.fourdevs.diuquestionbank.modules.OnlineQualifier
import com.fourdevs.diuquestionbank.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    @OnlineQualifier private val repositoryOnline: QuestionRepository,
    @OfflineQualifier private val repositoryOffline: QuestionRepository
) : ViewModel() {


    private val _questionCreateFlow = MutableStateFlow<Resource<Unit>?>(null)
    val questionCreateFlow = _questionCreateFlow.asStateFlow()

    private val _questionDownloadFlow = MutableStateFlow<Resource<Double>?>(null)
    val questionDownloadFlow = _questionDownloadFlow.asStateFlow()

    var questions : Flow<PagingData<Question>>? = null








    fun createQuestion(newQuestion: Question) = viewModelScope.launch {
        _questionCreateFlow.value = Resource.Loading
        val result = repositoryOnline.createQuestion(newQuestion)
        _questionCreateFlow.value = result
    }


    fun downloadFile(fileName: String) = viewModelScope.launch {
        _questionDownloadFlow.value = Resource.Loading
        val result = repositoryOnline.downloadFile(fileName)
        _questionDownloadFlow.value = result
    }

    fun updateQuestion(id: String, code: String, name: String, lt: String, isApproved: Int) =
        viewModelScope.launch {
            repositoryOnline.updateQuestion(id, code, name, lt, isApproved)
        }


    fun getQuestionsByDepartment(department: String) = viewModelScope.launch {
        try{
            questions  = repositoryOnline.getQuestionsByDepartment(department).cachedIn(viewModelScope)
        } catch (e:Exception) {
            Log.d("Afridi", e.message!!)
        }


    }

}