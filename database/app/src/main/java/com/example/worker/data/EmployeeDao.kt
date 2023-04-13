package com.example.worker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {

    @Insert
    fun insert(employee: Employee)

    @Query("SELECT * FROM t_employee")
    fun selectAll(): List<Employee>
}