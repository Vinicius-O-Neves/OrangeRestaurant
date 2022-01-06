package app.dealux.orangerestaurant.ui.foodandcategoryfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.category.FoodCategoryAdapter
import app.dealux.orangerestaurant.adapter.items.FoodItemsAdapter
import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.model.FoodItemsModel
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.ui.foodfragment.FoodFragment
import app.dealux.orangerestaurant.ui.mainactivity.MainActivity
import kotlinx.coroutines.*
import okhttp3.internal.notify
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class FoodAndCategoryFragment : Fragment(),
    FoodCategoryAdapter.MyOnClickListener,
    FoodItemsAdapter.MyOnClickListener {

    private lateinit var mView: View
    private lateinit var mContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<FoodAndCategoryViewModel> { viewModelFactory }

    private lateinit var rvCategorys: RecyclerView
    private lateinit var rvItems: RecyclerView
    private lateinit var foodCategoryAdapter: FoodCategoryAdapter
    private lateinit var foodItemsAdapter: FoodItemsAdapter
    private lateinit var itemsResponse: Deferred<Response<List<FoodItemsModel>>>

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
        loadFromRetrofit()
        (mContext as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.food_and_category_fragment, container, false)

        setupRecyclerViews()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCategoryCardClick(position: Int, items: List<FoodCategoryModel>) {
        val categoryName = items[position].categoryName

        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                itemsResponse = try {
                    async { RetrofitInstance.api.getFoods(categoryName) }
                } catch (e: IOException) {
                    Log.d("Retrofit", "You might not have internet")
                    return@withContext
                }
                if (itemsResponse.await().isSuccessful && itemsResponse.await().body()!! != null) {
                    Log.d("Retrofit", "data successfuly load")
                    CoroutineScope(Dispatchers.Main.immediate).launch {
                        mView.findViewById<TextView>(R.id.txt_items).text = categoryName
                        foodItemsAdapter.setData(itemsResponse.await().body()!!)
                    }
                } else {
                    Log.d("Retrofit", "Response not successful")
                }
            }
        }
    }

    override fun onFoodCardClick(position: Int, items: List<FoodItemsModel>) {
        val foodFragment = FoodFragment()
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, foodFragment)
            .addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun loadFromRetrofit() {
        CoroutineScope(Dispatchers.Default).launch {
            lifecycleScope.launchWhenCreated {
                val categoryResponse = try {
                    async { RetrofitInstance.api.getCategorys() }
                } catch (e: IOException) {
                    Log.d("Retrofit", "You might not have internet")
                    return@launchWhenCreated
                }
                if (categoryResponse.await().isSuccessful && categoryResponse.await().body() != null) {
                    Log.d("Retrofit", "data successfuly load")
                    CoroutineScope(Dispatchers.Main.immediate).launch {
                        foodCategoryAdapter.setData(categoryResponse.await().body()!!)
                    }
                } else {
                    Log.d("Retrofit", "Response not successful")
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        rvCategorys = mView.findViewById(R.id.rv_categorys)
        rvItems = mView.findViewById(R.id.rv_items)
        CoroutineScope(Dispatchers.Main.immediate).launch {
            foodCategoryAdapter = FoodCategoryAdapter(mView.context, this@FoodAndCategoryFragment)
            rvCategorys.adapter = foodCategoryAdapter
            rvCategorys.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvCategorys.setHasFixedSize(true)

            foodItemsAdapter = FoodItemsAdapter(mView.context, this@FoodAndCategoryFragment)
            rvItems.adapter = foodItemsAdapter
            rvItems.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvItems.setHasFixedSize(true)
            rvItems.startLayoutAnimation()

        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViews()
        foodCategoryAdapter.notifyDataSetChanged()
        foodItemsAdapter.notifyDataSetChanged()
    }

}