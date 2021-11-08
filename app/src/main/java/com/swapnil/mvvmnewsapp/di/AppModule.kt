package com.swapnil.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.swapnil.mvvmnewsapp.db.ArticleDatabase
import com.swapnil.mvvmnewsapp.network.NewsApiService
import com.swapnil.mvvmnewsapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun providesNewsApiService(baseUrl: String): NewsApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApiService::class.java)

    @Provides
    @Singleton
    fun providesArticleDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesArticleDao(db: ArticleDatabase) = db.getArticleDao()
}