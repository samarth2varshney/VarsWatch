package com.example.varswatch.di

import com.example.varswatch.data.repository.YoutubeRepositoryImpl
import com.example.varswatch.domain.repository.YoutubeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindYoutubeRepository(youtubeRepositoryImpl: YoutubeRepositoryImpl): YoutubeRepository

}