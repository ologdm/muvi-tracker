package com.example.muvitracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// 00
@Entity(tableName = "prefs_show_entities")
data class PrefsShowEntity(
    @PrimaryKey val traktId: Int,
    val liked: Boolean = false,
    val addedDateTime: Long // using timestamp
)




    // watched non servono
//    val watchedAll: Boolean = false,
//    val watchedCount: Int = 0,
//)

// logica:
// watchedCount=0 default | observe on episodes
// watchedAll=true  if(watchedCount=max episodes)