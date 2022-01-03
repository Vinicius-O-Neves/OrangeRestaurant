package app.dealux.orangerestaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import app.dealux.orangerestaurant.databinding.ActivityMainBinding
import app.dealux.orangerestaurant.databinding.FoodCategoryRvBinding
import com.squareup.picasso.Picasso

class FoodCategoryAdapter: RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder>() {

    inner class FoodCategoryViewHolder(val binding: FoodCategoryRvBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<FoodCategoryModel>() {
        override fun areItemsTheSame(
            oldItem: FoodCategoryModel,
            newItem: FoodCategoryModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FoodCategoryModel,
            newItem: FoodCategoryModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    var items: List<FoodCategoryModel>
        get() = differ.currentList
        set(value) { differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCategoryViewHolder {
       return FoodCategoryViewHolder(FoodCategoryRvBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       ))
    }

    override fun onBindViewHolder(holder: FoodCategoryViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            Picasso.get().load(item.categoryPhoto).into(categoryPhoto)
            categoryName.text = item.categoryName
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}