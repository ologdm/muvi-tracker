package com.example.muvitracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.dao.BoxofficeDao
import com.example.muvitracker.data.database.dao.DetailMovieDao
import com.example.muvitracker.data.database.dao.DetailShowDao
import com.example.muvitracker.data.database.dao.EpisodeDao
import com.example.muvitracker.data.database.dao.PrefsMovieDao
import com.example.muvitracker.data.database.dao.PrefsShowDao
import com.example.muvitracker.data.database.dao.SeasonDao
import com.example.muvitracker.data.database.entities.BoxoMovieEntity
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.SeasonEntity

@Database(
    entities = [
        BoxoMovieEntity::class,
        DetailMovieEntity::class,
        PrefsMovieEntity::class,
        DetailShowEntity::class,
        PrefsShowEntity::class,
        SeasonEntity::class,
        EpisodeEntity::class,
    // TODO tmdb
    ],
    version = 1
)
@TypeConverters(ConvertersUtils::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun boxofficeDao(): BoxofficeDao // only list with caching

    abstract fun detailDao(): DetailMovieDao
    abstract fun prefsDao(): PrefsMovieDao

    abstract fun detailShowDao(): DetailShowDao
    abstract fun prefsShowDao(): PrefsShowDao

    abstract fun seasonsDao(): SeasonDao
    abstract fun episodesDao(): EpisodeDao




}
