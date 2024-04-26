package com.example.mycookingapp.data

import androidx.lifecycle.LiveData
import com.example.mycookingapp.home_screen.data.Meal

class RecipeRepository(private val recipeDao: RecipeDao) {
    fun getRecipeById(recipeId: Int): LiveData<List<Recipe>> {
        return recipeDao.getRecipeById(recipeId)
    }

    fun searchByIngredients(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.searchByIngredients(ingr, time, mealList)
    }

    fun searchByIngredientsNotExact(
        ingr: List<String>,
        time: Int,
        mealType: String
    ): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.searchByIngredientsNotExact(ingr, ingr.size, time, mealList)
    }

    fun getIngredientsCategory(): LiveData<List<Ingredient>> {
        return recipeDao.getIngredientsCategory()
    }

    fun getAll(): LiveData<List<Meal>> {
        return recipeDao.getAll()
    }

    fun sortByCalories(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }

        return recipeDao.sortByCalories(ingr, time, mealList)
    }

    fun sortByCaloriesNotExact(
        ingr: List<String>,
        time: Int,
        mealType: String
    ): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.sortByCaloriesNotExact(ingr, ingr.size, time, mealList)
    }

    fun sortByName(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.sortByName(ingr, time, mealList)
    }

    fun sortByNameNotExact(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.sortByNameNotExact(ingr, ingr.size, time, mealList)
    }

    fun sortByTime(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.sortByTime(ingr, time, mealList)
    }

    fun sortByTimeNotExact(ingr: List<String>, time: Int, mealType: String): LiveData<List<Meal>> {
        var mealList: List<String>
        if (mealType == "-") {
            mealList = listOf<String>("-", "завтрак", "обед", "ужин")
        } else {
            mealList = listOf<String>("-", mealType)
        }
        return recipeDao.sortByTimeNotExact(ingr, ingr.size, time, mealList)
    }
}