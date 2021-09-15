package com.example.newsmvvm.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmvvm.R
import com.example.newsmvvm.adapters.ArticleAdapter
import com.example.newsmvvm.base.BaseFragment
import com.example.newsmvvm.databinding.FragmentSavedNewsBinding
import com.example.newsmvvm.tools.SnackBar
import com.example.newsmvvm.ui.MainActivity
import com.example.newsmvvm.ui.NewsViewModel
import com.example.newsmvvm.util.Resourse
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : BaseFragment<FragmentSavedNewsBinding>() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: ArticleAdapter
    override fun getBinding() = R.layout.fragment_saved_news
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initAdapter()
        newsAdapter.setOnItemClickListner {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breackingNewsFragment_to_articleFragment,
                bundle
            )
        }
        viewModel.getSaveNews().observe(viewLifecycleOwner, Observer {
            article->
            newsAdapter.submitList(article)
        })
        val itemTouchHelperCallBack = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
        , ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            @SuppressLint("ShowToast")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.deleteNews(article)
                Snackbar.make(view, "aricle successfull delete", Snackbar.LENGTH_LONG).apply {
                      setAction("Undo"){
                          viewModel.saveNews(article)
                      }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvBreackingNews)
        }
    }
    private fun initAdapter() {
        newsAdapter = ArticleAdapter()
        binding.rvBreackingNews.adapter = newsAdapter
    }
}