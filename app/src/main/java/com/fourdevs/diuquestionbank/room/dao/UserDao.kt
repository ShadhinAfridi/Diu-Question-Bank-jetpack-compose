package com.fourdevs.diuquestionbank.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fourdevs.diuquestionbank.data.User
import com.fourdevs.diuquestionbank.utilities.Constants

@Dao
interface UserDao {

    @Query("SELECT * FROM "+ Constants.KEY_COLLECTION_USERS+" where user_id = :userId")
    fun loadAllById(userId: String): LiveData<User>

    @Insert()
    fun insertAll(users: User)

    @Delete
    fun delete(user: User)

}