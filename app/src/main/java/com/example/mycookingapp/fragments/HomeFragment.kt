package com.example.mycookingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.data.RecipeViewModel
import com.example.mycookingapp.home_screen.FridgeIngredientResult
import com.example.mycookingapp.home_screen.FridgeIngredientsAdapter
import com.example.mycookingapp.home_screen.MealConfigAdapter
import com.example.mycookingapp.home_screen.data.FridgeProducts
import com.example.mycookingapp.home_screen.data.ProductCategory
import java.util.Locale


private var dataset = mutableListOf<ProductCategory>()
private var all_ingredients = mutableListOf<FridgeProducts>()

private lateinit var fridgeAdapter: FridgeIngredientsAdapter
lateinit var searchView: SearchView
lateinit var mRecipeViewModel: RecipeViewModel
lateinit var fridgeFoodRV: RecyclerView
lateinit var mealTypeRV: RecyclerView
lateinit var timeConfigRV: RecyclerView
lateinit var btn: Button
var rootView: View? = null
private var loadingText: TextView? = null

class HomeFragment : Fragment() {
    val mealTypesList = mutableListOf<String>(
        "завтрак", "обед", "ужин"
    )

    private var mealConfigAdapter = MealConfigAdapter(mealTypesList)
    val timeList = mutableListOf<String>(
        "< 15 мин", "< 30 мин", "< 45 мин", "< 1 час"
    )
    private var timeConfigAdapter = MealConfigAdapter(timeList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (rootView != null) {
            return rootView!!
        }

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        rootView = view

        loadingText = view.findViewById(R.id.loading_text)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        var curIngredient: FridgeProducts
        var categoriesIndexMap = mutableMapOf<String, Int>()
        var cur_indx = 0
        var category_idx = 0
        mRecipeViewModel.getIngredientsCategory()
        mRecipeViewModel.ingredientsCategory.observe(
            viewLifecycleOwner,
            Observer { ingredientsList ->
                if (dataset.size < 3 && ingredientsList.isNotEmpty()) {
                    for (ingredient in ingredientsList) {
                        if (ingredient.category == null) {
                            continue
                        }

                        curIngredient = FridgeProducts(ingredient.ingredient_name, false)
                        if (!categoriesIndexMap.containsKey(ingredient.category)) {
                            categoriesIndexMap[ingredient.category!!] = cur_indx
                            cur_indx++
                            dataset.add(
                                ProductCategory(
                                    ingredient.category!!,
                                    false,
                                    mutableListOf()
                                )
                            )
                        }
                        category_idx = categoriesIndexMap[ingredient.category]!!
                        dataset[category_idx].products.add(curIngredient)
                    }
                    fridgeAdapter.notifyDataSetChanged()
                    for (el in dataset) {
                        all_ingredients += el.products
                    }
                    loadingText?.visibility = View.GONE
                }
            })


        fridgeAdapter = FridgeIngredientsAdapter(dataset)
        fridgeFoodRV = view.findViewById(R.id.products_choose)

        fridgeFoodRV.layoutManager = LinearLayoutManager(context)
        fridgeFoodRV.adapter = fridgeAdapter

        mealTypeRV = view.findViewById(R.id.meal_type_rv)
        mealTypeRV.layoutManager = GridLayoutManager(context, 3)
        mealTypeRV.adapter = mealConfigAdapter

        timeConfigRV = view.findViewById(R.id.time_rv)
        timeConfigRV.layoutManager = GridLayoutManager(context, 4)
        timeConfigRV.adapter = timeConfigAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById<SearchView>(R.id.searchVieww)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var searchList =
                    mutableListOf<FridgeProducts>() // array of all suitable for query ingredients
                var categoryMain = mutableListOf<ProductCategory>()
                if (p0 != null && p0 != "") {
                    for (i in all_ingredients) {
                        if (i.name.lowercase(Locale.ROOT).contains(p0)) {
                            searchList.add(i)
                        }
                    }
                    if (searchList.isEmpty()) {
                        fridgeAdapter.onApplySearch(dataset)
                        Toast.makeText(context, "не найдено", Toast.LENGTH_SHORT).show()
                    } else {
                        categoryMain.add(
                            ProductCategory(
                                "Найденные ингредиенты:",
                                false,
                                searchList
                            )
                        )
                        fridgeAdapter.onApplySearch(categoryMain)
                    }
                } else {
                    fridgeAdapter.onApplySearch(dataset)
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            false
        }

        btn = view.findViewById<Button>(R.id.btn1)
        btn.setOnClickListener {

            val mt = mealConfigAdapter.makeSearchRequest()
            val time = timeConfigAdapter.makeSearchRequest()
            val ingr = fridgeAdapter.makeSearchRequest()
            val intent = Intent(activity, FridgeIngredientResult::class.java)

            intent.putExtra("reqIngredientsArray", ingr)
            intent.putExtra("mealType", mt)
            intent.putExtra("time", time)
            startActivity(intent)
        }
    }

}