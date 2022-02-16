package app.dealux.orangerestaurant.data.retrofit

import app.dealux.orangerestaurant.data.retrofit.model.*
import retrofit2.Response
import retrofit2.http.*

interface OrangeRestaurantApi {

    @GET("/categorys")
    suspend fun getCategorys(): Response<List<FoodCategoryModel>>

    @GET("/")
    suspend fun getAllItems(): Response<List<FoodItemsModel>>

    @GET("/category/{category}")
    suspend fun getFoods(
        @Path("category") category: String): Response<List<FoodItemsModel>>

    @GET("/addInHamburguer")
    suspend fun getItemsToAddInHamburguer(): Response<List<AddIngredientModel>>

    @GET("/meatPoint")
    suspend fun getMeatPoints(): Response<List<MeatPointModel>>

    companion object {
        const val BASE_URL = "http://192.168.15.14:8140"
    }

}