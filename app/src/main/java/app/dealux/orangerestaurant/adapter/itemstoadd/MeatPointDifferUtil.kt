package app.dealux.orangerestaurant.adapter.itemstoadd

import androidx.recyclerview.widget.DiffUtil
import app.dealux.orangerestaurant.data.model.MeatPointModel

class MeatPointDifferUtil(
    private val oldList: List<MeatPointModel>,
    private val newList: List<MeatPointModel>,
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
            oldList[oldItemPosition].meatPointName != newList[newItemPosition].meatPointName -> {
                false
            }
            oldList[oldItemPosition].meatPointImage != newList[newItemPosition].meatPointImage -> {
                false
            }
            else -> true
        }
    }

}