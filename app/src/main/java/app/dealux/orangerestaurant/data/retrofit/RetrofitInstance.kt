package app.dealux.orangerestaurant.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: OrangeRestaurantApi by lazy {
        Retrofit.Builder()
            .baseUrl(OrangeRestaurantApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrangeRestaurantApi::class.java)
    }

}