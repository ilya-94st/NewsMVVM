package com.example.newsmvvm.model

import com.example.newsmvvm.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)