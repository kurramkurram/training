package com.example.worker.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.worker.KEY_ADDRESS
import com.example.worker.KEY_DEPARTMENT
import com.example.worker.KEY_INCOME
import com.example.worker.KEY_NAME
import com.example.worker.data.Employee
import com.example.worker.data.EmployeeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoroutineWorkerEx(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val name = inputData.getString(KEY_NAME) ?: return@withContext Result.failure()
        val department = inputData.getString(KEY_DEPARTMENT) ?: return@withContext Result.failure()
        val income = inputData.getInt(KEY_INCOME, 0)
        val address = inputData.getString(KEY_ADDRESS) ?: return@withContext Result.failure()

        val employee = Employee(
            name = name,
            department = department,
            income = income,
            address = address
        )

        val database = EmployeeDatabase.getDatabases(applicationContext)
        val dao = database.employeeDao()
        dao.insert(employee)

        return@withContext Result.success()
    }

    companion object {
        private const val TAG = "CoroutineWorkerEx"
    }
}