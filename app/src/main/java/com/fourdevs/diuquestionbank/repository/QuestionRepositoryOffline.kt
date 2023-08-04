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
    override suspend fun createQuestion(newQuestion: Question): Resource<Unit> {
        return try{
            val result = questionDao.insertQuestion(newQuestion)
            Resource.Success(result)
        } catch (e:Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }


    override suspend fun downloadFile(fileName: String): Resource<Double> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuestion(
        id: String,
        code: String,
        name: String,
        lt: String,
        isApproved: Int
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionsByDepartment(
        department: String
    ): Flow<PagingData<Question>> {
        TODO("Not yet implemented")
    }


}