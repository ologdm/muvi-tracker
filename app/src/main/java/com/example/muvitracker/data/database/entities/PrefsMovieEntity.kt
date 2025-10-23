package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity(tableName = "prefs_movie_entities")
//data class PrefsMovieEntity(
//    @PrimaryKey val traktId: Int,
//    val liked: Boolean = false,
//    val watched: Boolean = false,
//    val addedDateTime : Long // using timestamp
//    // TODO add notes
//)

@Entity(tableName = "prefs_movie_table")
data class PrefsMovieEntity(
    @PrimaryKey val traktId: Int,
    val liked: Boolean = false,
    val watched: Boolean = false,
    val addedDateTime : Long, // using timestamp
    // TODO 1.1.3 add notes
//    val notes : String = ""
)