package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModulesDagger {


    @Provides
    @Singleton
    fun getMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            "muvi-tracker-db"
        )
//            .fallbackToDestructiveMigration() // NOTE: calcella db e ne create uno nuovo - non crasha quando modifichi lo schema del DB
            // TEST migration - doesn't need
//            .addMigrations(MIGRATION_1_2) // add movie tmdb elements
//            .addMigrations(MIGRATION_2_3) // add show tmdb elements
//            .addMigrations(MIGRATION_3_4) // add season tmdb elements
            .build()
    }


}