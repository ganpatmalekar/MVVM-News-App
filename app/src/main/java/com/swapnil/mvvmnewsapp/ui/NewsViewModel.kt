package com.swapnil.mvvmnewsapp.ui

import androidx.lifecycle.*
import com.swapnil.mvvmnewsapp.repository.NewsRepository
import com.swapnil.mvvmnewsapp.ui.model.NewsResponseModel
import com.swapnil.mvvmnewsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponseModel>>()
    val breakingNews: LiveData<Resource<NewsResponseModel>> = _breakingNews
    private val breakingNewsPage = 1

    private val _searchNews = MutableLiveData<Resource<NewsResponseModel>>()
    val searchNews: LiveData<Resource<NewsResponseModel>> = _searchNews
    private val searchNewsPage = 1

    init {
        getBreakingNews()
    }

    private fun getBreakingNews() = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews("us", breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResp ->
                return Resource.Success(resultResp)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews("android", searchNewsPage)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResp ->
                return Resource.Success(resultResp)
            }
        }
        return Resource.Error(response.message())
    }
}