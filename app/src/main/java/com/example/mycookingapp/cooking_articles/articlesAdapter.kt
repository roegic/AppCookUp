package com.example.mycookingapp.cooking_articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.databinding.ArticlePreviewBinding

class articlesAdapter(private val articleData: List<Article>): RecyclerView.Adapter<articlesAdapter.ArticlesHolder>() {

    lateinit var onArticleClick: ((Article) -> Unit)

    class ArticlesHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ArticlePreviewBinding.bind(item)

        fun bind(article: Article) {
            binding.articlePreviewTitle.text = article.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.article_preview, parent, false) // TODO
        return articlesAdapter.ArticlesHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return articleData.size
    }

    override fun onBindViewHolder(holder: ArticlesHolder, position: Int) {
        val item = articleData[position]
        holder.bind(item)
        holder.binding.articlePreviewTitle.setOnClickListener{
            onArticleClick.invoke(articleData[position])
            true
        }
    }


}