package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.work.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progress = findViewById<ProgressBar>(R.id.progress)

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                this.isEnabled = false

                val workManager = WorkManager.getInstance(applicationContext)
                val request = OneTimeWorkRequestBuilder<WorkerEx>().addTag(TAG_PROGRESS).build()
                workManager.getWorkInfosByTagLiveData(TAG_PROGRESS).observe(this@MainActivity) {
                    it.forEach { info ->
                        Log.d(TAG, "#onCreate info = $info")

                        when (info.state) {
                            WorkInfo.State.RUNNING -> {
                                val status = info.progress.getInt(PROGRESS, -1)
                                progress.progress = status
                                Log.d(TAG, "#onCreate progress = $status")
                            }

                            WorkInfo.State.SUCCEEDED -> {
                                val isComplete = info.outputData.getBoolean(KEY_COMPLETED, false)
                                if (isComplete) {
                                    this.isEnabled = true
                                }
                            }

                            else -> {}
                        }
                    }
                }
                workManager.enqueue(request)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}