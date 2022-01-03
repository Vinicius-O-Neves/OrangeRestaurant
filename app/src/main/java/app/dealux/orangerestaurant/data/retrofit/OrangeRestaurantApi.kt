package app.dealux.orangerestaurant.data.retrofit

import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import retrofit2.Response
import retrofit2.http.GET

interface OrangeRestaurantApi {

    @GET("/items/categorys")
    suspend fun getCategorys(): Response<List<FoodCategoryModel>>

    companion object {
        const val BASE_URL = "http://192.168.15.14:8140"
    }

}