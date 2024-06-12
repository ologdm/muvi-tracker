package com.example.muvitracker.data

import com.example.muvitracker.data.detail.DetailRepository
import com.example.muvitracker.data.movies.MoviesRepository
import com.example.muvitracker.data.prefs.PrefsRepository
import com.example.muvitracker.data.search.SearchRepository
import com.example.muvitracker.domain.repo.DetailRepo
import com.example.muvitracker.domain.repo.MoviesRepo
import com.example.muvitracker.domain.repo.PrefsRepo
import com.example.muvitracker.domain.repo.SearchRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideMoviesRepo(impl: MoviesRepository): MoviesRepo {
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

}