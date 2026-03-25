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
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Qualifier
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
            // NOTE: calcella db e ne create uno nuovo - non crasha quando modifichi lo schema del DB
//            .fallbackToDestructiveMigration()
            // valutare se aggiungere
//            .addMigrations(MIGRATION_1_2) // add movie tmdb elements
//            .addMigrations(MIGRATION_2_3) // add show tmdb elements
//            .addMigrations(MIGRATION_3_4) // add season tmdb elements
            .build()
    }

    // ---------------------------------------------------------------------------

    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

    // API SERVICES -----------------------------------------------------------------------------------

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TraktRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TmdbRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OmdbRetrofit


    @Provides
    @Singleton
    @TraktRetrofit
    fun provideTraktRetrofit(
        converterFactory: Converter.Factory,
        client: OkHttpClient
    ): Retrofit {
        val traktRetrofit = Retrofit.Builder()
            .baseUrl("https://api.trakt.tv/")
            .addConverterFactory(converterFactory)
            .client( // al posto di callFactory()
//                OkHttpClient.Builder()
                client.newBuilder() // eredita impostazione da altri builder - CHECK SERVRE
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("trakt-api-key", BuildConfig.TRAKT_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    }.build()
            )
            .build()
        return traktRetrofit
    }


    @Provides
    @Singleton
    @TmdbRetrofit
    fun provideTmdbRetrofit(
        converterFactory: Converter.Factory,
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(converterFactory)
            .build()
    }


    @Provides
    @Singleton
    @OmdbRetrofit
    fun provideOmdbRetrofit(
        converterFactory: Converter.Factory,
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/")
            .addConverterFactory(converterFactory)
            .build()
    }


    // API SERVICES -----------------------------------------------------------------------------------

    @Provides
    @Singleton
    fun provideTraktApi(@TraktRetrofit retrofit: Retrofit): TraktApi {
        return retrofit.create(TraktApi::class.java)
    }


    @Provides
    @Singleton
    fun provideTmdbApi(
        @TmdbRetrofit retrofit: Retrofit
    ): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }


    @Provides
    @Singleton
    fun provideOmdbApi(
        @OmdbRetrofit retrofit: Retrofit
    ): OmdbApi {
        return retrofit.create(OmdbApi::class.java)
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


    // TODO: NEW FEATURES
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