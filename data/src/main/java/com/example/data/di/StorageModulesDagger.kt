package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.core.BuildConfig
import com.example.data.api.OmdbApi
import com.example.data.api.TmdbApi
import com.example.data.api.TraktApi
import com.example.data.database.MyDatabase
import com.example.data.repositories.DetailMovieRepositoryImpl
import com.example.data.repositories.DetailShowRepositoryImpl
import com.example.data.repositories.EpisodeRepositoryImpl
import com.example.data.repositories.ExploreRepositoryImpl
import com.example.data.repositories.PersonRepositoryImpl
import com.example.data.repositories.PrefsMovieRepositoryImpl
import com.example.data.repositories.PrefsShowRepositoryImpl
import com.example.data.repositories.SearchRepositoryImpl
import com.example.data.repositories.SeasonRepositoryImpl
import com.example.domain.repo.DetailMovieRepository
import com.example.domain.repo.DetailShowRepository
import com.example.domain.repo.EpisodeRepository
import com.example.domain.repo.ExploreRepository
import com.example.domain.repo.PersonRepository
import com.example.domain.repo.PrefsMovieRepository
import com.example.domain.repo.PrefsShowRepository
import com.example.domain.repo.SearchRepository
import com.example.domain.repo.SeasonRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Qualifier
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