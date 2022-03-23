package app.dealux.orangerestaurant.ui.fragments.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.dealux.orangerestaurant.data.sqlite.OrderDataBase
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.data.sqlite.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragmentViewModel @Inject constructor(context: Context) : ViewModel() {

    var readAllData: LiveData<List<OrderEntity>>

    private val repository: OrderRepository

    init {
        val orderDao = OrderDataBase.getDatabase(context).orderDao()
        repository = OrderRepository(orderDao)
        readAllData = repository.readAllData()
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrder(order)
        }
    }

}