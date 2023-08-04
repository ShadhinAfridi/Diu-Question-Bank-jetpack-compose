package com.fourdevs.diuquestionbank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "questions")
@Serializable
data class Question(
    @PrimaryKey val questionId: String,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "courseName") val courseName: String,
    @ColumnInfo(name = "lt") val lt: String,
    @ColumnInfo(name = "departmentName") val departmentName: String,
    @ColumnInfo(name = "shift") val shift: String,
    @ColumnInfo(name = "exam") val exam: String,
    @ColumnInfo(name = "semester") val semester: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "uploaderId") val uploaderId: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isApproved") val isApproved: Int,
    @ColumnInfo(name = "link") val link: String
)

@Serializable
data class QuestionsResponse(
    val success : Int,
    val data : List<Question>
)