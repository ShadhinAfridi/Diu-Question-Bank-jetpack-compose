package com.fourdevs.diuquestionbank.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.firebase.FirebaseCloudStorage
import com.fourdevs.diuquestionbank.paging.CoursePagingSource
import com.fourdevs.diuquestionbank.paging.QuestionPagingSource
import com.fourdevs.diuquestionbank.paging.UserPagingSource
import com.fourdevs.diuquestionbank.room.dao.QuestionDao
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.utilities.PreferenceManager
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionRepositoryOnline @Inject constructor(
    private val apiClient: ApiClient,
    private val storage: FirebaseCloudStorage,
    private val questionDao: QuestionDao,
    private val preferenceManager: PreferenceManager
) : QuestionRepository {


    override suspend fun createQuestion(
        newQuestion: Question,
        token: String,
        callback: (Boolean) -> Unit
    ) {
        return apiClient.createQuestion(newQuestion, token, callback)
    }

    override suspend fun downloadFile(fileName: String): Resource<Double> {
        return storage.downloadFile(fileName, Constants.KEY_QUESTIONS)
    }

    override suspend fun uploadFile(
        uri: Uri,
        question: Question,
        taskSnapshot: (UploadTask.TaskSnapshot) -> Unit,
        callback: (Question) -> Unit
    ) {
        preferenceManager.getString(Constants.KEY_USER_ID)?.let {
            question.uploaderId = it
            storage.uploadFile(uri, question, taskSnapshot, callback)
        }

    }

    override suspend fun updateQuestion(
        id: String,
        isApproved: Int,
        token: String
    ) {
        apiClient.updateQuestion(id, isApproved, token)
    }

    override suspend fun getQuestionsByDepartment(
        department: String,
        token: String
    ): Flow<PagingData<Question>> {
        val pager = Pager(PagingConfig(pageSize = 10, prefetchDistance = 5)) {
            QuestionPagingSource(apiClient, department, token)
        }

        return pager.flow
    }

    override suspend fun getQuestionsByUser(
        userId: String,
        token: String
    ): Flow<PagingData<Question>> {
        val pager = Pager(PagingConfig(pageSize = 10, prefetchDistance = 5)) {
            UserPagingSource(apiClient, userId, token, questionDao)
        }

        return pager.flow
    }

    override suspend fun getQuestionsByCourseName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Flow<PagingData<Question>> {
        val pager = Pager(PagingConfig(pageSize = 10, prefetchDistance = 5)) {
            CoursePagingSource(apiClient, department, token, courseName, shift, exam, questionDao)
        }
        return pager.flow
    }

    override suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Int {

        return apiClient.getQuestionCountByName(department, courseName, shift, exam, token)
    }

    override suspend fun getQuestionCountByDepartment(
        department: String,
        token: String
    ): Int {
        return apiClient.getQuestionCountByDepartment(department, token)
    }

    override fun getToken() = preferenceManager.getString(Constants.KEY_USER_TOKEN)


}