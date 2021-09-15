package com.example.newsmvvm.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsmvvm.model.Article

@Dao
interface DaoArticle {

    @Query("select * from article")
    fun getAllBreackingNews() : LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aricle: Article): Long

    @Delete
    suspend fun deleteAricle(aricle: Article)

}