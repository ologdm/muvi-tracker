package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// 00
@Entity(tableName = "prefs_show_entities")
data class PrefsShowEntity(
    @PrimaryKey val traktId: Int,
    val liked: Boolean = false,
    val watchedAll: Boolean = false, // solo
    val watchedCount: Int = 0, // default 0,
    val addedDateTime: Long // using timestamp
)




// logica:
// watchedCount=0 default | observe on episodes
// watchedAll=true  if(watchedCount=max episodes)