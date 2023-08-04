package com.fourdevs.diuquestionbank.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.firebase.FirebaseCloudStorage
import com.fourdevs.diuquestionbank.paging.QuestionPagingSource
import com.fourdevs.diuquestionbank.utilities.Constants
import com.fourdevs.diuquestionbank.utilities.PreferenceManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionRepositoryOnline @Inject constructor(
    private val apiClient: ApiClient,
    private val preferenceManager: PreferenceManager,
    private val storage: FirebaseCloudStorage
) : QuestionRepository {


    override suspend fun createQuestion(newQuestion: Question): Resource<Unit> {
        return apiClient.createQuestion(newQuestion, getToken())
    }

    override suspend fun downloadFile(fileName: String) : Resource<Double> {
        return storage.downloadFile(fileName, Constants.KEY_QUESTIONS)
    }

    override suspend fun updateQuestion(
        id: String,
        code: String,
        name: String,
        lt: String,
        isApproved: Int
    ) {
        apiClient.updateQuestion(id, code, name, lt, isApproved, getToken())
    }

    override suspend fun getQuestionsByDepartment(department: String): Flow<PagingData<Question>> {
        val pager = Pager(PagingConfig(pageSize = 10, prefetchDistance = 5)) {
            QuestionPagingSource(apiClient, department, getToken())
        }

        return pager.flow
    }


    private fun getToken(): String = preferenceManager.getString(Constants.KEY_USER_TOKEN)!!
}