package com.fourdevs.diuquestionbank.data

data class Comment(
    val commentId:String,
    val userId:String,
    val questionId:String,
    val comment:String,
    val date:String,
    val time:String,
    val replyTo:String?
)
