package com.example.newsmvvm.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsmvvm.repository.NewsRepository


@Suppress("UNCHECKED_CAST")
class NewsProviderFactory(val newsRepository: NewsRepository, val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return NewsViewModel(newsRepository, app) as T
    }
}