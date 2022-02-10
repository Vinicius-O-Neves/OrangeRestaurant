package app.dealux.orangerestaurant.adapter.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.databinding.ItemInCartRvBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(
    var context: Context,
    private var totalOfItem: TextView,
    private var total: TextView,
    private var totalOfItemsAdded: TextView,
    private val listener: MyOnClickListener
) :
    RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder>() {

    private var itemsAddedAdapter: ItemsAddedAdapter? = null

    var items = emptyList<OrderEntity>()
    private var item: OrderEntity? = null

    private var orderList: List<OrderEntity>? = null

    private var totalOfItems: Int = 0

    interface MyOnClickListener {
        fun onDeleteClick(position: Int, order: List<OrderEntity>)
    }

    inner class CartAdapterViewHolder(val binding: ItemInCartRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val itemsAddedRv: RecyclerView = binding.itemsAddedRv
            itemsAddedAdapter = ItemsAddedAdapter(context)
            itemsAddedRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemsAddedRv.adapter = itemsAddedAdapter

            binding.delete.setOnClickListener {
                listener.onDeleteClick(adapterPosition, items)
                CoroutineScope(Dispatchers.IO).launch {
                    subPrices(listOf(item!!.price.toInt()))
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].itemId.toLong()
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
        item = items[position]
        holder.binding.apply {
            Glide.with(context).load(item!!.photo).into(itemImage)
            itemName.text = item!!.name
            "R$ ${item!!.price}".also { itemPrice.text = it }
            itemsAddedAdapter!!.setData(listOf(item!!))
            holder.binding.cardView.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_down)

            for (price in listOf(item!!.price)) {
                totalOfItems += if (item!!.itemsAdded != "") {
                    price.toInt() + 6 * item!!.itemsAdded.split(",").size
                } else {
                    price.toInt()
                }
                "R$ $totalOfItems".also { totalOfItem.text = it }
                "R$ $totalOfItems".also { total.text = it }
                if (item!!.itemsAdded != "") {
                    "R$ ${6 * item!!.itemsAdded.split(",").size}".also { totalOfItemsAdded.text = it }
                }
            }
        }
        getItems(listOf(item!!))
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

    private fun subPrices(prices: List<Int>) {
        for (price in prices.indices) {
            totalOfItems -= prices[price]
            Log.v("total", totalOfItems.toString())
        }
        CoroutineScope(Dispatchers.Main).launch {
            "R$ $totalOfItems".also { totalOfItem.text = it }
            "R$ $totalOfItems".also { total.text = it }
        }
    }

    private fun getItems(orders: List<OrderEntity>) {
        orderList = orders
    }

}
