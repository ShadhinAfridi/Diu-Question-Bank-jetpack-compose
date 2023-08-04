package com.fourdevs.diuquestionbank.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun createQuestion(newQuestion: Question): Resource<Unit>
    suspend fun downloadFile(fileName:String) : Resource<Double>
    suspend fun updateQuestion(id:String, code:String, name:String, lt:String, isApproved:Int)
    suspend fun getQuestionsByDepartment(department : String) : Flow<PagingData<Question>>
}