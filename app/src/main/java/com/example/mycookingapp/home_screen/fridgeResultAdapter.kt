package com.example.mycookingapp.home_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.R
import com.example.mycookingapp.databinding.FridgeResultBinding
import com.example.mycookingapp.home_screen.data.Meal

class fridgeResultAdapter(private var dataset: List<Meal>) :
    RecyclerView.Adapter<fridgeResultAdapter.SelectMealHolder>() {

    lateinit var onMealClick: ((Meal) -> Unit)

    class SelectMealHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = FridgeResultBinding.bind(item)

        fun bind(meal: Meal) {
            binding.fridgeResultTitle.text = meal.name
            binding.fridgeResultDescription.text = meal.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMealHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fridge_result, parent, false)
        return SelectMealHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: SelectMealHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
        holder.binding.fridgeResultCard.setOnClickListener {
            onMealClick.invoke(dataset[position])
            true
        }
    }

    fun changeData(new_dataset: List<Meal>) {
        this.dataset = new_dataset
        notifyDataSetChanged()
    }
}