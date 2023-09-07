package com.fourdevs.diuquestionbank.models

data class HelpRequest(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val subject: String,
    val message: String
)
