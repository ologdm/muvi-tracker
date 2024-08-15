package com.example.muvitracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.muvitracker.data.database.dao.BoxofficeDao
import com.example.muvitracker.data.database.dao.DetailDao
import com.example.muvitracker.data.database.dao.PrefsDao
import com.example.muvitracker.data.database.entities.BoxoMovieEntity
import com.example.muvitracker.data.database.entities.DetailMovieEntity
import com.example.muvitracker.data.database.entities.PrefsEntity

@Database(
    entities = [
        DetailMovieEntity::class,
        PrefsEntity::class,
        BoxoMovieEntity::class
    ],
    version = 1
)
@TypeConverters(ConvertersUtils::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun detailDao(): DetailDao
    abstract fun prefsDao(): PrefsDao
    abstract fun boxofficeDao(): BoxofficeDao

}
