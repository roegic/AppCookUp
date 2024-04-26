package com.example.mycookingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.cooking_articles.Article
import com.example.mycookingapp.cooking_articles.ArticlesInfo
import com.example.mycookingapp.cooking_articles.articlesAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private val articlesList = mutableListOf<Article>()

class ArticlesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_articles, container, false)

        val database = FirebaseDatabase.getInstance().getReference("articles")

        val articlesAdapter = articlesAdapter(articlesList)
        val articleRV: RecyclerView = view.findViewById(R.id.articlesRV)
        articleRV.layoutManager = LinearLayoutManager(context)
        articleRV.adapter = articlesAdapter
        articlesAdapter.onArticleClick = { article ->
            val intent = Intent(activity, ArticlesInfo::class.java)
            intent.putExtra("articleId", article.id)
            intent.putExtra("articleName", article.name)
            startActivity(intent)
        }

        database.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (articlesList.size > 0) { // not to fetch data again
                    return
                }
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.getValue(Article::class.java)
                    value?.let { articlesList.add(it) }
                }
                articlesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context, "ошибка при получении данных: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return view
    }

}