package com.fourdevs.diuquestionbank.core

import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.ApiResponse
import com.fourdevs.diuquestionbank.models.ApiUserResponse
import com.fourdevs.diuquestionbank.models.UserInfo

interface ApiClient {

    //Auth
    suspend fun getUserById(id: String): ApiUserResponse
    suspend fun updateUserPassword(email: String, password: String): Resource<Unit>
    suspend fun verifyUser(userId: String): Resource<Unit>
    suspend fun getUserByEmail(email: String): Resource<Unit>


    //Questions
    suspend fun createQuestion(newQuestion: Question, token: String): Resource<Unit>
    suspend fun getQuestionsByDepartment(
        department: String,
        token: String,
        page: Int
    ): List<Question>

    suspend fun getQuestionsByCourseName(
        department: String,
        token: String,
        page: Int,
        courseName: String,
        shift: String,
        exam: String
    ): List<Question>

    suspend fun updateQuestion(
        id: String,
        isApproved: Int,
        token: String
    )

    suspend fun getQuestionCountByDepartment(
        department: String,
        token: String
    ) : Int

    suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ) : Int

    suspend fun createUserInfo(
        userInfo: UserInfo
    ) : String

    suspend fun getUserInfo(
        id: String
    ) : UserInfo?

    suspend fun updateUserInfo(
        id: String,
        department: String,
        about: String
    ) : String

    suspend fun updateUserImage(
        id: String,
        image: String
    ) : String



}