package com.example.mycookingapp.home_screen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.databinding.FridgeIngredientChildBinding
import com.example.mycookingapp.home_screen.data.FridgeProducts

val COLOR_GREEN_SELECTED = Color.parseColor("#90C668")
val COLOR_GRAY_SELECTED = Color.parseColor("#F0F2F4")

class FridgeIngredientsAdapterChild(
    private var dataset: MutableList<FridgeProducts>,
    var parentAdapter: FridgeIngredientsAdapter
) : RecyclerView.Adapter<FridgeIngredientsAdapterChild.MultiselectViewHolder>() {

    var foodSelectedList = arrayListOf<String>()

    class MultiselectViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = FridgeIngredientChildBinding.bind(item)
        fun bind(product: FridgeProducts) {
            binding.fridgeProductTitleChild.text = product.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fridge_ingredient_child, parent, false)
        return MultiselectViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
        if (parentAdapter.foodSelectedList.contains(dataset[position].name)) {
            holder.binding.productFieldChild.setCardBackgroundColor(COLOR_GREEN_SELECTED)
        } else {
            holder.binding.productFieldChild.setCardBackgroundColor(COLOR_GRAY_SELECTED)
        }
        holder.binding.productFieldChild.setOnClickListener {
            if (parentAdapter.foodSelectedList.contains(dataset[position].name)) {
                holder.binding.productFieldChild.setCardBackgroundColor(COLOR_GRAY_SELECTED)
                dataset[position].isSelected = false
                holder.binding.fridgeProductTitleChild.setTextColor(Color.BLACK)
                parentAdapter.foodSelectedList.remove(dataset[position].name)
            } else { // user wants to add an ingredient
                dataset[position].isSelected = true
                holder.binding.productFieldChild.setCardBackgroundColor(COLOR_GREEN_SELECTED)
                holder.binding.fridgeProductTitleChild.setTextColor(Color.WHITE)
                foodSelectedList.add(dataset[position].name)
                parentAdapter.foodSelectedList.add(dataset[position].name)
            }
            true
        }
    }

}