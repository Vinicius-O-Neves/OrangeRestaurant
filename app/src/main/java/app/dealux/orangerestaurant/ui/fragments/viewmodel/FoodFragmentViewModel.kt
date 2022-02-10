package app.dealux.orangerestaurant.ui.fragments.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dealux.orangerestaurant.data.sqlite.OrderDataBase
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.data.sqlite.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FoodFragmentViewModel @Inject constructor(context: Context) : ViewModel() {

    private val repository: OrderRepository

    init {
        val orderDao = OrderDataBase.getDatabase(context.applicationContext).orderDao()
        repository = OrderRepository(orderDao)
    }

    fun addOrder(order: OrderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

}