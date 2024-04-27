package com.example.mycookingapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.data.Recipe
import com.example.mycookingapp.data.RecipeViewModel
import com.example.mycookingapp.favourites_screen.AuthenticationActivity
import com.example.mycookingapp.favourites_screen.LogOutActivity
import com.example.mycookingapp.home_screen.FridgeIngredientResult
import com.example.mycookingapp.home_screen.RecipeInfo
import com.example.mycookingapp.home_screen.data.Meal
import com.example.mycookingapp.home_screen.fridgeResultAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FavouritesFragment : Fragment() {
    private lateinit var mRecipeViewModel: RecipeViewModel
    lateinit var fridgeResultAdapter: fridgeResultAdapter
    private lateinit var authenticationLauncher: ActivityResultLauncher<Intent>
    private lateinit var fridgeFoodRV: RecyclerView
    var dataset = mutableListOf<Meal>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var auth = FirebaseAuth.getInstance()
        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        val view: View = inflater.inflate(R.layout.fragment_favourites, container, false)


        authenticationLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        initializeRv(currentUser, view)
                    }
                }
            }


        var currentUser = auth.currentUser
        if (currentUser == null) {
            view.findViewById<TextView>(R.id.emptyListText).text = "требуется авторизация"
            view.findViewById<TextView>(R.id.emptyListText).visibility = View.VISIBLE
        } else {
            initializeRv(currentUser, view)
        }

        val btn = view.findViewById<Button>(R.id.accBtn)


        btn.setOnClickListener {
            var curUser = FirebaseAuth.getInstance().currentUser
            if (curUser == null) {
                val intent = Intent(activity, AuthenticationActivity::class.java)
                authenticationLauncher.launch(intent)
            } else {
                val intent = Intent(activity, LogOutActivity::class.java)
                startActivity(intent)
            }
            curUser = FirebaseAuth.getInstance().currentUser
            if (curUser != null) {
                initializeRv(curUser,view)
            }
        }

        return view
    }

    fun initializeRv(currentUser: FirebaseUser?, view: View) {
        fridgeFoodRV = view.findViewById(R.id.favouritesRV)
        if (currentUser == null) {
            return
        }

        val favouritesId = mutableListOf<Int>()

        val userId = currentUser.uid

        val database = FirebaseDatabase.getInstance().getReference("users")
        dataset.clear()

        database.child(userId).child("favouriteRecipes").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.getValue(Int::class.java)
                    value?.let { favouritesId.add(it) }
                }

                if (favouritesId.isEmpty()) {
                    view.findViewById<TextView>(R.id.emptyListText).text = "список рецептов пуст"
                    view.findViewById<TextView>(R.id.emptyListText).visibility = View.VISIBLE
                    fridgeFoodRV.visibility = View.GONE // hide
                } else {
                    view.findViewById<TextView>(R.id.emptyListText).visibility = View.GONE
                    fridgeFoodRV.visibility = View.VISIBLE // show
                }


                for (i in 0..favouritesId.size - 1) {
                    mRecipeViewModel.getRecipe(favouritesId[i]).observe(
                        viewLifecycleOwner,
                        Observer { curRecipeList ->
                            if (curRecipeList.isNotEmpty()) {
                                val curRecipe: Recipe = curRecipeList[0]
                                dataset.add(
                                    Meal(
                                        curRecipe.id,
                                        curRecipe.name,
                                        curRecipe.description
                                    )
                                )
                                if (i == favouritesId.size - 1) { // last element loaded
                                    //fridgeResultAdapter = fridgeResultAdapter(dataset)
                                    fridgeResultAdapter.changeData(dataset)
                                }
                            } else {
                                // Список избранного пуст
                                Toast.makeText(
                                    context, "Список пуст",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context, "ошибка при получении данных: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        // RV initializer
        fridgeResultAdapter = fridgeResultAdapter(dataset)

        fridgeFoodRV.layoutManager = LinearLayoutManager(context)


        fridgeFoodRV.adapter = fridgeResultAdapter
        fridgeResultAdapter.onMealClick = { meal ->
            val intent = Intent(activity, RecipeInfo::class.java)
            intent.putExtra("recipeId", meal.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        var currentUser = FirebaseAuth.getInstance().currentUser



        if (currentUser == null) {
            requireView().findViewById<TextView>(R.id.emptyListText).text = "требуется авторизация"
            requireView().findViewById<TextView>(R.id.emptyListText).visibility = View.VISIBLE
            if (::fridgeFoodRV.isInitialized) {
                fridgeFoodRV.visibility = View.GONE // hide
            }
        } else {
            if (::fridgeFoodRV.isInitialized) {
                requireView().findViewById<TextView>(R.id.emptyListText).visibility = View.GONE
                fridgeFoodRV.visibility = View.VISIBLE // show
            }
        }
    }

}