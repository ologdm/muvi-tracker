package com.example.data.di

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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModulesDagger {

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

    // new - gradle modules release
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

}