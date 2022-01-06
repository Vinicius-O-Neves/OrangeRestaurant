package app.dealux.orangerestaurant.data.model

import android.os.Parcelable
import java.io.Serializable

data class FoodCategoryModel(
    val id: Int,
    val categoryName: String,
    val categoryPhoto: String,
) : Serializable