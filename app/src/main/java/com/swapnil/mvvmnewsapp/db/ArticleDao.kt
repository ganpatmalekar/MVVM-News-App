package com.swapnil.mvvmnewsapp.db

import androidx.room.*
import com.swapnil.mvvmnewsapp.ui.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles() : Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}