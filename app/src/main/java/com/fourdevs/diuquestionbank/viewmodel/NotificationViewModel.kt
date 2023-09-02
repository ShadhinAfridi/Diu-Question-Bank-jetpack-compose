package com.fourdevs.diuquestionbank.viewmodel

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.fourdevs.diuquestionbank.core.Permissions
import com.fourdevs.diuquestionbank.models.Notification
import com.fourdevs.diuquestionbank.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val permissions: Permissions
)  : ViewModel() {

    private val notificationList = mutableStateListOf<Notification>()
    fun addNotification(notification: Notification) {
        notificationList.add(notification)
    }


    fun checkNotificationPermission() = permissions.checkNotificationPermission()

    fun askNotificationPermission(activity: Activity) {
        permissions.askNotificationPermission(activity)
    }


    fun showNotification(notification: Notification) {
        notificationRepository.buildNotification(notification)
    }
}