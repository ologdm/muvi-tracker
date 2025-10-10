package com.example.muvitracker.data.database.all_images_tmdb.database.entities

import androidx.room.TypeConverter
import com.example.muvitracker.data.dto.tmdb.MediaItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConvertersUtilsTmdb {

    // MEDIA ITEM
    @TypeConverter
    fun fromMediaList(mediaList: List<MediaItem>): String {
        return Gson().toJson(mediaList)
    }

    @TypeConverter
    fun toMediaList(mediaString: String): List<MediaItem> {
        val objectType = object : TypeToken<List<MediaItem>>() {}.type
        return Gson().fromJson(mediaString, objectType)
    }


}