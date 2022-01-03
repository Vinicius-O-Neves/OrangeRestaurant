package app.dealux.orangerestaurant.data.model

import android.net.Uri

data class FoodCategoryModel(
    val id: Int,
    val categoryName: String,
    val categoryPhoto: Uri,
)