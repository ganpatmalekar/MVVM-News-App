package com.swapnil.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swapnil.mvvmnewsapp.databinding.ItemArticleBinding
import com.swapnil.mvvmnewsapp.ui.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>()/*ListAdapter<Article, NewsAdapter.ArticleViewHolder>(differCallback)*/ {

    private lateinit var binding: ItemArticleBinding

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = /*getItem(position)*/differ.currentList[position]

        binding.apply {
            Glide.with(ivArticleImage).load(article.urlToImage).into(ivArticleImage)
            tvPublishedAt.text = article.publishedAt
            tvDescription.text = article.description
            tvTitle.text = article.title
            tvSource.text = article.source?.name

            root.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    // lambda function
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size

    /*companion object {
        val differCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }*/
}