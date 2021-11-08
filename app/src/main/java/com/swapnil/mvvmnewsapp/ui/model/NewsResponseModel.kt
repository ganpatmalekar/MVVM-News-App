package com.swapnil.mvvmnewsapp.ui.model

data class NewsResponseModel(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)