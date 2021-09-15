package com.example.newsmvvm.repository

import com.example.newsmvvm.api.RetrofitInstanse
import com.example.newsmvvm.db.ArticleDatabase
import com.example.newsmvvm.model.Article


class NewsRepository(
    val db:ArticleDatabase
) {
    suspend fun getBreackingNews(countryCode: String, numberPage: Int) =
        RetrofitInstanse.api?.getBreackingNews(countryCode, numberPage)

    suspend fun searchBreackingNews(searchQuery: String, numberPage: Int) =
        RetrofitInstanse.api?.getSearchNews(searchQuery, numberPage)

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getAllNews() = db.getArticleDao().getAllBreackingNews()

    suspend fun deleteNews(article: Article) = db.getArticleDao().deleteAricle(article)
}