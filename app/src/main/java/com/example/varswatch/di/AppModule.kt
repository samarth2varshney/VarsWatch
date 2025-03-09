package com.example.varswatch.di

import android.content.Context
import android.content.SharedPreferences
import com.example.varswatch.data.remote.YoutubeApi
import com.example.varswatch.data.repository.YoutubeRepositoryImpl
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.util.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideYoutubeData(): YoutubeApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideYoutubeRepository(api: YoutubeApi,firestoreInstance: FirebaseFirestore,sharedPrefManager: SharedPrefManager): YoutubeRepository{
        return YoutubeRepositoryImpl(api,firestoreInstance,sharedPrefManager)
    }

    @Provides
    @Singleton
    fun provideSharedPrefManager(sharedPreferences: SharedPreferences): SharedPrefManager {
        return SharedPrefManager(sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

}