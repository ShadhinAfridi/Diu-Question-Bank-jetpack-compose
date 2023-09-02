package com.fourdevs.diuquestionbank.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.room.dao.QuestionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class UserPagingSource (
    private val apiClient: ApiClient,
    private val userId: String,
    private val token: String,
    private val questionDao: QuestionDao
) : PagingSource<Int, Question>(){
    override fun getRefreshKey(state: PagingState<Int, Question>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {

        return try{

            val page = params.key ?: 1

            val onlineResponse = apiClient.getQuestionsByUser(userId, token, page)
            var response: List<Question>
            withContext(Dispatchers.IO + NonCancellable) {
                questionDao.insertAll(onlineResponse)
                response = questionDao.getQuestionByUser(userId)
            }
            LoadResult.Page(
                data = response,
                prevKey = if(page==1) null else page-1,
                nextKey = if (onlineResponse.isEmpty()) null else page+1
            )

        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}