package com.swapnil.mvvmnewsapp.repository

import com.swapnil.mvvmnewsapp.db.ArticleDao
import com.swapnil.mvvmnewsapp.network.NewsApiService
import com.swapnil.mvvmnewsapp.ui.model.Article
import javax.inject.Inject

class NewsRepository
@Inject constructor(private val articleDao: ArticleDao, private val apiService: NewsApiService) {

    suspend fun getBreakingNews(
        countryCode: String, pageNumber: Int
    ) = apiService.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(
        searchQuery: String, pageNumber: Int
    ) = apiService.searchNews(searchQuery, pageNumber)

    suspend fun insert(article: Article) = articleDao.insert(article)

    fun getSavedArticles() = articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)

}