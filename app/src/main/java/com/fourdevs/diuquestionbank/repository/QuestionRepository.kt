package com.fourdevs.diuquestionbank.repository

import android.net.Uri
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun createQuestion(newQuestion: Question, token: String, callback: (Boolean) -> Unit)
    suspend fun downloadFile(fileName: String): Resource<Double>
    suspend fun uploadFile(
        uri: Uri,
        question: Question,
        taskSnapshot: (UploadTask.TaskSnapshot) -> Unit,
        callback: (Question) -> Unit
    )

    suspend fun updateQuestion(id: String, isApproved: Int, token: String)
    suspend fun getQuestionsByDepartment(
        department: String,
        token: String
    ): Flow<PagingData<Question>>

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

    fun getToken() : String?


}