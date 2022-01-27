package app.dealux.orangerestaurant.adapter.itemstoadd

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.retrofit.model.MeatPointModel
import app.dealux.orangerestaurant.databinding.MeatPointRvBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeatPointAdapter(var context: Context, val listener: MyOnClickListener) :
    RecyclerView.Adapter<MeatPointAdapter.MeatPointViewHolder>() {

    private var selectPosition: Int? = null

    private var items = emptyList<MeatPointModel>()

    private lateinit var meatPoint: String

    interface MyOnClickListener {
        fun onMeatPointCardClick(position: Int, items: List<MeatPointModel>)
    }

    inner class MeatPointViewHolder(val binding: MeatPointRvBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeatPointViewHolder {
        return MeatPointViewHolder(
            MeatPointRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MeatPointViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            itemName.text = item.meatPointName
            Glide.with(context).load(item.meatPointImage).into(itemImage)

            CoroutineScope(Dispatchers.Main).launch {
                onCardClick(position, item, holder)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<MeatPointModel>) {
        val differUtil = MeatPointDifferUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(differUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    private fun onCardClick(
        position: Int,
        item: MeatPointModel,
        holder: MeatPointViewHolder
    ) {
        if (selectPosition == position) {
            meatPoint = item.meatPointName
            println(meatPoint)
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
        } else {
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