package com.example.newsmvvm.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmvvm.R
import com.example.newsmvvm.adapters.ArticleAdapter
import com.example.newsmvvm.base.BaseFragment
import com.example.newsmvvm.databinding.FragmentSearchNewsBinding
import com.example.newsmvvm.ui.MainActivity
import com.example.newsmvvm.ui.NewsViewModel
import com.example.newsmvvm.util.Constants
import com.example.newsmvvm.util.Resourse
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : BaseFragment<FragmentSearchNewsBinding>() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: ArticleAdapter
    override fun getBinding() = R.layout.fragment_search_news
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        searchNews()
        initAdapter()
        newsAdapter.setOnItemClickListner {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }
        viewModel.searchBreackingNews.observe(viewLifecycleOwner, Observer {
                response ->
            when(response){
                is Resourse.Success ->{
                    hideProgressBar()
                    response.data?.let {
                            newsResponse ->
                        newsAdapter.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.searchbreackingPage == totalPage
                        if(isLastPage){
                            binding.rvBreackingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resourse.Error ->{
                    hideProgressBar()
                    response.data?.let {
                            message->
                        Toast.makeText(activity, "Error${message}", Toast.LENGTH_SHORT).show()
                    }

                }
                is Resourse.Loading ->{
                    showProgressBar()
                }
            }
        })
    }
    var isLoading =false
    var isLastPage = false
    var isScroling = false

    val scrollListner = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScroling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisableItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            val visebleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoandinAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisableItemPosition + visebleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisableItemPosition >= 0
            val isTotalMoreThanVisable = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shoulPaginate = isNotLoandinAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisable
                    && isScroling
            if(shoulPaginate){
                viewModel.searchBreakingNews(binding.etSearch.text.toString())
                isScroling = false
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun initAdapter() {
        newsAdapter = ArticleAdapter()
        binding.rvBreackingNews.adapter = newsAdapter
        binding.rvBreackingNews.apply {
            addOnScrollListener(this@SearchNewsFragment.scrollListner)
        }
    }
    private fun searchNews() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchBreakingNews(editable.toString())
                        binding.etSearch.hideKeyboard()
                    }
                }
            }
        }
    }
}