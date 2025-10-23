package com.example.muvitracker.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.TmdbApi
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.database.all_images_tmdb.TmdbAllImagesApi
import com.example.muvitracker.data.repositories.DetailMovieRepositoryImpl
import com.example.muvitracker.data.repositories.DetailShowRepositoryImpl
import com.example.muvitracker.data.repositories.EpisodeRepositoryImpl
import com.example.muvitracker.data.repositories.PrefsMovieRepositoryImpl
import com.example.muvitracker.data.repositories.PrefsShowRepositoryImpl
import com.example.muvitracker.data.repositories.SeasonRepositoryImpl
import com.example.muvitracker.domain.repo.DetailMovieRepository
import com.example.muvitracker.domain.repo.DetailShowRepository
import com.example.muvitracker.domain.repo.EpisodeRepository
import com.example.muvitracker.domain.repo.PrefsMovieRepository
import com.example.muvitracker.domain.repo.PrefsShowRepository
import com.example.muvitracker.domain.repo.SeasonRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaggerModules {

    // ok
    @Provides
    @Singleton
    fun providedDetailMovieRepo(impl: DetailMovieRepositoryImpl): DetailMovieRepository {
        return impl
    }

    // ok
    @Provides
    @Singleton
    fun providedDetailShowRepo(impl: DetailShowRepositoryImpl): DetailShowRepository {
        return impl
    }


    @Provides
    @Singleton
    fun providePrefsMovieRepo(impl: PrefsMovieRepositoryImpl): PrefsMovieRepository {
        return impl
    }


    @Provides
    @Singleton
    fun providePrefsShowRepo(impl: PrefsShowRepositoryImpl): PrefsShowRepository {
        return impl
    }


    @Provides
    @Singleton
    fun provideSeasonRepo(impl: SeasonRepositoryImpl): SeasonRepository {
        return impl
    }


    @Provides
    @Singleton
    fun provideEpisodeRepo(impl: EpisodeRepositoryImpl): EpisodeRepository {
        return impl
    }


    // DATABASING ---------------------------------------------------------------------------------
    @Provides
    @Singleton
    fun getMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            "muvi-tracker-db"
        )
            // calcella db e ne create uno nuovo - non crasha quando modifichi lo schema del DB
//            .fallbackToDestructiveMigration()
            // TODO: valutare se aggiungere
//            .addMigrations(MIGRATION_1_2) // add movie tmdb elements
//            .addMigrations(MIGRATION_2_3) // add show tmdb elements
//            .addMigrations(MIGRATION_3_4) // add season tmdb elements
            .build()
    }

    // shared preferences
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }


    // CHIAMATE INTERNET
    @Provides
    @Singleton
    fun provideTraktApi(): TraktApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("trakt-api-key", BuildConfig.TRAKT_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    }.build()
            ).build()
        return retrofit.create(TraktApi::class.java)
    }


    @Provides
    @Singleton
    fun provideTmdbApi(): TmdbApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TmdbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTmdbAllImagesApi(): TmdbAllImagesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TmdbAllImagesApi::class.java)
    }


}