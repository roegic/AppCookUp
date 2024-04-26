package com.example.mycookingapp.home_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingapp.home_screen.data.FridgeProducts
import com.example.mycookingapp.home_screen.data.ProductCategory
import com.example.mycookingapp.R
import com.example.mycookingapp.databinding.FridgeIngredientBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class FridgeIngredientsAdapter(private var dataset: MutableList<ProductCategory>) : RecyclerView.Adapter<FridgeIngredientsAdapter.MultiselectViewHolder>() {


    var foodSelectedList = arrayListOf<String>()

    class MultiselectViewHolder(item: View, parent: FridgeIngredientsAdapter) : RecyclerView.ViewHolder(item) {
        val binding = FridgeIngredientBinding.bind(item)
        fun bind(productCategory: ProductCategory, parent: FridgeIngredientsAdapter){
            binding.fridgeProductTitle.text = productCategory.name
            binding.ChildProductFromCategory.setHasFixedSize(true)

            val flexLayoutManager = FlexboxLayoutManager(itemView.context)
            flexLayoutManager.flexDirection = FlexDirection.ROW
            binding.ChildProductFromCategory.layoutManager = flexLayoutManager
            val adapter = FridgeIngredientsAdapterChild(productCategory.products, parent)
            binding.ChildProductFromCategory.adapter = adapter
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.fridge_ingredient, parent, false)
        return MultiselectViewHolder(adapterLayout, this)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item, this)
    }

    fun makeSearchRequest(): ArrayList<String> {
        val t = ArrayList(foodSelectedList.toList())
        return t
    }

    fun onApplySearch(dataset: MutableList<ProductCategory>){

        this.dataset = dataset
        notifyDataSetChanged()
    }

}