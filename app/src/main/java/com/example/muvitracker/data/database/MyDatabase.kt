package com.example.muvitracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.dao.MovieDao
import com.example.muvitracker.data.database.dao.ShowDao
import com.example.muvitracker.data.database.dao.EpisodeDao
import com.example.muvitracker.data.database.dao.PrefsMovieDao
import com.example.muvitracker.data.database.dao.PrefsShowDao
import com.example.muvitracker.data.database.dao.SeasonDao
import com.example.muvitracker.data.database.entities.MovieEntity
import com.example.muvitracker.data.database.entities.ShowEntity
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.SeasonEntity

@Database(
    entities = [
        MovieEntity::class,
        ShowEntity::class,
        PrefsMovieEntity::class,
        PrefsShowEntity::class,
        SeasonEntity::class,
        EpisodeEntity::class,
        //
        /** tmdb images - unused */
//        MovieShowImageEntity::class,
//        SeasonImageEntity::class,
//        EpisodeImageEntity::class,
//        PersonImageEntity::class,
    ],
    version = 1
)
@TypeConverters(
    ConvertersUtils::class,
//    ConvertersUtilsImagesTmdb::class  /* unused */
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun showDao(): ShowDao

    abstract fun prefsMovieDao(): PrefsMovieDao
    abstract fun prefsShowDao(): PrefsShowDao

    abstract fun seasonsDao(): SeasonDao
    abstract fun episodesDao(): EpisodeDao


    /** tmdb all_images - unused */
//    abstract fun movieShowImageDao(): MovieShowImageDao
//    abstract fun seasonImageDao(): SeasonImageDao
//    abstract fun episodeImageDao(): EpisodeImageDao
//    abstract fun personImageDao(): PersonImageDao

}
