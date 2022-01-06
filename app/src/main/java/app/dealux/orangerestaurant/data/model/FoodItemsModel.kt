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
) : Serializable