package app.dealux.orangerestaurant.data.model

import java.io.Serializable

data class FoodItemsModel(
    val id: Int,
    val name: String,
    val category: String,
    val price: String,
    val description: String,
    val frontCoverUrl: String,
    val imageDescription1: String,
    val imageDescription2: String,
    val serve: String,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FoodItemsModel

        if (id != other.id) {
            return false
        }
        if (category != other.category) {
            return false
        }
        if (price != other.price) {
            return false
        }
        if (frontCoverUrl != other.frontCoverUrl ) {
            return false
        }
        return true
    }

}