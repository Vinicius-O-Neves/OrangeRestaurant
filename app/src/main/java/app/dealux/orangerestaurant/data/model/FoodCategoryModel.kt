package app.dealux.orangerestaurant.data.model

import java.io.Serializable

data class FoodCategoryModel(
    val id: Int,
    val categoryName: String,
    val categoryPhoto: String,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FoodCategoryModel

        if (id != other.id) {
            return false
        }
        if (categoryName != other.categoryName) {
            return false
        }
        if (categoryPhoto != other.categoryPhoto) {
            return false
        }
        return true
    }

}