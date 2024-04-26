package com.example.mycookingapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycookingapp.home_screen.data.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application): AndroidViewModel(application){
    val repository: RecipeRepository
    var recipesResultSearch: LiveData<List<Meal>> = MutableLiveData<List<Meal>>()
    var recipesSortedByCalories: LiveData<List<Meal>> = MutableLiveData<List<Meal>>()
    var recipesSortedByName: LiveData<List<Meal>> = MutableLiveData<List<Meal>>()
    var recipesSortedByTime: LiveData<List<Meal>> = MutableLiveData<List<Meal>>()
    var ingredientsCategory: LiveData<List<Ingredient>> = MutableLiveData<List<Ingredient>>()
    var allData: LiveData<List<Meal>> = MutableLiveData<List<Meal>>()

    fun getAll() {
        allData = repository.getAll()
    }

    fun getRecipe(recipeId: Int): LiveData<List<Recipe>> {
        return repository.getRecipeById(recipeId)
    }

    fun getIngredientsCategory() {
        ingredientsCategory = repository.getIngredientsCategory()
    }
    fun searchByIngredients(ingr: List<String>, isExact: Boolean, time: Int, mealType: String) {
        if (isExact) {
            recipesResultSearch = repository.searchByIngredients(ingr, time, mealType)
        } else {
            recipesResultSearch = repository.searchByIngredientsNotExact(ingr, time, mealType)
        }
    }


    fun sortByCalories(ingr: List<String>, isExact: Boolean, time: Int, mealType: String){
        if (isExact) {
            recipesSortedByCalories = repository.sortByCalories(ingr, time, mealType)
        } else {
            recipesSortedByCalories = repository.sortByCaloriesNotExact(ingr, time, mealType)
        }

    }

    fun sortByName(ingr: List<String>, isExact: Boolean, time: Int, mealType: String){
        if (isExact){
            recipesSortedByName = repository.sortByName(ingr, time, mealType)
        } else {
            recipesSortedByName = repository.sortByNameNotExact(ingr, time, mealType)
        }

    }
    fun sortByTime(ingr: List<String>, isExact: Boolean, time: Int, mealType: String){
        if (isExact) {
            recipesSortedByTime = repository.sortByTime(ingr, time, mealType)
        } else {
            recipesSortedByTime = repository.sortByTimeNotExact(ingr, time, mealType)
        }
    }

    init {
        val recipeDao = RecipesDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
    }
}