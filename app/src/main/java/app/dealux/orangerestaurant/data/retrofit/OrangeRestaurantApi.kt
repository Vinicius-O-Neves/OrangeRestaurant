package app.dealux.orangerestaurant.data.retrofit

import app.dealux.orangerestaurant.data.retrofit.model.*
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrangeRestaurantApi {

    @GET("/categorys")
    suspend fun getCategorys(): Response<List<FoodCategoryModel>>

    @GET("/category/{category}")
    suspend fun getFoods(
        @Path("category") category: String): Response<List<FoodItemsModel>>

    @GET("/addInHamburguer")
    suspend fun getItemsToAddInHamburguer(): Response<List<AddIngredientModel>>

    @GET("/meatPoint")
    suspend fun getMeatPoints(): Response<List<MeatPointModel>>

    @POST("/order")
    suspend fun sendOrder(): Response<List<OrderEntity>>

    companion object {
        const val BASE_URL = "http://192.168.15.14:8140"
        const val ORDER_URL = "http://192.168.15.14:3000"
    }

}