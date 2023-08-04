package com.fourdevs.diuquestionbank.core

import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.data.UserResponse

interface ApiClient {

    //Auth
    suspend fun getUserById(id: String): UserResponse
    suspend fun updateUserPassword(email: String, password: String): Resource<Unit>
    suspend fun verifyUser(userId: String): Resource<Unit>
    suspend fun getUserByEmail(email: String): Resource<Unit>

    //Questions
    suspend fun createQuestion(newQuestion: Question, token: String): Resource<Unit>
    suspend fun getQuestionsByDepartment(department: String, token: String, page: Int): List<Question>
    suspend fun updateQuestion(id:String, code:String, name:String, lt:String, isApproved:Int, token: String)



}