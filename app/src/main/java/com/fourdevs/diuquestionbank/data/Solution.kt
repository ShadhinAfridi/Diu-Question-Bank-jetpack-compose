package com.fourdevs.diuquestionbank.data

data class Solution(
    val solutionId: String,
    val questionId: String,
    val uploaderId: String,
    val date: String,
    val isApproved: Boolean,
    val link: String,
    val downloadCount: Int,
    val viewCount: Int,
    val commentCount: Int
)
