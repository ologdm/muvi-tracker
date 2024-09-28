package com.example.muvitracker.data.database

import androidx.room.TypeConverter
import com.example.muvitracker.data.dto.base.Ids
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ConvertersUtils {

    // IDS
    @TypeConverter
    fun fromIds(ids: Ids): String {
        return Gson().toJson(ids)
    }


    @TypeConverter
    fun toIds(idsString: String): Ids {
        val objectType = object : TypeToken<Ids>() {}.type
        return Gson().fromJson(idsString, objectType)
    }


    // GENRES LIST
    @TypeConverter
    fun fromGenreslist(list: List<String>): String {
        return Gson().toJson(list)
    }


    @TypeConverter
    fun toGenreslist(genresString: String): List<String> {
        val objectType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(genresString, objectType)
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