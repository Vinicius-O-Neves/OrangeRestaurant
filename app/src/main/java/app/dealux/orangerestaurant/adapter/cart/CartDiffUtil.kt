package app.dealux.orangerestaurant.adapter.cart

import androidx.recyclerview.widget.DiffUtil
import app.dealux.orangerestaurant.data.sqlite.OrderEntity

class CartDiffUtil(
    private val oldList: List<OrderEntity>,
    private val newList: List<OrderEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }
            oldList[oldItemPosition].price != newList[newItemPosition].price -> {
                false
            }
            oldList[oldItemPosition].itemsAdded != newList[newItemPosition].itemsAdded  -> {
                false
            }
            else -> true
        }
    }
}