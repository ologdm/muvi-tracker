package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.core.BuildConfig
import com.example.data.database.MyDatabase
import com.example.data.repositories.DetailMovieRepositoryImpl
import com.example.data.repositories.DetailShowRepositoryImpl
import com.example.data.repositories.EpisodeRepositoryImpl
import com.example.data.repositories.PersonRepositoryImpl
import com.example.data.repositories.PrefsMovieRepositoryImpl
import com.example.data.repositories.PrefsShowRepositoryImpl
import com.example.data.repositories.SearchRepositoryImpl
import com.example.data.repositories.SeasonRepositoryImpl
import com.example.data.repositories.ExploreRepositoryImpl
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DaggerModules {

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

    // new
    @Provides
    @Singleton
    fun provideExploreRepo(impl: ExploreRepositoryImpl): ExploreRepository {
        return impl
    }

    @Provides
    @Singleton
    fun providePersonRepo(impl: PersonRepositoryImpl): PersonRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideSearchRepo(impl: SearchRepositoryImpl): SearchRepository {
        return impl
    }


    // DATABASING --------------------------------------------------------------------------------- data OK
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





    @OptIn(ExperimentalSerializationApi::class) // opt-in corretto per asConverterFactory
    @Provides
    @Singleton
    fun provideTraktApi(): TraktApi {
        val json = Json {
            ignoreUnknownKeys = true // ignora campi non presenti nel tuo data class
            isLenient = true // rende il parser più permissivo in lettura di JSON non perfetto
            encodeDefaults =
                true // (per POST, invio dati) include sempre i valori di default nella serializzazione in JSON
            coerceInputValues =
                true // GET - se trova un null in un campo non nullable, usa il valore di default invece di crashare.
        }

        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client( // al posto di callFactory()
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("trakt-api-key", BuildConfig.TRAKT_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    }.build()
            )
            .build()
        return retrofit.create(TraktApi::class.java)
    }


    @Provides
    @Singleton
    fun provideTmdbApi(): TmdbApi {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        return retrofit.create(TmdbApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOmdbApi(): OmdbApi {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        return retrofit.create(OmdbApi::class.java)
    }


    /**  ALL IMAGES FROM TMDB - attualmente non usato
     * -> data.unused.tmdb_all_images
     */
//    @Provides
//    @Singleton
//    fun provideTmdbAllImagesApi(): TmdbAllImagesApi {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        return retrofit.create(TmdbAllImagesApi::class.java)
//    }


}