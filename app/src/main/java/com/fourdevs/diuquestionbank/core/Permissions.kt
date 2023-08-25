package com.fourdevs.diuquestionbank.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

}