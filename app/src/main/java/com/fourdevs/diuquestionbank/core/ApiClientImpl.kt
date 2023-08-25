package com.fourdevs.diuquestionbank.core

import android.util.Log
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.QuestionsResponse
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.models.ApiResponse
import com.fourdevs.diuquestionbank.models.ApiUserInfo
import com.fourdevs.diuquestionbank.models.ApiUserResponse
import com.fourdevs.diuquestionbank.models.CountResponse
import com.fourdevs.diuquestionbank.models.UserInfo
import com.fourdevs.diuquestionbank.utilities.await
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.contentType
import javax.inject.Inject


class ApiClientImpl @Inject constructor(
    private val client: HttpClient
) : ApiClient {
    private val apiKey = "123456789"
    private val keyName = "x-api-key"
    private val baseUrl = "https://app.techerax.com"

    private var count = 0
    private var count1 = 0


    override suspend fun getUserById(id: String): ApiUserResponse {
        return client
            .get("$baseUrl/users/${id}") {
                headers {
                    append(keyName, apiKey)
                }
            }
            .await()
    }

    override suspend fun getUserByEmail(email: String): Resource<Unit> {
        return try {
            val result = client
                .post("$baseUrl/users/check-user") {
                    headers {
                        append(keyName, apiKey)
                    }
                    contentType(ContentType.Application.Json)
                    setBody("""{"email": "$email"}""")
                }.await<ApiResponse>()
            return if (result.success == 1) {
                Resource.Success(Unit)
            } else {
                Resource.Failure(Exception("User not available!"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserPassword(email: String, password: String): Resource<Unit> {
        return try {
            val result = client
                .post("$baseUrl/users/update-password") {
                    headers {
                        append(keyName, apiKey)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(
                        """{
                        "email": "$email",
                        "newPassword":"$password"
                        }""".trimMargin()
                    )
                }.await<ApiResponse>()
            return if (result.success == 1) {
                Resource.Success(Unit)
            } else {
                Resource.Failure(Exception(result.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun verifyUser(userId: String): Resource<Unit> {
        return try {
            val result = client
                .post("$baseUrl/users/verify") {
                    headers {
                        append(keyName, apiKey)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(
                        """{
                        "uid": "$userId"
                        }""".trimMargin()
                    )
                }.await<ApiResponse>()
            return if (result.success == 1) {
                Resource.Success(Unit)
            } else {
                Resource.Failure(Exception(result.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }


    override suspend fun createQuestion(newQuestion: Question, token: String): Resource<Unit> {
        return try {
            val result = client.post("$baseUrl/questions/") {
                headers {
                    append(Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(newQuestion)
            }.await<ApiResponse>()

            if (result.success == 1) {
                Resource.Success(Unit)
            } else {
                Resource.Failure(Exception(result.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }


    override suspend fun getQuestionsByDepartment(
        department: String,
        token: String,
        page: Int
    ): List<Question> {

        return try {
            val result = client.get("$baseUrl/questions/department/${department}?page=${page}") {
                headers {
                    append(Authorization, "Bearer $token")
                }
            }.await<QuestionsResponse>()

            if (result.success == 1) {
                result.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }

    }

    override suspend fun getQuestionsByUser(
        userId: String,
        token: String,
        page: Int
    ): List<Question> {
        return try {
            val result = client.get("$baseUrl/questions/user/${userId}?page=${page}") {
                headers {
                    append(Authorization, "Bearer $token")
                }
            }.await<QuestionsResponse>()

            if (result.success == 1) {
                result.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getQuestionsByCourseName(
        department: String,
        token: String,
        page: Int,
        courseName: String,
        shift: String,
        exam: String
    ): List<Question> {
        return try {
            val result =
                client.get("$baseUrl/questions/course/${department}/${shift}/${exam}/${courseName}/1?page=${page}") {
                    headers {
                        append(Authorization, "Bearer $token")
                    }
                }.await<QuestionsResponse>()

            if (result.success == 1) {
                result.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun updateQuestion(
        id: String,
        isApproved: Int,
        token: String
    ) {
        try {
            val result = client.patch("$baseUrl/questions/") {
                headers {
                    append(Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(
                    """{
                        "questionId": "$id",
                        "isApproved": $isApproved
                    }""".trimMargin()
                )
            }.await<ApiResponse>()

            Log.d("Afridi ${count1++}", result.toString())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Afridi ${count++}", e.message!!)
        }
    }

    override suspend fun getQuestionCountByDepartment(department: String, token: String): Int {
        return try {
            val result =
                client.get("$baseUrl/questions/count/${department}/1") {
                    headers {
                        append(Authorization, "Bearer $token")
                    }
                }.await<CountResponse>()

            if (result.success == 1) {
                result.data
            } else {
                0
            }
        } catch (e:Exception) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun getQuestionCountByName(
        department: String,
        courseName: String,
        shift: String,
        exam: String,
        token: String
    ): Int {
        return try {
            val result =
                client.get("$baseUrl/questions/count/${department}/${shift}/${exam}/${courseName}/1") {
                    headers {
                        append(Authorization, "Bearer $token")
                    }
                }.await<CountResponse>()

            if (result.success == 1) {
                result.data
            } else {
                0
            }
        } catch (e:Exception) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun createUserInfo(
        userInfo: UserInfo
    ): String {
        return try{

            val result = client.post("$baseUrl/users/about/") {
                headers {
                    append(keyName, apiKey)
                }
                contentType(ContentType.Application.Json)
                setBody(userInfo)
            }.await<ApiResponse>()

            result.message

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Afridi", e.message!!)
            e.message!!
        }

    }

    override suspend fun getUserInfo(id: String): UserInfo? {
        return try{

            val result = client
                .get("$baseUrl/users/about/$id") {
                    headers {
                        append(keyName, apiKey)
                    }
                }
                .await<ApiUserInfo>()
            return if(result.success == 1) {
                result.data
            } else {
                Log.d("Afridi", result.success.toString())
                null
            }

        } catch (e: Exception) {
            Log.d("Afridi", e.message!!)
            e.printStackTrace()
            null
        }

    }

    override suspend fun updateUserInfo(
        id: String,
        department: String,
        about: String
    ): String {
        return try {
            val result = client.patch("$baseUrl/users/about") {
                headers {
                    append(keyName, apiKey)
                }
                contentType(ContentType.Application.Json)
                setBody(
                    """{
                        "id": "$id",
                        "department":"$department",
                        "about":"$about"
                    }""".trimMargin()
                )
            }.await<ApiResponse>()

            result.message

        } catch (e: Exception) {
            e.printStackTrace()
            e.message!!
        }
    }

    override suspend fun updateUserImage(id: String, image: String): String {
        return try {
            val result = client.patch("$baseUrl/users/about/image") {
                headers {
                    append(keyName, apiKey)
                }
                contentType(ContentType.Application.Json)
                setBody(UserInfo(id, "", "", image, 0, 0, 0))
            }.await<ApiResponse>()

            result.message

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Afridi", e.message!!)
            e.message!!
        }
    }


}

