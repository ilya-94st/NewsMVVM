package com.example.newsmvvm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvm.NewsApplication
import com.example.newsmvvm.model.Article
import com.example.newsmvvm.model.NewsResponse
import com.example.newsmvvm.repository.NewsRepository
import com.example.newsmvvm.util.Resourse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(private var newsRepository: NewsRepository, var app: Application): AndroidViewModel(app) {
    val breackingNews: MutableLiveData<Resourse<NewsResponse>> = MutableLiveData()
    var breackinNewsResponse: NewsResponse? = null
    var breackingPage = 1

    val searchBreackingNews: MutableLiveData<Resourse<NewsResponse>> = MutableLiveData()
    var serchBreackingNewsResponse: NewsResponse? = null
    var searchbreackingPage = 1

    init {
        getBreackingNews("ru")
    }
    fun getBreackingNews(countryCode: String) = viewModelScope.launch {
       safeBreacingNews(countryCode)
    }
    fun searchBreakingNews(searchNews: String) = viewModelScope.launch {
        safeSearchNews(searchNews)
    }
    private fun handleBreackingNews(response: Response<NewsResponse>) : Resourse<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                resultResponse->
                breackingPage++
                if(breackinNewsResponse==null){
                    breackinNewsResponse = resultResponse
                }else {
                    val oldArticle = breackinNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resourse.Success(breackinNewsResponse?: resultResponse)
            }
        }
        return  Resourse.Error(response.message())
    }
    private fun handleSearchNews(response: Response<NewsResponse>) : Resourse<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchbreackingPage++
                if (serchBreackingNewsResponse == null) {
                    serchBreackingNewsResponse = resultResponse
                } else {
                    val oldArticle = serchBreackingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resourse.Success(serchBreackingNewsResponse ?: resultResponse)
            }
        }
        return Resourse.Error(response.message())
    }

    private suspend fun safeSearchNews(searchNews: String){
        searchBreackingNews.postValue(Resourse.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.searchBreackingNews(searchNews, searchbreackingPage)
                searchBreackingNews.postValue(response?.let { handleSearchNews(it) })
            } else {
                searchBreackingNews.postValue(Resourse.Error("No internet conection"))
            }
        }catch (t: Throwable){
            when(t){
           is IOException -> searchBreackingNews.postValue(Resourse.Error("Network Failure"))
                else -> searchBreackingNews.postValue(Resourse.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreacingNews(countryCode: String){
        breackingNews.postValue(Resourse.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.getBreackingNews(countryCode, breackingPage)
                breackingNews.postValue(response?.let { handleBreackingNews(it) })
            } else {
                breackingNews.postValue(Resourse.Error("No internet conection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> breackingNews.postValue(Resourse.Error("Network Failure"))
                else -> breackingNews.postValue(Resourse.Error("Conversion Error"))
            }
        }
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
    val activityNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
    return when{
        capabilities.hasTransport(TRANSPORT_WIFI) -> true
        capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
        else -> false
    }
} else {
    connectivityManager.activeNetworkInfo?.run {
        return when(type){
            TYPE_WIFI -> true
            TYPE_MOBILE -> true
            TYPE_ETHERNET -> true
            else -> false
        }
    }
}
        return false
    }

    fun saveNews(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }
    fun getSaveNews() = newsRepository.getAllNews()
    fun deleteNews(article: Article) = viewModelScope.launch {
        newsRepository.deleteNews(article)
    }
}