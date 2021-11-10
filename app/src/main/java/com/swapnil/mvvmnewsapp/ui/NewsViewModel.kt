package com.swapnil.mvvmnewsapp.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.swapnil.mvvmnewsapp.R
import com.swapnil.mvvmnewsapp.repository.NewsRepository
import com.swapnil.mvvmnewsapp.ui.model.Article
import com.swapnil.mvvmnewsapp.ui.model.NewsResponseModel
import com.swapnil.mvvmnewsapp.utils.NetUtils
import com.swapnil.mvvmnewsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponseModel>>()
    val breakingNews: LiveData<Resource<NewsResponseModel>> = _breakingNews
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponseModel? = null

    private val _searchNews = MutableLiveData<Resource<NewsResponseModel>>()
    val searchNews: LiveData<Resource<NewsResponseModel>> = _searchNews
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponseModel? = null

    init {
        getBreakingNews()
    }

    fun getBreakingNews() = viewModelScope.launch {
        safeBreakingNewsCall()
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getSavedArticles() = newsRepository.getSavedArticles().asLiveData()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getNews(): LiveData<List<Article>> {
        try {
            if (NetUtils.hasInternetConnection(context)) {
                viewModelScope.launch {
                    newsRepository.fetchNews("us")
                }
            } else {
                Toast.makeText(
                    context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT
                ).show()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(
                    context, context.getString(R.string.network_failure), Toast.LENGTH_SHORT
                ).show()
                else -> Toast.makeText(
                    context, context.getString(R.string.conversion_error), Toast.LENGTH_SHORT
                ).show()
            }
        }
        return newsRepository.getSavedArticles().asLiveData()
    }

    private suspend fun safeBreakingNewsCall() {
        _breakingNews.postValue(Resource.Loading())
        try {
            if (NetUtils.hasInternetConnection(context)) {
                val response = newsRepository.getBreakingNews("in", breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                _breakingNews.postValue(Resource.Error("No internet connection."))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNews.postValue(Resource.Error("Network Failure"))
                else -> _breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        _breakingNews.postValue(Resource.Loading())
        try {
            if (NetUtils.hasInternetConnection(context)) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                _searchNews.postValue(Resource.Error("No internet connection."))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNews.postValue(Resource.Error("Network Failure"))
                else -> _searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}