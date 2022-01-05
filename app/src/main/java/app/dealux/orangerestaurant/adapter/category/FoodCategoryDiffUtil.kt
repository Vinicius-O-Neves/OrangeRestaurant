package app.dealux.orangerestaurant.adapter.category

import androidx.recyclerview.widget.DiffUtil
import app.dealux.orangerestaurant.data.model.FoodCategoryModel

class FoodCategoryDiffUtil(
    private val oldList: List<FoodCategoryModel>,
    private val newList: List<FoodCategoryModel>
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
            oldList[oldItemPosition].categoryName != newList[newItemPosition].categoryName-> {
                false
            }
            oldList[oldItemPosition].categoryPhoto != newList[newItemPosition].categoryPhoto -> {
                false
            }
            else -> true
        }
    }

}