package com.example.muvitracker.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.muvitracker.BuildConfig
import com.example.muvitracker.data.TraktApi
import com.example.muvitracker.data.database.MyDatabase
import com.example.muvitracker.data.DetailRepository
import com.example.muvitracker.data.PrefsRepository
import com.example.muvitracker.data.SearchRepository
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.domain.repo.SearchRepo
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


    @Provides
    @Singleton
    fun providedMoviesRepo(impl: MoviesRepository): MoviesRepo {
        return impl
    }


    @Provides
    @Singleton
    fun providedDetailRepo(impl: DetailRepository): DetailRepo {
        return impl
    }


    @Provides
    @Singleton
    fun providePrefsRepo(impl: PrefsRepository): PrefsRepo {
        return impl
    }

    @Provides
    @Singleton
    fun provideSearchRepo(impl: SearchRepository): SearchRepo {
        return impl
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
        return context.getSharedPreferences("all_app_cache", Context.MODE_PRIVATE)
    }


    // retrofit
    @Provides
    @Singleton
    fun getApi(): TraktApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader(
                                "trakt-api-key",
                                BuildConfig.TRAKT_API_KEY
                            )
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            )
            .build()

        return retrofit.create(TraktApi::class.java)
    }


    @Provides
    @Singleton
    fun getMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            "muvi-tracker-db"
        ).build()
    }


}