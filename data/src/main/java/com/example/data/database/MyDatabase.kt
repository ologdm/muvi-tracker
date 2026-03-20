package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.database.dao.EpisodeDao
import com.example.data.database.dao.MovieDao
import com.example.data.database.dao.PrefsMovieDao
import com.example.data.database.dao.PrefsShowDao
import com.example.data.database.dao.SeasonDao
import com.example.data.database.dao.ShowDao
import com.example.data.database.entities.EpisodeEntity
import com.example.data.database.entities.MovieEntity
import com.example.data.database.entities.PrefsMovieEntity
import com.example.data.database.entities.PrefsShowEntity
import com.example.data.database.entities.SeasonEntity
import com.example.data.database.entities.ShowEntity
import com.example.domain.model.Movie

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
    version = 1,
    exportSchema = false
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
