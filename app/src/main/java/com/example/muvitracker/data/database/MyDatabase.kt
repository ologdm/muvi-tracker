package com.example.muvitracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.entities.DetailEntityR
import com.example.muvitracker.data.database.entities.PopularMovieEntity
import com.example.muvitracker.data.database.entities.PrefsEntityR

@Database(
    entities = [
        DetailEntityR::class,
        PrefsEntityR::class,
        PopularMovieEntity::class
    ],
    version = 1
)
@TypeConverters(ConvertersUtils::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun detailDao(): DetailDao
    abstract fun prefsDao(): PrefsDao

    abstract fun popularDao(): PopularDao
    abstract fun boxofficeDao(): BoxofficeDao


}
