package com.swapnil.mvvmnewsapp.network

import com.swapnil.mvvmnewsapp.ui.model.NewsResponseModel
import com.swapnil.mvvmnewsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ) : Response<NewsResponseModel>

    @GET("v2/top-everything")
    suspend fun searchNews(
        @Query("country")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ) : Response<NewsResponseModel>
}