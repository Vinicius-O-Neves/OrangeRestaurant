package app.dealux.orangerestaurant.adapter.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.databinding.ItemAddedInFoodRvBinding

class ItemsAddedAdapter(var context: Context) :
    RecyclerView.Adapter<ItemsAddedAdapter.ItemsAddedAdapterViewHolder>(){

    private var items = emptyList<OrderEntity>()

    inner class ItemsAddedAdapterViewHolder(val binding: ItemAddedInFoodRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAddedAdapterViewHolder {
        return ItemsAddedAdapterViewHolder(
            ItemAddedInFoodRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsAddedAdapterViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            val regex = Regex("[^A-Za-z0-9-%]")
            val result = regex.replace(item.itemsAdded, " ")
            "•$result".also { txtItemAddedInFood.text = it }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<OrderEntity>) {
        val differUtil = CartDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(differUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
        CartAdapter(context).notifyDataSetChanged()
    }

}