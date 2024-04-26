package com.example.mycookingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients_table")
data class Ingredient (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val ingredient_name: String,
    var category: String?
)