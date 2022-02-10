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
        return oldList[oldItemPosition].itemId == newList[newItemPosition].itemId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].itemId != newList[newItemPosition].itemId-> {
                false
            }
            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }
            oldList[oldItemPosition].price != newList[newItemPosition].price -> {
                false
            }
            oldList[oldItemPosition].photo != newList[newItemPosition].photo -> {
                false
            }
            oldList[oldItemPosition].itemsAdded != newList[newItemPosition].itemsAdded  -> {
                false
            }
            else -> true
        }
    }
}