package com.example.mycookingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes_table")
data class Recipe(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val description: String,
    val time_estimated: Int,
    val ingredients_list: String,
    val ingredients_cnt: Int,
    val calories: Int,
    val cooking_steps: String,
    val image_link: String,
    val recipe_link: String,
    var category: String
)