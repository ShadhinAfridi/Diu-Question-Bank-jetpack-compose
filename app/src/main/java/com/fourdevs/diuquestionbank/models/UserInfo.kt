package com.fourdevs.diuquestionbank.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val department: String?,
    val about: String?,
    val image: String?,
    val pendingCount: Int,
    val approvedCount: Int,
    val rejectedCount: Int,
)

@Serializable
data class ApiUserInfo(
    val success: Int,
    val data: UserInfo
)