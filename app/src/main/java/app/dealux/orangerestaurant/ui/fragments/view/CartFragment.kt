package app.dealux.orangerestaurant.ui.fragments.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.cart.CartAdapter
import app.dealux.orangerestaurant.data.datastore.TableNumberDataStoreRepository
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.databinding.CartFragmentBinding
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import app.dealux.orangerestaurant.ui.fragments.viewmodel.CartFragmentViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import javax.inject.Inject

class CartFragment : Fragment(),
    CartAdapter.MyOnClickListener,
    View.OnClickListener {

    private var binding: CartFragmentBinding? = null
    private lateinit var mContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<CartFragmentViewModel> { viewModelFactory }

    private lateinit var database: DatabaseReference

    private lateinit var rvItemsInCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        (mContext as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CartFragmentBinding.inflate(layoutInflater)

        setupRecyclerView()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener()

        val repository = TableNumberDataStoreRepository(requireContext())
        val readFromDataStore = repository.read().asLiveData()

        readFromDataStore.observe(viewLifecycleOwner) { table ->
            binding!!.txtTable.text = table
        }

    }

    private fun setupRecyclerView() {
        rvItemsInCart = binding!!.itemInCartRv

        cartAdapter =
            CartAdapter(
                mContext.applicationContext,
                binding!!.totalOfItems,
                binding!!.total,
                binding!!.totalOfItemsAdded,
                this@CartFragment
            )
        cartAdapter.setHasStableIds(true)
        rvItemsInCart.adapter = cartAdapter
        rvItemsInCart.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rvItemsInCart.startLayoutAnimation()

        mViewModel.readAllData.observe(viewLifecycleOwner) { orderList ->
            cartAdapter.setData(orderList)
        }
    }

    private fun listener() {
        binding!!.btnBuy.setOnClickListener(this@CartFragment)
        binding!!.cardViewTable.setOnClickListener(this@CartFragment)
    }

    override fun onDeleteClick(position: Int, order: List<OrderEntity>) {
        mViewModel.deleteOrder(order[position])

        val cartFragment = FoodAndCategoryFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, cartFragment, "food_and_category_fragment")
            .addToBackStack(null)

        Toast.makeText(requireContext(), "Item excluido com sucesso!", Toast.LENGTH_SHORT).show()
        fragmentTransaction.commit()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_buy -> {
                val order = cartAdapter.items

                database = FirebaseDatabase.getInstance().getReference("Orders")
                database.child(binding!!.txtTable.text[5].toString()).setValue(order).addOnSuccessListener {
                    Log.d("sucess", "send with sucess")
                }.addOnFailureListener {
                    Log.d("sucess", "error on send")
                }
            }
            R.id.card_view_table -> {
                val selectTableFragment = SelectTableFragment()

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    .hide(requireActivity().supportFragmentManager.findFragmentByTag("cart_fragment")!!)
                    .add(R.id.fragment, selectTableFragment, "select_table_fragment")
                    .addToBackStack(null)

                CoroutineScope(Dispatchers.IO).cancel()
                CoroutineScope(Dispatchers.Main).cancel()
                fragmentTransaction.commit()
            }
        }
    }

}