package com.fourdevs.diuquestionbank.repository

import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun createQuestion(newQuestion: Question, token: String): Resource<Unit>
    suspend fun downloadFile(fileName: String): Resource<Double>
    suspend fun updateQuestion(id: String, isApproved: Int, token: String)
    suspend fun getQuestionsByDepartment(department: String, token: String): Flow<PagingData<Question>>
    suspend fun getQuestionsByUser(userId: String, token: String): Flow<PagingData<Question>>
    suspend fun getQuestionsByCourseName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Flow<PagingData<Question>>

    suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Int

    suspend fun getQuestionCountByDepartment(department: String, token: String): Int


}