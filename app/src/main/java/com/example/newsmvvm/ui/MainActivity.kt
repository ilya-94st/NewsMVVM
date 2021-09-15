package com.example.newsmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsmvvm.R
import com.example.newsmvvm.databinding.ActivityMainBinding
import com.example.newsmvvm.db.ArticleDatabase
import com.example.newsmvvm.model.Article
import com.example.newsmvvm.repository.NewsRepository

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel


   private lateinit var binding: ActivityMainBinding
   private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =supportFragmentManager.findFragmentById((R.id.fragmentContainerView)) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.botomNavigation.setupWithNavController(navController)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val newsProviderFactory = NewsProviderFactory(newsRepository, application)
        viewModel = ViewModelProvider(this, newsProviderFactory).get(NewsViewModel::class.java)
    }
}