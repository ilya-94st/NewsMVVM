package com.example.newsmvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmvvm.databinding.ArticleItemsRecyclerBinding
import com.example.newsmvvm.model.Article


class ArticleAdapter: ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {
    inner class ArticleViewHolder(private val binding: ArticleItemsRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article){
binding.article = article
}
    }
    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                oldItem.url == newItem.url
            override fun areContentsTheSame(
                oldItem: Article,
                newItem: Article
            ) = oldItem.url == newItem.url

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
       val binding = ArticleItemsRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem!=null) {
            holder.bind(currentItem)
        }
        holder.itemView.setOnClickListener {
            onItemClickListner?.let {
                it(currentItem)
            }
        }
    }
    private var onItemClickListner: ((Article) ->Unit)? = null

    fun setOnItemClickListner(listner: (Article) ->Unit) {
        onItemClickListner = listner
    }
}