package com.example.mycookingapp.home_screen.data

data class ProductCategory(
    val name: String,
    var isSelected: Boolean,
    val products: MutableList<FridgeProducts>
)