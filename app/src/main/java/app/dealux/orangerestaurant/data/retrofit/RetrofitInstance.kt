package app.dealux.orangerestaurant.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    val api: OrangeRestaurantApi by lazy {
        Retrofit.Builder()
            .baseUrl(OrangeRestaurantApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrangeRestaurantApi::class.java)
    }

    val orderApi: OrangeRestaurantApi by lazy {
        Retrofit.Builder()
            .baseUrl(OrangeRestaurantApi.ORDER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrangeRestaurantApi::class.java)
    }

}