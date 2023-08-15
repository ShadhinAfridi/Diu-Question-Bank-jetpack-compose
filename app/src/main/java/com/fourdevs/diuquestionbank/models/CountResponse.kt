package com.fourdevs.diuquestionbank.models

import kotlinx.serialization.Serializable

@Serializable
data class CountResponse(
    val success: Int,
    val data: Int
)

