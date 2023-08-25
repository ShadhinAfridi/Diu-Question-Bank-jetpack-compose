package com.fourdevs.diuquestionbank.data

import androidx.compose.runtime.saveable.listSaver
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

val QuestionSaver = listSaver<Question, Any>(
    save = {
        listOf(
            it.questionId,
            it.code,
            it.courseName,
            it.lt,
            it.departmentName,
            it.shift,
            it.exam,
            it.semester,
            it.year,
            it.uploaderId,
            it.date,
            it.isApproved,
            it.link
        )
    },
    restore = {
        Question(
            it[0] as String,
            it[1] as String,
            it[2] as String,
            it[3] as String,
            it[4] as String,
            it[5] as String,
            it[6] as String,
            it[7] as String,
            it[8] as String,
            it[9] as String,
            it[10] as String,
            it[11] as Int,
            it[12] as String
        )
    }
)

@Serializable
data class QuestionsResponse(
    val success: Int,
    val data: List<Question>
)