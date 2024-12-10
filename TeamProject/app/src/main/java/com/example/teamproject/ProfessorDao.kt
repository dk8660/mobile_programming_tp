package com.example.teamproject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfessorDao {
    @Query("SELECT * FROM professor")
    suspend fun getAll(): Array<Professor>
    @Insert
    suspend fun insert(vararg professor: Professor)
    @Update
    suspend fun update(professor: Professor)
    @Delete
    suspend fun delete(professor: Professor)
}