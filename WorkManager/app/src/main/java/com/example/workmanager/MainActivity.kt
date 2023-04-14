package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.work.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workManager = WorkManager.getInstance(applicationContext)

        val request = OneTimeWorkRequestBuilder<WorkerEx>().addTag(TAG_PROGRESS).build()

        workManager.getWorkInfosByTagLiveData(TAG_PROGRESS).observe(this) {
            it.forEach { info ->
                if (info.state == WorkInfo.State.RUNNING) {
                    val status = info.progress.getInt(PROGRESS, -1)
                    Log.d("MainActivity", "#onCreate progress = $status")
                }
            }
        }

        workManager.enqueue(request)
    }
}