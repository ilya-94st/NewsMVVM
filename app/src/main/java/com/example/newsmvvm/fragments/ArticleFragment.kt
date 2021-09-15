package com.example.newsmvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsmvvm.R
import com.example.newsmvvm.base.BaseFragment
import com.example.newsmvvm.databinding.FragmentArticleBinding
import com.example.newsmvvm.ui.MainActivity
import com.example.newsmvvm.ui.NewsViewModel

class ArticleFragment : BaseFragment<FragmentArticleBinding>() {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    override fun getBinding() = R.layout.fragment_article
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
       webView(article.url)
        binding.fab.setOnClickListener {
            viewModel.saveNews(article)
            snackBar("article saved successfully")
        }
        inVisibleMenu(R.id.botomNavigation)
    }
    fun webView(urlAricle: String) {
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(urlAricle)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        visibleMenu(R.id.botomNavigation)
    }
}