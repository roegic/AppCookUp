package com.example.mycookingapp.home_screen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mycookingapp.R
import com.example.mycookingapp.data.Recipe
import com.example.mycookingapp.data.RecipeViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

val USER_FAVOURITES_FIELD = "favouriteRecipes"

class RecipeInfo : AppCompatActivity() {
    private lateinit var mRecipeViewModel: RecipeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_info)
        var recipeLink = ""

        var recipeId = intent.getIntExtra("recipeId", -1)
        if (recipeId == -1) {
            Toast.makeText(
                this, "список рецептов пуст",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
            mRecipeViewModel.getRecipe(recipeId).observe(this, Observer { curRecipeList ->
                if (curRecipeList.isNotEmpty()) {
                    val curRecipe: Recipe = curRecipeList[0]
                    update_recipe_view(curRecipe)
                    recipeLink = curRecipe.recipe_link
                    onShareClicked(recipeLink)
                } else {
                    Toast.makeText(
                        this, "список рецептов пуст",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        onFavouritesClicked(recipeId)

    }

    fun update_recipe_view(recipe: Recipe) {

        val recippeCollapsingToolbar: CollapsingToolbarLayout =
            findViewById(R.id.recipe_collapsing_toolbar)
        recippeCollapsingToolbar.title = recipe.name

        val recipeCaloriesTV: TextView = findViewById(R.id.recipe_calories)
        recipeCaloriesTV.text = "калории: " + recipe.calories.toString() + " ккал"

        val recipeCoookingTimeTV: TextView = findViewById(R.id.recipe_cooking_time)
        recipeCoookingTimeTV.text = "время: " + recipe.time_estimated.toString() + " мин"

        val recipeIngredintsListTV: TextView = findViewById(R.id.recipe_ingredients_list)
        recipeIngredintsListTV.text = recipe.ingredients_list

        val recipeCookingStepsTV: TextView = findViewById(R.id.recipe_cooking_steps_content)
        recipeCookingStepsTV.text = recipe.cooking_steps

        val recipeDescriptionTV: TextView = findViewById(R.id.recipe_description_content)
        recipeDescriptionTV.text = recipe.description

        val recipeImage: ImageView = findViewById(R.id.recipe_img)
        val url = "https://" + recipe.image_link
        Glide.with(this).load(url).placeholder(R.drawable.ic_downloading_img).fitCenter().into(recipeImage)

    }

    fun onFavouritesClicked(recipeId: Int) {
        val btn = findViewById<FloatingActionButton>(R.id.favouriteBtn)
        btn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "выполните вход в аккаунт", Toast.LENGTH_SHORT).show()
            } else {
                val userId = currentUser.uid
                val database = FirebaseDatabase.getInstance().getReference("users")
                val favRef = database.child(userId).child(USER_FAVOURITES_FIELD)

                favRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val isInFavourites =
                                dataSnapshot.children.any { it.getValue(Int::class.java) == recipeId }
                            if (isInFavourites) {// рецепт уже  в избранном, удаление
                                dataSnapshot.children.forEach {
                                    if (it.getValue(Int::class.java) == recipeId) {
                                        it.ref.removeValue().addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "Рецепт удален из избранного",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }.addOnFailureListener { e ->
                                            Toast.makeText(
                                                applicationContext,
                                                "ошибка при удалении избранного: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            } else { // добавляем в избранное
                                val newFavouriteRef = favRef.push()
                                newFavouriteRef.setValue(recipeId).addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Рецепт добавлен в избранное",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        applicationContext,
                                        "Ошибка при добавлении в избранное: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            favRef.push().setValue(recipeId).addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Рецепт добавлен в избранное",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    applicationContext,
                                    "Ошибка при добавлении в избранное: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // ошибка
                        Toast.makeText(
                            applicationContext,
                            "ошибка: ${databaseError.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    fun onShareClicked(recipeLink: String) {
        val btn = findViewById<FloatingActionButton>(R.id.shareBtn)
        btn.setOnClickListener {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, recipeLink)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

    }
}