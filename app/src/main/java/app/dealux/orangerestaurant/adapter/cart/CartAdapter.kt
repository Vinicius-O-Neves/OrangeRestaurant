package app.dealux.orangerestaurant.adapter.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.databinding.ItemInCartRvBinding
import com.bumptech.glide.Glide

class CartAdapter(var context: Context) :
    RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder>() {

    private var itemsAddedAdapter: ItemsAddedAdapter? = null

    private var recyclerViewsLoad: Int = 0

    private var items = emptyList<OrderEntity>()

    inner class CartAdapterViewHolder(val binding: ItemInCartRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.CartAdapterViewHolder {
        return CartAdapterViewHolder(
            ItemInCartRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartAdapter.CartAdapterViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            val itemsAddedRv: RecyclerView = holder.binding.itemsAddedRv
            Glide.with(context).load(item.photo).into(itemImage)
            itemName.text = item.name
            "R$ ${item.price}".also { itemPrice.text = it }
            itemsAddedAdapter = ItemsAddedAdapter(context)
            itemsAddedRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemsAddedRv.setHasFixedSize(true)
            itemsAddedRv.adapter = itemsAddedAdapter
            itemsAddedAdapter!!.setData(items)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(newItems: List<OrderEntity>) {
        val diffUtil = CartDiffUtil(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}
