package com.example.worker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_employee")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val department: String,
    val income: Int,
    val address: String
)
