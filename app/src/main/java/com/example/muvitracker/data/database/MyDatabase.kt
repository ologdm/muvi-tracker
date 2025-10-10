package com.example.muvitracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.dao.DetailMovieDao
import com.example.muvitracker.data.database.dao.DetailShowDao
import com.example.muvitracker.data.database.dao.EpisodeDao
import com.example.muvitracker.data.database.dao.PrefsMovieDao
import com.example.muvitracker.data.database.dao.PrefsShowDao
import com.example.muvitracker.data.database.dao.SeasonDao
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.DetailShowEntity
import com.example.muvitracker.data.database.entities.EpisodeEntity
import com.example.muvitracker.data.database.entities.PrefsMovieEntity
import com.example.muvitracker.data.database.entities.PrefsShowEntity
import com.example.muvitracker.data.database.entities.SeasonEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.dao.EpisodeImageDao
import com.example.muvitracker.data.database.all_images_tmdb.database.dao.MovieShowImageDao
import com.example.muvitracker.data.database.all_images_tmdb.database.dao.PersonImageDao
import com.example.muvitracker.data.database.all_images_tmdb.database.dao.SeasonImageDao
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.ConvertersUtilsTmdb
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.EpisodeImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.MovieShowImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.PersonImageEntity
import com.example.muvitracker.data.database.all_images_tmdb.database.entities.SeasonImageEntity

@Database(
    entities = [
        // trakt
        DetailMovieEntity::class,
        DetailShowEntity::class,
        PrefsMovieEntity::class,
        PrefsShowEntity::class,
        SeasonEntity::class,
        EpisodeEntity::class,
        // tmdb images
        MovieShowImageEntity::class,
        SeasonImageEntity::class,
        EpisodeImageEntity::class,
        PersonImageEntity::class,
        // tmdb new details
    ],
    version = 1
)
@TypeConverters(ConvertersUtils::class, ConvertersUtilsTmdb::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun detailMovieDao(): DetailMovieDao
    abstract fun detailShowDao(): DetailShowDao

    abstract fun prefsMovieDao(): PrefsMovieDao
    abstract fun prefsShowDao(): PrefsShowDao

    abstract fun seasonsDao(): SeasonDao
    abstract fun episodesDao(): EpisodeDao


    // tmdb
    abstract fun movieShowImageDao() : MovieShowImageDao
    abstract fun seasonImageDao() : SeasonImageDao
    abstract fun episodeImageDao() : EpisodeImageDao
    abstract fun personImageDao() : PersonImageDao
}
