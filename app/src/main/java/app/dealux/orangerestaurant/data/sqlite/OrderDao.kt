package app.dealux.orangerestaurant.data.sqlite

import androidx.room.*

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("SELECT * FROM orders_table ORDER BY id ASC")
    suspend fun readAllData(): List<OrderEntity>

}