package com.fourdevs.diuquestionbank.core

import android.util.Log
import com.fourdevs.diuquestionbank.data.ApiResponse
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.QuestionsResponse
import com.fourdevs.diuquestionbank.data.Resource
import com.fourdevs.diuquestionbank.data.UserResponse
import com.fourdevs.diuquestionbank.utilities.await
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import javax.inject.Inject


class ApiClientImpl @Inject constructor(
    private val client: HttpClient
) : ApiClient {
    private val apiKey = "123456789"
    private val keyName = "x-api-key"
    private val baseUrl = "https://app.techerax.com"


    override suspend fun getUserById(id: String): UserResponse {
        return client
            .get("$baseUrl/users/${id}") {
                headers {
                    append(keyName, apiKey)
                }
            }
            .body()
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
            if(page>1) {
                delay(3000L)
            }

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

    override suspend fun updateQuestion(
        id: String,
        code: String,
        name: String,
        lt: String,
        isApproved: Int,
        token: String
    ) {
        try{
            val result = client.patch("$baseUrl/questions/") {
                headers {
                    append(Authorization, "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(
                    """{
                        "questionId": "$id",
                        "code": "$code",
                        "courseName": "$name",
                        "lt": "$lt",
                        "isApproved": $isApproved
                    }""".trimMargin()
                )
            }.await<ApiResponse>()

            Log.d("Afridi", result.toString())

        }catch (e: Exception) {
            e.printStackTrace()
            Log.d("Afridi", e.message!!)
        }
    }




}

