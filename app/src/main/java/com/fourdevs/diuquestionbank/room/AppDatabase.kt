package com.fourdevs.diuquestionbank.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fourdevs.diuquestionbank.data.Question
import com.fourdevs.diuquestionbank.data.User
import com.fourdevs.diuquestionbank.room.dao.QuestionDao
import com.fourdevs.diuquestionbank.room.dao.UserDao
import com.fourdevs.diuquestionbank.utilities.Constants


@Database(
    entities = [User::class, Question::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, Constants.KEY_DB
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }
}