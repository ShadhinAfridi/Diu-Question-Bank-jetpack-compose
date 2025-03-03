package com.fourdevs.diuquestionbank.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fourdevs.diuquestionbank.core.ApiClient
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.room.dao.QuestionDao

class QuestionPagingSource(
    private val apiClient: ApiClient,
    private val department: String,
    private val token: String
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

            val response = apiClient.getQuestionsByDepartment(department =  department, token = token, page = page)

            LoadResult.Page(
                data = response,
                prevKey = if(page==1) null else page-1,
                nextKey = if (response.isEmpty()) null else page+1
            )

        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}