package app.dealux.orangerestaurant.data.sqlite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("DELETE FROM orders_table WHERE itemId = :id")
    suspend fun delete(id: Int?)

    @Query("SELECT * FROM orders_table ORDER BY itemId ASC")
    fun readAllData(): LiveData<List<OrderEntity>>

}