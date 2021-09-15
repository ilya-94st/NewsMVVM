package com.example.newsmvvm.api

import com.example.newsmvvm.model.NewsResponse
import com.example.newsmvvm.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreackingNews(
        @Query("country")
        countryCode: String = "ru",
        @Query("page")
        newsPage: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q")
        everyNewsCode: String,
        @Query("page")
        newsPage: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}