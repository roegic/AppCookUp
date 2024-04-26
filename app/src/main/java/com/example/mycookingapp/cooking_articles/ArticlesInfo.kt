package com.example.mycookingapp.cooking_articles

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mycookingapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticlesInfo : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var containerLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles_info)
        containerLayout = findViewById(R.id.containerLayout)
        readData()
    }

    fun readData() {

        var articleId = intent.getIntExtra("articleId", -1)
        var articleName = intent.getStringExtra("articleName") as String
        database = FirebaseDatabase.getInstance().getReference("articles")

        drawArticleName(articleName)

        database.child(articleId.toString()).child("content")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var contentList = ArrayList<String>()

                    for (snapshot in dataSnapshot.children) {
                        val contentSource = snapshot.getValue(String::class.java)
                        contentSource?.let { contentList.add(it) }
                    }

                    for (content in contentList) {
                        drawArticleView(content)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@ArticlesInfo, "ошибка при чтении данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun drawArticleName(name: String) {
        val textView = TextView(this@ArticlesInfo)
        textView.text = name
        textView.setTextColor(Color.BLACK)
        textView.textSize = 24F
        textView.setTypeface(null, Typeface.BOLD)
        val padding = 20
        textView.setPadding(padding, padding, padding, padding)
        containerLayout.addView(textView)
    }

    fun drawArticleView(content: String) {
        if (content.startsWith("http")) { // image link
            val imageView = ImageView(this@ArticlesInfo)
            Glide.with(this@ArticlesInfo).load(content).fitCenter().into(imageView)
            containerLayout.addView(imageView)
        } else {
            val textView = TextView(this@ArticlesInfo)
            textView.text = "\n" + content
            textView.setTextColor(Color.BLACK)
            val padding = 10
            textView.textSize = 19F
            textView.setPadding(padding, padding, padding, padding)
            containerLayout.addView(textView)
        }
    }
}