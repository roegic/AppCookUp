package com.example.mycookingapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mycookingapp.home_screen.data.Meal

object Constants {
    const val MAX_INGREDIENTS_DIFF = 3
    const val MAX_TIME = 9999999
    const val MAX_SEARCH_RESULT = 5
}

@Dao
interface RecipeDao {
    @Query("SELECT id, name, description FROM recipes_table WHERE id IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name IN (:ingr)) GROUP BY recipe_id HAVING COUNT(DISTINCT ingredient_id) <= :inr_size) AND ingredients_cnt - :inr_size <= :maxIngredientsDiff AND time_estimated <= :time AND category in (:categoryList) ORDER BY ingredients_cnt - :inr_size")
    fun searchByIngredientsNotExact(
        ingr: List<String>,
        inr_size: Int,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf(
            "-",
            "завтрак",
            "обед",
            "ужин"
        ),
        maxIngredientsDiff: Int = Constants.MAX_INGREDIENTS_DIFF
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id NOT IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name NOT IN (:ingr)) GROUP BY recipe_id) AND time_estimated <= :time AND category in (:categoryList)")
    fun searchByIngredients(
        ingr: List<String>,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин")
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name IN (:ingr)) GROUP BY recipe_id HAVING COUNT(DISTINCT ingredient_id) <= :inr_size) AND ingredients_cnt - :inr_size <= 100  ORDER BY ingredients_cnt - :inr_size ASC")
    fun notExactSearch(ingr: List<String>, inr_size: Int): LiveData<List<Meal>>

    @Query("SELECT * FROM ingredients_table")
    fun getIngredientsCategory(): LiveData<List<Ingredient>>

    @Query("SELECT * FROM recipes_table WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): LiveData<List<Recipe>>

    @Query("SELECT id, name, description FROM recipes_table")
    fun getAll(): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id NOT IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name NOT IN (:ingr)) GROUP BY recipe_id) AND time_estimated <= :time AND category in (:categoryList) ORDER BY calories ASC")
    fun sortByCalories(
        ingr: List<String>,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин")
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name IN (:ingr)) GROUP BY recipe_id HAVING COUNT(DISTINCT ingredient_id) <= :inr_size) AND ingredients_cnt - :inr_size <= :maxIngredientsDiff AND time_estimated <= :time AND category in (:categoryList) ORDER BY calories ASC, ingredients_cnt - :inr_size ASC ")
    fun sortByCaloriesNotExact(
        ingr: List<String>,
        inr_size: Int,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин"),
        maxIngredientsDiff: Int = Constants.MAX_INGREDIENTS_DIFF
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id NOT IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name NOT IN (:ingr)) GROUP BY recipe_id) AND time_estimated <= :time AND category in (:categoryList) ORDER BY name ASC")
    fun sortByName(
        ingr: List<String>,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин")
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name IN (:ingr)) GROUP BY recipe_id HAVING COUNT(DISTINCT ingredient_id) <= :inr_size) AND ingredients_cnt - :inr_size <= :maxIngredientsDiff AND time_estimated <= :time AND category in (:categoryList) ORDER BY  name ASC, ingredients_cnt - :inr_size ASC")
    fun sortByNameNotExact(
        ingr: List<String>,
        inr_size: Int,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин"),
        maxIngredientsDiff: Int = Constants.MAX_INGREDIENTS_DIFF
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name IN (:ingr)) GROUP BY recipe_id HAVING COUNT(DISTINCT ingredient_id) <= :inr_size) AND ingredients_cnt - :inr_size <= :maxIngredientsDiff AND time_estimated <= :time AND category in (:categoryList) ORDER BY  time_estimated ASC, ingredients_cnt - :inr_size ASC")
    fun sortByTimeNotExact(
        ingr: List<String>,
        inr_size: Int,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин"),
        maxIngredientsDiff: Int = Constants.MAX_INGREDIENTS_DIFF
    ): LiveData<List<Meal>>

    @Query("SELECT id, name, description FROM recipes_table WHERE id NOT IN (SELECT recipe_id FROM ingredient_recipe_index_table WHERE ingredient_id IN (SELECT id FROM ingredients_table WHERE ingredient_name NOT IN (:ingr)) GROUP BY recipe_id) AND time_estimated <= :time AND category in (:categoryList) ORDER BY time_estimated ASC")
    fun sortByTime(
        ingr: List<String>,
        time: Int = Constants.MAX_TIME,
        categoryList: List<String> = listOf("-", "завтрак", "обед", "ужин")
    ): LiveData<List<Meal>>
}