package com.example.teamproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Professor::class], version = 1)
abstract class ProfessorDatabase: RoomDatabase() {
    abstract fun professorDao():ProfessorDao
    companion object{
        @Volatile
        private var INSTANCE: ProfessorDatabase? = null
        fun getInstance(context:Context):ProfessorDatabase{
            return INSTANCE?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    ProfessorDatabase::class.java,
                    "student_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE=it }
            }
        }
    }
}