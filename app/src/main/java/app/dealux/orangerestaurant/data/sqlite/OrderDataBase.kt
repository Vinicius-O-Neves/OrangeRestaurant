package app.dealux.orangerestaurant.data.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [OrderEntity::class], version = 2, exportSchema = false)
abstract class OrderDataBase: RoomDatabase() {

    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: OrderDataBase? = null

        fun getDatabase(context: Context): OrderDataBase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDataBase::class.java,
                    "orders_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}