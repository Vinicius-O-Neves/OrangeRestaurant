package app.dealux.orangerestaurant.ui.fragments.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.data.retrofit.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.retrofit.model.FoodItemsModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FoodAndCategoryViewModel @Inject constructor() : ViewModel()  {

    var itemsFound: MutableList<FoodItemsModel> = mutableListOf()
    private lateinit var allItemsResponse: Response<List<FoodItemsModel>>

    private var mCategories = MutableLiveData<List<FoodCategoryModel>>()
    var categories: LiveData<List<FoodCategoryModel>> = mCategories
    private lateinit var categoryResponse: Deferred<Response<List<FoodCategoryModel>>>

    private var mItems = MutableLiveData<List<FoodItemsModel>>()
    var items: LiveData<List<FoodItemsModel>> = mItems
    private lateinit var itemsResponse: Deferred<Response<List<FoodItemsModel>>>


    suspend fun searchItem(newText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allItemsResponse = try {
                RetrofitInstance.api.getAllItems()
            } catch (e: IOException) {
                Log.d("Retrofit", "You might not have internet")
                return@launch
            }

            if (allItemsResponse.isSuccessful) {
                Log.d("Retrofit", "data successfuly load")
                    allItemsResponse.body()!!.forEach { search ->
                        if (search.name.lowercase(Locale.getDefault()).contains(newText)) {
                            itemsFound.add(search)
                        }
                    }
            }
        }
    }


    fun loadCategories() {
        viewModelScope.launch {
            categoryResponse = try {
               async {  RetrofitInstance.api.getCategorys() }
            } catch (e: IOException) {
                Log.d("Retrofit", "You might not have internet")
                return@launch
            }

            if (categoryResponse.await().isSuccessful && categoryResponse.await().body() != null) {
                Log.d("Retrofit", "data successfuly load")
                viewModelScope.launch {
                    mCategories.value = categoryResponse.await().body()
                }
            } else {
                Log.d("Retrofit", "Response not successful")
            }
        }
    }

   fun loadItems(categoryName: String) {
       viewModelScope.launch {
           itemsResponse = try {
               async { RetrofitInstance.api.getFoods(categoryName) }
           } catch (e: IOException) {
               Log.d("Retrofit", "You might not have internet")
               return@launch
           }

           if (itemsResponse.await().isSuccessful) {
               Log.d("Retrofit", "data successfuly load")
               viewModelScope.launch {
                   mItems.value = itemsResponse.await().body()
               }
           }
       }
   }

}