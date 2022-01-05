package app.dealux.orangerestaurant.adapter.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import app.dealux.orangerestaurant.databinding.FoodCategoryRvBinding
import com.squareup.picasso.Picasso

class FoodCategoryAdapter(var context: Context, val listener: MyOnClickListener) :
    RecyclerView.Adapter<FoodCategoryAdapter.FoodCategoryViewHolder>() {

    private var selectPosition = 0

    interface MyOnClickListener {
        fun onCardClick(position: Int, items: List<FoodCategoryModel>)
    }

    private var items = emptyList<FoodCategoryModel>()

    inner class FoodCategoryViewHolder(val binding: FoodCategoryRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.categoryPhoto.setOnClickListener {
                    val position = adapterPosition
                    listener.onCardClick(position, items)
                    selectPosition = position
                    notifyDataSetChanged()
                }
            }
        }

    override fun getItemCount(): Int {
        return items.size
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
            Picasso.get().load(item.categoryPhoto).into(categoryPhoto)
            categoryName.text = item.categoryName
            holder.binding.cardView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in)

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

    fun setData(newItems: List<FoodCategoryModel>) {
        val diffUtil = FoodCategoryDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}