package com.harsh.workmanager

import android.content.Context
import android.os.Build
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.Duration

private const val NOTIFICATION_KEY_BODY = "body"
private const val DEFAULT_NOTIFICATION_BODY = "Notification triggered from Work Manager"

internal class NotificationWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val pusher = NotificationPusher(context)

    override fun doWork(): Result {
        val body = workerParams.inputData.getString(NOTIFICATION_KEY_BODY)
            .orEmpty().ifBlank { DEFAULT_NOTIFICATION_BODY }
        pusher.push(context.getString(R.string.app_name), body)
        return Result.success()
    }

    companion object {
        fun createWorkRequest(data: Map<String, String>): WorkRequest {
            val requestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestBuilder.setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(10L))
            }
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            requestBuilder.setConstraints(constraints)
            requestBuilder.setInputData(Data.Builder().putAll(data).build())
            return requestBuilder.build()
        }
    }
}
