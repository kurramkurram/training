package com.example.worker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Employee::class], version = 1, exportSchema = false)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        private const val dbName = "Employee.db"

        fun getDatabases(context: Context): EmployeeDatabase {
            val tmpInstance = INSTANCE

            if (tmpInstance != null) return tmpInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeDatabase::class.java,
                    dbName
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}