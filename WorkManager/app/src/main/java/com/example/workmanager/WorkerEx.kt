package com.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import java.lang.Thread.sleep

class WorkerEx(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "#doWork")

        (0..100 step 10).forEach {
            Log.d(TAG, "#doWork it = $it")
            setProgressAsync(workDataOf(PROGRESS to it))
            sleep(1000)
        }

        return Result.success(workDataOf(KEY_COMPLETED to true))
    }

    companion object {
        private const val TAG = "WorkerEx"
    }
}