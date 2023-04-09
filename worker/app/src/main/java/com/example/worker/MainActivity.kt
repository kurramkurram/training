package com.example.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.worker.workmanager.CoroutineWorkerEx

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameEditText = findViewById<TextView>(R.id.name_edit)
        val departmentEditText = findViewById<TextView>(R.id.department_edit)
        val incomeEditText = findViewById<TextView>(R.id.income_edit)
        val addressEditText = findViewById<TextView>(R.id.address_edit)

        findViewById<Button>(R.id.save_button).setOnClickListener {
            val name = nameEditText.text?.toString() ?: ""
            val department = departmentEditText.text?.toString() ?: ""
            val income = incomeEditText.text?.toString()?.toInt() ?: 0
            val address = addressEditText.text?.toString() ?: ""

            val data = Data.Builder()
                .putString(KEY_NAME, name)
                .putString(KEY_DEPARTMENT, department)
                .putInt(KEY_INCOME, income)
                .putString(KEY_ADDRESS, address)
                .build()

            val request =
                OneTimeWorkRequest.Builder(CoroutineWorkerEx::class.java).setInputData(data).build()

            WorkManager.getInstance(applicationContext).enqueue(request)
        }
    }
}