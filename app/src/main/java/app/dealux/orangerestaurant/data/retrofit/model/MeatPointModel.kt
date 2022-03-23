package app.dealux.orangerestaurant.data.retrofit.model

import java.io.Serializable

data class MeatPointModel (
    val id: Int,
    val meatPointName: String,
    val meatPointImage: String,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as AddIngredientModel

        if (id != other.id) {
            return false
        }
        if (meatPointName != other.itemName) {
            return false
        }
        if (meatPointImage != other.itemImage) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + meatPointName.hashCode()
        result = 31 * result + meatPointImage.hashCode()
        return result
    }

}