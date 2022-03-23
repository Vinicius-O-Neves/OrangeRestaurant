package app.dealux.orangerestaurant.adapter.itemstoadd

import androidx.recyclerview.widget.DiffUtil
import app.dealux.orangerestaurant.data.retrofit.model.AddIngredientModel

class AddIngredientDifferUtil(
    private val oldList: List<AddIngredientModel>,
    private val newList: List<AddIngredientModel>,
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
            oldList[oldItemPosition].id != newList[newItemPosition].id-> {
                false
            }
            oldList[oldItemPosition].itemName != newList[newItemPosition].itemName -> {
                false
            }
            oldList[oldItemPosition].itemImage != newList[newItemPosition].itemImage -> {
                false
            }
            else -> true
        }
    }

}