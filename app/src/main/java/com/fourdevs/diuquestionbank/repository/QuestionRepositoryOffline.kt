package com.fourdevs.diuquestionbank.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.room.dao.QuestionDao
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class QuestionRepositoryOffline @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    override suspend fun createQuestion(newQuestion: Question, token: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun downloadFile(fileName: String): Resource<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuestion(id: String, isApproved: Int, token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionsByDepartment(
        department: String,
        token: String
    ): Flow<PagingData<Question>> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionsByUser(
        userId: String,
        token: String
    ): Flow<PagingData<Question>> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionsByCourseName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Flow<PagingData<Question>> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionCountByDepartment(department: String, token: String): Int {
        TODO("Not yet implemented")
    }


}