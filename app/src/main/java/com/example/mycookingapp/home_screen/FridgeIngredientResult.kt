package com.example.mycookingapp.home_screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.data.RecipeViewModel
import com.example.mycookingapp.home_screen.data.Meal


class FridgeIngredientResult : AppCompatActivity() {

    var requestedIngredients = mutableListOf<Meal>()


    private lateinit var fridgeResultAdapter: fridgeResultAdapter
    lateinit var fridgeFoodRV: RecyclerView
    lateinit var checkboxIsExact: CheckBox
    lateinit var sortRecipeSpinner: Spinner

    private lateinit var mRecipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge_ingredient_result)

        var arrayFromIntent =
            intent.getStringArrayListExtra("reqIngredientsArray") as ArrayList<String>?
        var mealTypeFromIntent = intent.getStringExtra("mealType")
        var timeEstimateFromIntent = intent.getStringExtra("time")
        if (timeEstimateFromIntent == null) {
            timeEstimateFromIntent = ""
        }
        if (mealTypeFromIntent == null || mealTypeFromIntent == "") {
            mealTypeFromIntent = "-"
        }

        if (arrayFromIntent == null) {
            Toast.makeText(
                this, "список рецептов пуст",
                Toast.LENGTH_SHORT
            ).show()
        }

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        initializeSpinner(arrayFromIntent, timeEstimateFromIntent, mealTypeFromIntent)
        initializeAdapter()
        initializeCheckbox(arrayFromIntent, timeEstimateFromIntent, mealTypeFromIntent)

    }

    fun initializeAdapter() {
        fridgeFoodRV = findViewById(R.id.products_search_result)
        fridgeFoodRV.layoutManager = LinearLayoutManager(this)
        fridgeResultAdapter = fridgeResultAdapter(requestedIngredients)
        fridgeFoodRV.adapter = fridgeResultAdapter
        fridgeResultAdapter.onMealClick = { meal ->
            val intent = Intent(this@FridgeIngredientResult, RecipeInfo::class.java)
            intent.putExtra("recipeId", meal.id)
            startActivity(intent)
        }
    }

    fun initializeCheckbox(arrayFromIntent: ArrayList<String>?, time: String, mealType: String) {
        checkboxIsExact = findViewById(R.id.isExactCheckbox)

        checkboxIsExact.setOnCheckedChangeListener { buttonView, isChecked ->
            updateData(arrayFromIntent, time, mealType)
        }
    }

    fun initializeSpinner(arrayFromIntent: ArrayList<String>?, time: String, mealType: String) {
        sortRecipeSpinner = findViewById(R.id.sort_spinner)
        val sorting_options = arrayOf("нет", "название", "калории", "время")
        val sortingAdapter = ArrayAdapter(
            this@FridgeIngredientResult,
            android.R.layout.simple_spinner_dropdown_item,
            sorting_options
        )
        sortRecipeSpinner.adapter = sortingAdapter

        sortRecipeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val arrayFromIntent2 =
                    arrayFromIntent
                updateData(arrayFromIntent2, time, mealType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                defaultSearch(arrayFromIntent, isExact = false, 999999, "-")
            }
        }
    }

    fun updateData(arrayFromIntent: ArrayList<String>?, time: String, mealType: String) {
        val selectedOption = sortRecipeSpinner.selectedItem.toString()
        var realTime = 999999
        if (time == "") {
            realTime = 1440
        } else {
            realTime = time.toInt()
        }

        when (selectedOption) {
            "нет" -> defaultSearch(arrayFromIntent, checkboxIsExact.isChecked, realTime, mealType)
            "калории" -> sortByCalories(
                arrayFromIntent,
                checkboxIsExact.isChecked,
                realTime,
                mealType
            )

            "название" -> sortByName(arrayFromIntent, checkboxIsExact.isChecked, realTime, mealType)
            "время" -> sortByTime(arrayFromIntent, checkboxIsExact.isChecked, realTime, mealType)
        }
    }

    fun defaultSearch(
        arrayFromIntent: ArrayList<String>?,
        isExact: Boolean,
        realTime: Int,
        mealType: String
    ) {
        if (arrayFromIntent == null) {
            Toast.makeText(this@FridgeIngredientResult, "произошла ошибка", Toast.LENGTH_LONG)
                .show()
            return
        }
        mRecipeViewModel.searchByIngredients(arrayFromIntent, isExact, realTime, mealType)
        mRecipeViewModel.recipesResultSearch.observe(
            this@FridgeIngredientResult,
            Observer { recipesList ->
                if (recipesList.isNotEmpty()) {
                    val req = mutableListOf<Meal>(recipesList[0])
                    fridgeResultAdapter.changeData(recipesList)
                } else {
                    Toast.makeText(
                        this, "список рецептов пуст",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun sortByCalories(
        arrayFromIntent: ArrayList<String>?,
        isExact: Boolean,
        realTime: Int,
        mealType: String
    ) {
        if (arrayFromIntent == null) {
            Toast.makeText(this@FridgeIngredientResult, "произошла ошибка", Toast.LENGTH_LONG)
                .show()
            return
        }
        mRecipeViewModel.sortByCalories(arrayFromIntent, isExact, realTime, mealType)
        mRecipeViewModel.recipesSortedByCalories.observe(
            this@FridgeIngredientResult,
            Observer { recipesList ->
                if (recipesList.isNotEmpty()) {
                    fridgeResultAdapter.changeData(recipesList)
                } else {
                    Toast.makeText(
                        this, "список рецептов пуст",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun sortByName(
        arrayFromIntent: ArrayList<String>?,
        isExact: Boolean,
        realTime: Int,
        mealType: String
    ) {
        if (arrayFromIntent == null) {
            Toast.makeText(this@FridgeIngredientResult, "произошла ошибка", Toast.LENGTH_LONG)
                .show()
            return

        }
        mRecipeViewModel.sortByName(arrayFromIntent, isExact, realTime, mealType)
        mRecipeViewModel.recipesSortedByName.observe(
            this@FridgeIngredientResult,
            Observer { recipesList ->
                if (recipesList.isNotEmpty()) {
                    fridgeResultAdapter.changeData(recipesList)

                } else {
                    Toast.makeText(
                        this, "список рецептов пуст",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun sortByTime(
        arrayFromIntent: ArrayList<String>?,
        isExact: Boolean,
        realTime: Int,
        mealType: String
    ) {
        if (arrayFromIntent == null) {
            Toast.makeText(this@FridgeIngredientResult, "произошла ошибка", Toast.LENGTH_LONG)
                .show()
            return
        }
        mRecipeViewModel.sortByTime(arrayFromIntent, isExact, realTime, mealType)
        mRecipeViewModel.recipesSortedByTime.observe(
            this@FridgeIngredientResult,
            Observer { recipesList ->
                if (recipesList.isNotEmpty()) {
                    fridgeResultAdapter.changeData(recipesList)
                } else {
                    Toast.makeText(
                        this, "список рецептов пуст",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}

