package com.fourdevs.diuquestionbank.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val displayName: String,
    val disabled: Boolean,
    val metadata: Metadata,
    val tokensValidAfterTime: String,
    val providerData: List<ProviderData>
)
@Serializable
data class Metadata(
    val lastSignInTime: String,
    val creationTime: String,
    val lastRefreshTime: String
)
@Serializable
data class ProviderData(
    val uid: String,
    val displayName: String,
    val email: String,
    val providerId: String
)


@Serializable
data class ApiUserResponse(
    val success: Int,
    val message: User
)

@Serializable
data class ApiResponse(
    val success: Int,
    val message: String
)