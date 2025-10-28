package com.example.muvitracker.data.database

import androidx.room.TypeConverter
import com.example.muvitracker.data.dto._support.Ids
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ConvertersUtils {

    private val gson = Gson()

    // IDS
    @TypeConverter
    fun fromIds(ids: Ids): String {
        return gson.toJson(ids)
    }

    @TypeConverter
    fun toIds(idsString: String): Ids {
        val objectType = object : TypeToken<Ids>() {}.type
        return gson.fromJson(idsString, objectType)
    }


    // LISTE STRINGHE
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(stringList: String?): List<String> {
        val objectType = object : TypeToken<List<String>>() {}.type

        return if (stringList.isNullOrEmpty())
            emptyList()
        else
            gson.fromJson(stringList, objectType)
    }


}


// ##############################################################################################
/*
// JSON
{
    "trakt": 12345,
    "slug": "example-slug",
    "tvdb": 67890,
    "imdb": "tt1234567",
    "tmdb": 112233
}

// CLASS OBJECT
val ids = Ids(
    trakt = 12345,
    slug = "example-slug",
    tvdb = 67890,
    imdb = "tt1234567",
    tmdb = 112233
)
 */