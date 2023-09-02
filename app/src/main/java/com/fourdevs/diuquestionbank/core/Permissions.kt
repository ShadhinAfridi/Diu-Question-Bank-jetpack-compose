package com.fourdevs.diuquestionbank.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.fourdevs.diuquestionbank.utilities.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class Permissions @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sdkVersion = Build.VERSION.SDK_INT


    fun checkPermissions(): Boolean {
        return if(sdkVersion >= Build.VERSION_CODES.S_V2){
            true
        } else{
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun askPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            Constants.PERMISSION_REQUEST_CODE
        )
    }


    fun checkNotificationPermission() : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }



    fun askNotificationPermission(activity: Activity) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    Constants.NOTIFICATION_REQUEST_CODE
                )
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

}