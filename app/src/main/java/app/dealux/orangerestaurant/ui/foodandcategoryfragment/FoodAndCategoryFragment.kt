package app.dealux.orangerestaurant.ui.foodandcategoryfragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class FoodAndCategoryFragment : Fragment(),
    FoodCategoryAdapter.MyOnClickListener,
    FoodItemsAdapter.MyOnClickListener {

    private lateinit var mView: View
    private lateinit var mContext: Context
    private lateinit var mSavedInstanceState: Bundle

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<FoodAndCategoryViewModel> { viewModelFactory }

    private lateinit var rvCategorys: RecyclerView
    private lateinit var foodCategoryAdapter: FoodCategoryAdapter
    private lateinit var categoryResponse: Deferred<Response<List<FoodCategoryModel>>>
    private var lastRvCategoryPosition: Parcelable? = null

    private lateinit var rvItems: RecyclerView
    private lateinit var foodItemsAdapter: FoodItemsAdapter
    private lateinit var itemsResponse: Deferred<Response<List<FoodItemsModel>>>
    private var lastRvItemsPosition: Parcelable? = null

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
        (mContext as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.food_and_category_fragment, container, false)

        if (savedInstanceState == null) {
            loadFromRetrofit()
            setupRecyclerViews()
        } else {
            restorePreviousState()
        }
        mSavedInstanceState = Bundle()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                withContext(Dispatchers.Main.immediate) {
                    mView.findViewById<TextView>(R.id.txt_items).text = categoryName
                }
            } else {
                Log.d("Retrofit", "Response not successful")
            }
        }
    }

    override fun onFoodCardClick(position: Int, items: List<FoodItemsModel>) {
        val bundle = Bundle()
        bundle.putString("imageDescription1", items[position].imageDescription1)
        bundle.putString("foodName", items[position].name)
        bundle.putString("serve", items[position].serve)
        bundle.putString("foodPrice", items[position].price)
        val foodFragment = FoodFragment()
        foodFragment.arguments = bundle
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            .hide(activity!!.supportFragmentManager.findFragmentByTag("food_and_category_fragment")!!)
            .add(R.id.fragment, foodFragment, "food_fragment")
            .addToBackStack(null)
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
        rvCategorys = mView.findViewById(R.id.rv_categorys)
        rvItems = mView.findViewById(R.id.rv_items)

        foodCategoryAdapter = FoodCategoryAdapter(mView.context, this@FoodAndCategoryFragment)
        foodCategoryAdapter.setHasStableIds(true)
        rvCategorys.adapter = foodCategoryAdapter
        rvCategorys.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategorys.setHasFixedSize(true)

        foodItemsAdapter = FoodItemsAdapter(mView.context, this@FoodAndCategoryFragment)
        foodItemsAdapter.setHasStableIds(true)
        rvItems.adapter = foodItemsAdapter
            rvItems.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.startLayoutAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lastRvCategoryPosition = rvCategorys.layoutManager!!.onSaveInstanceState()
        outState.putParcelable("1324", lastRvCategoryPosition)

        lastRvItemsPosition = rvItems.layoutManager!!.onSaveInstanceState()
        outState.putParcelable("1325", lastRvCategoryPosition)
    }

    private fun restorePreviousState() {
        lastRvCategoryPosition = mSavedInstanceState.getParcelable("1324")
        loadFromRetrofit()
        setupRecyclerViews()
        foodItemsAdapter.notifyDataSetChanged()
        foodCategoryAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        restorePreviousState()
    }

}