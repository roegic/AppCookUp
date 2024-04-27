package com.example.mycookingapp.home_screen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.databinding.MealConfigBinding

class MealConfigAdapter(private var dataset: MutableList<String>) :
    RecyclerView.Adapter<MealConfigAdapter.SelectViewHolder>() {

    var selectedType = ""

    var currentSelectedItem = RecyclerView.NO_POSITION
    val timeDataset = mutableListOf<String>("15", "30", "45", "60")

    class SelectViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = MealConfigBinding.bind(item)

        fun bind(meal: String, position: Int, curPos: Int) {
            binding.mealConfigTitle.text = meal
            if (curPos == position) {
                binding.productFieldMeal.setCardBackgroundColor(Color.GREEN)
            } else {
                binding.productFieldMeal.setCardBackgroundColor(Color.WHITE)
                binding.mealConfigTitle.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.meal_config, parent, false)
        return SelectViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item, position, currentSelectedItem)
        holder.binding.mealConfigTitle.setOnClickListener {
            if (position == currentSelectedItem) { // user wants to delete existing ingredient
                currentSelectedItem = RecyclerView.NO_POSITION
                notifyItemChanged(position)
            } else { // user wants to add an option but we can have only one option
                holder.binding.productFieldMeal.setCardBackgroundColor(COLOR_GREEN_SELECTED)
                holder.binding.mealConfigTitle.setTextColor(Color.WHITE)
                notifyItemChanged(currentSelectedItem)
                currentSelectedItem = position
                if (dataset[position][0] == '<') {
                    selectedType =
                        (timeDataset[position]) // if we have time we need to see what number is str
                } else {
                    selectedType = (dataset[position])
                }
            }
            true
        }
    }

    fun makeSearchRequest(): String {
        return selectedType
    }
}