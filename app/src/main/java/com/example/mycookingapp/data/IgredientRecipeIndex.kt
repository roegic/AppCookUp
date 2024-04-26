package com.example.mycookingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient_recipe_index_table")
data class IngedientRecipeIndex (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val recipe_id: Int,
    val ingredient_id: Int
)