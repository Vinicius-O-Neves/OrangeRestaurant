package app.dealux.orangerestaurant.adapter.itemstoadd

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.retrofit.model.AddIngredientModel
import app.dealux.orangerestaurant.databinding.AddIngredientRvBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddIngredientAdapter(var context: Context) :
    RecyclerView.Adapter<AddIngredientAdapter.AddInHamburguerViewHolder>() {

    private var selectPosition: Int? = null

    private var items = emptyList<AddIngredientModel>()

    var itemsAdded: MutableList<String> = mutableListOf()

    inner class AddInHamburguerViewHolder(val binding: AddIngredientRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                selectPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddInHamburguerViewHolder {
        return AddInHamburguerViewHolder(
            AddIngredientRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: AddInHamburguerViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            val item = items[position]
            itemName.text = item.itemName
            Glide.with(context)
                .load("http://${item.itemImage}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemImage)

            CoroutineScope(Dispatchers.Main).launch {
                onCardClick(position, item, holder)
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<AddIngredientModel>) {
        val differUtil = AddIngredientDifferUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(differUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

   private fun onCardClick(
        position: Int,
        item: AddIngredientModel,
        holder: AddInHamburguerViewHolder
    ) {
        if (selectPosition == position && holder.binding.cardView.background.constantState !=
            ContextCompat.getDrawable(
                context,
                R.drawable.food_category_selection_background
            )!!.constantState
        ) {
            itemsAdded.add(item.itemName)
            holder.binding.cardView.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.food_category_selection_background
                )
            holder.binding.itemName.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        } else if (selectPosition == position && holder.binding.cardView.background.constantState ==
            ContextCompat.getDrawable(
                context,
                R.drawable.food_category_selection_background
            )!!.constantState
        ) {
            itemsAdded.remove(item.itemName)
            println(itemsAdded)
            holder.binding.cardView.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.add_ingredient_rv_background
                )
            holder.binding.itemName.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            )
        }
    }

}