package app.dealux.orangerestaurant.data.retrofit

import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.model.FoodItemsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrangeRestaurantApi {

    @GET("/categorys")
    suspend fun getCategorys(): Response<List<FoodCategoryModel>>

    @GET("/category/{category}")
    suspend fun getFoods(
        @Path("category") category: String): Response<List<FoodItemsModel>>

    companion object {
        const val BASE_URL = "http://192.168.15.14:8140"
    }

}