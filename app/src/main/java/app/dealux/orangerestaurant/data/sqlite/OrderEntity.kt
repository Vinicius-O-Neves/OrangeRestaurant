package app.dealux.orangerestaurant.data.sqlite

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "orders_table")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val name: String,
    val price: String,
    val photo: String,
    val itemsAdded: String,
    val meatPoint: String,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as OrderEntity

        if ( itemId != other. itemId) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (photo != other.photo) {
            return false
        }
        if (price != other.price) {
            return false
        }
        if (itemsAdded != other.itemsAdded) {
            return false
        }
        if (meatPoint != other.meatPoint) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = itemId
        result = 31 * result + name.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + photo.hashCode()
        result = 31 * result + itemsAdded.hashCode()
        result = 31 * result + meatPoint.hashCode()
        return result
    }
}