package app.dealux.orangerestaurant.data.sqlite

import androidx.lifecycle.LiveData

class OrderRepository(private val orderDao: OrderDao) {

    fun readAllData(): LiveData<List<OrderEntity>> {
        return orderDao.readAllData()
    }

    suspend fun addOrder(order: OrderEntity) {
        orderDao.addOrder(order)
    }

    suspend fun deleteOrder(order: OrderEntity) {
        orderDao.delete(order.itemId)
    }

}