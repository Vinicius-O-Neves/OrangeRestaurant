package app.dealux.orangerestaurant.data.retrofit.model

import java.io.Serializable

data class AddIngredientModel(
    val id: Int,
    val itemName: String,
    val itemImage: String,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as AddIngredientModel

        if (id != other.id) {
            return false
        }
        if (itemName != other.itemName) {
            return false
        }
        if (itemImage != other.itemImage) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + itemName.hashCode()
        result = 31 * result + itemImage.hashCode()
        return result
    }

}