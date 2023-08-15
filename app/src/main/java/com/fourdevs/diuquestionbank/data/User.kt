package com.fourdevs.diuquestionbank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fourdevs.diuquestionbank.utilities.Constants
import kotlinx.serialization.Serializable

@Entity(tableName = Constants.KEY_COLLECTION_USERS)
@Serializable
data class User(
    @ColumnInfo(name = "serial") val serial:Int,
    @PrimaryKey val user_id: String,
    @ColumnInfo(name = "user_name") val user_name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "is_verified") val is_verified: Int,
    @ColumnInfo(name = "profile_picture") val profile_picture: String?,
    @ColumnInfo(name = "ip_address") val ip_address: String,
    @ColumnInfo(name = "last_login") val last_login: String,
    @ColumnInfo(name = "sign_up_date") val sign_up_date: String?,
    @ColumnInfo(name = "is_admin") val is_admin: Int
)






