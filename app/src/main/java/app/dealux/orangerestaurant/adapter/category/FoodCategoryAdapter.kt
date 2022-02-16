package app.dealux.orangerestaurant.adapter.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.retrofit.model.FoodCategoryModel
import app.dealux.orangerestaurant.databinding.FoodCategoryRvBinding
import com.bumptech.glide.Glide

class FoodCategoryAdapter(var context: Context, val listener: MyOnClickListener) :
    RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder>() {

    private var selectPosition = 0

    private var items = emptyList<FoodCategoryModel>()

    interface MyOnClickListener {
        fun onCategoryCardClick(position: Int, selectPosition: Int, itemsList: List<FoodCategoryModel>)
    }

    inner class FoodCategoryViewHolder(val binding: FoodCategoryRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.categoryPhoto.setOnClickListener {
                val position = adapterPosition
                selectPosition = position
                listener.onCategoryCardClick(position, selectPosition, items)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCategoryViewHolder {
        return FoodCategoryViewHolder(
            FoodCategoryRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodCategoryViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            categoryName.text = item.categoryName
            Glide.with(context).load(item.categoryPhoto).into(categoryPhoto)

            if (selectPosition == position) {
                holder.binding.linearLayout.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.food_category_selection_background
                    )
                holder.binding.categoryName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            } else {
                holder.binding.linearLayout.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.food_category_background
                    )
                holder.binding.categoryName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.btn_lets_start
                    )
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<FoodCategoryModel>) {
        val diffUtil = FoodCategoryDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}