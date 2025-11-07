package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// 00
//@Entity(tableName = "prefs_show_entities")
//data class PrefsShowEntity(
//    @PrimaryKey val traktId: Int,
//    val liked: Boolean = false,
//    val addedDateTime: Long // using timestamp
//    // TODO add notes
//)

@Entity(tableName = "prefs_show_table")
data class PrefsShowEntity(
    @PrimaryKey val traktId: Int,
    val liked: Boolean = false,
    val addedDateTime: Long, // using timestamp

    // TODO 1.1.3 add notes
    val notes : String = ""

    // !!note: watchedAll calcolato volta per volta a ui in base agli episodi
)
