package app.dealux.orangerestaurant.ui.fragments.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.category.FoodCategoryAdapter
import app.dealux.orangerestaurant.adapter.items.FoodItemsAdapter
import app.dealux.orangerestaurant.data.retrofit.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.retrofit.model.FoodItemsModel
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.databinding.FoodAndCategoryFragmentBinding
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException

class FoodAndCategoryFragment : Fragment(),
    FoodCategoryAdapter.MyOnClickListener,
    FoodItemsAdapter.MyOnClickListener {

    private var binding: FoodAndCategoryFragmentBinding? = null
    private lateinit var mContext: Context

    private lateinit var rvCategorys: RecyclerView
    private lateinit var foodCategoryAdapter: FoodCategoryAdapter
    private lateinit var categoryResponse: Deferred<Response<List<FoodCategoryModel>>>

    private lateinit var rvItems: RecyclerView
    private lateinit var foodItemsAdapter: FoodItemsAdapter
    private lateinit var itemsResponse: Deferred<Response<List<FoodItemsModel>>>

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoodAndCategoryFragmentBinding.inflate(layoutInflater)
        mContext = requireContext()

        setupRecyclerViews()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFromRetrofit()
    }

    override fun onCategoryCardClick(
        position: Int,
        selectPosition: Int,
        items: List<FoodCategoryModel>
    ) {
        val categoryName = items[position].categoryName

        CoroutineScope(Dispatchers.IO).launch {
            itemsResponse = try {
                async { RetrofitInstance.api.getFoods(categoryName) }
            } catch (e: IOException) {
                Log.d("Retrofit", "You might not have internet")
                return@launch
            }
            if (itemsResponse.await().isSuccessful && itemsResponse.await().body()!! != null) {
                Log.d("Retrofit", "data successfuly load")
                foodItemsAdapter.setData(itemsResponse.await().body()!!)
                CoroutineScope(Dispatchers.Main.immediate).launch {
                    binding!!.txtItems.text = categoryName
                }
            } else {
                Log.d("Retrofit", "Response not successful")
            }
        }
    }

    override fun onFoodCardClick(position: Int, items: List<FoodItemsModel>) {
        val bundle = Bundle()
        bundle.putString("frontCoverUrl", items[position].frontCoverUrl)
        bundle.putString("imageDescription1", items[position].imageDescription1)
        bundle.putString("foodName", items[position].name)
        bundle.putString("serve", items[position].serve)
        bundle.putString("foodPrice", items[position].price)
        bundle.putString("foodDescription", items[position].description)
        bundle.putString("category", items[position].category)

        val foodFragment = FoodFragment()
        foodFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            .hide(requireActivity().supportFragmentManager.findFragmentByTag("food_and_category_fragment")!!)
            .add(R.id.fragment, foodFragment, "food_fragment")
            .addToBackStack(null)

        CoroutineScope(Dispatchers.IO).cancel()
        CoroutineScope(Dispatchers.Main).cancel()
        fragmentTransaction.commit()
    }

    private fun loadFromRetrofit() {
        CoroutineScope(Dispatchers.IO).launch {
            categoryResponse = try {
                async { RetrofitInstance.api.getCategorys() }
            } catch (e: IOException) {
                Log.d("Retrofit", "You might not have internet")
                return@launch
            }
            if (categoryResponse.await().isSuccessful && categoryResponse.await().body() != null) {
                Log.d("Retrofit", "data successfuly load")
                foodCategoryAdapter.setData(categoryResponse.await().body()!!)
            } else {
                Log.d("Retrofit", "Response not successful")
            }
        }
    }

    private fun setupRecyclerViews() {
        rvCategorys = binding!!.rvCategorys
        rvItems = binding!!.rvItems

        foodCategoryAdapter = FoodCategoryAdapter(mContext, this@FoodAndCategoryFragment)
        foodCategoryAdapter.setHasStableIds(true)
        rvCategorys.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategorys.setHasFixedSize(true)
        rvCategorys.adapter = foodCategoryAdapter

        foodItemsAdapter = FoodItemsAdapter(mContext, this@FoodAndCategoryFragment)
        foodItemsAdapter.setHasStableIds(true)
        rvItems.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.startLayoutAnimation()
        rvItems.adapter = foodItemsAdapter
    }

}