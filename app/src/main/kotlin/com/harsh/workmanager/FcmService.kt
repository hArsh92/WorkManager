package com.harsh.workmanager

import android.util.Log
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.keys.size > 0) {
            val workRequest = NotificationWorker.createWorkRequest(message.data)
            WorkManager.getInstance(this).enqueue(workRequest)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(FcmService::class.simpleName, "FCM Token --> $token")
    }
}
