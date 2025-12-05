//package com.example.muvitracker.data.database.all_images_tmdb.database.entities
//
//import android.media.browse.MediaBrowser
//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class ConvertersUtilsImagesTmdb {
//
//    // MEDIA ITEM
//    @TypeConverter
//    fun fromMediaList(mediaList: List<MediaBrowser.MediaItem>): String {
//        return Gson().toJson(mediaList)
//    }
//
//    @TypeConverter
//    fun toMediaList(mediaString: String): List<MediaBrowser.MediaItem> {
//        val objectType = object : TypeToken<List<MediaBrowser.MediaItem>>() {}.type
//        return Gson().fromJson(mediaString, objectType)
//    }
//
//
//}