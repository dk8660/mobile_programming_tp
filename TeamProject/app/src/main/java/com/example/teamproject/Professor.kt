package com.example.teamproject

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "professor")
data class Professor (
    @PrimaryKey(autoGenerate=true) val id:Int=0,
    var name:String,
    var degree:String,
    var university:String,
    var field:String,
    var email:String,
    var lab:String
) : Parcelable