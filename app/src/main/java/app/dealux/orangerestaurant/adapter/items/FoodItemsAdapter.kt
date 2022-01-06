package app.dealux.orangerestaurant.adapter.items

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.model.FoodItemsModel
import app.dealux.orangerestaurant.databinding.FoodItemsRvBinding
import com.squareup.picasso.Picasso

class FoodItemsAdapter(var context: Context, val listener: MyOnClickListener) :
    RecyclerView.Adapter<FoodItemsAdapter.FoodItemsViewHolder>() {

    private var selectPosition = 0

    interface MyOnClickListener {
        fun onFoodCardClick(position: Int, items: List<FoodItemsModel>)
    }

    private var items = emptyList<FoodItemsModel>()

    inner class FoodItemsViewHolder(val binding: FoodItemsRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.foodPhoto.setOnClickListener {
                val position = adapterPosition
                listener.onFoodCardClick(position, items)
                selectPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemsViewHolder {
        return FoodItemsViewHolder(
            FoodItemsRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodItemsViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            Picasso.get().load(item.frontCoverUrl).into(foodPhoto)
            foodName.text = item.name
            "R$ ${item.price}".also { foodPrice.text = it }
            holder.binding.cardView.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in)
        }
    }

    fun setData(newItems: List<FoodItemsModel>) {
        val diffUtil = FoodItemsDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}