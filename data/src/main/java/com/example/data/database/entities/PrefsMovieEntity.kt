package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "prefs_movie_table")
data class PrefsMovieEntity(
    @PrimaryKey val traktId: Int,
    val liked: Boolean = false,
    val watched: Boolean = false,
    val addedDateTime : Long, // using timestamp

    /**  1.1.3 added notes  */
    val notes : String = ""
)