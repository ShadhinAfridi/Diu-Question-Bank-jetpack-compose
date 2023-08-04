package com.fourdevs.diuquestionbank.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fourdevs.diuquestionbank.data.Question

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Question>)

    @Update
    suspend fun updateQuestion(question: Question)

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("SELECT * FROM questions WHERE questionId = :questionId")
    fun getQuestionById(questionId: String): LiveData<Question?>

    @Query("SELECT * FROM questions WHERE departmentName = :departmentName")
    fun getQuestionsByDepartment(departmentName: String): List<Question>

    @Query("SELECT * FROM questions WHERE departmentName = :departmentName AND shift = :shift AND exam = :exam AND courseName = :courseName")
    fun getQuestionsByCourse(
        departmentName: String,
        shift: String,
        exam: String,
        courseName: String
    ): LiveData<List<Question>>

    @Query("UPDATE questions SET courseName = :courseName WHERE questionId = :questionId")
    fun updateQuestionCourseName(courseName: String, questionId: Int)

}