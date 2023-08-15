package com.fourdevs.diuquestionbank.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun createQuestion(newQuestion: Question): Resource<Unit>
    suspend fun downloadFile(fileName: String): Resource<Double>
    suspend fun updateQuestion(id: String, isApproved: Int)
    suspend fun getQuestionsByDepartment(department: String): Flow<PagingData<Question>>
    suspend fun getQuestionsByCourseName(
        department: String,
        courseName: String,
        shift: String,
        exam: String
    ): Flow<PagingData<Question>>

    suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String
    ): Int

    suspend fun getQuestionCountByDepartment(department: String): Int

}