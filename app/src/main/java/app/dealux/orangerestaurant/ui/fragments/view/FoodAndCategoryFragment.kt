package app.dealux.orangerestaurant.ui.fragments.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import  androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.category.FoodCategoryAdapter
import app.dealux.orangerestaurant.adapter.items.FoodItemsAdapter
import app.dealux.orangerestaurant.data.retrofit.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.retrofit.model.FoodItemsModel
import app.dealux.orangerestaurant.databinding.FoodAndCategoryFragmentBinding
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import app.dealux.orangerestaurant.ui.fragments.viewmodel.FoodAndCategoryViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

class FoodAndCategoryFragment : Fragment(),
    FoodCategoryAdapter.MyOnClickListener,
    FoodItemsAdapter.MyOnClickListener {

    private var binding: FoodAndCategoryFragmentBinding? = null
    private lateinit var mContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<FoodAndCategoryViewModel> { viewModelFactory }

    private lateinit var rvCategories: RecyclerView
    private lateinit var foodCategoryAdapter: FoodCategoryAdapter

    private lateinit var rvItems: RecyclerView
    private lateinit var foodItemsAdapter: FoodItemsAdapter

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
        binding = FoodAndCategoryFragmentBinding.inflate(layoutInflater)
        mContext = requireContext()

        loadFromRetrofit()
        setupRecyclerViews()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFromRetrofit()
        searchView()
    }

    private fun searchView() {
        binding!!.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    lifecycleScope.launchWhenCreated {
                        mViewModel.searchItem(newText.lowercase())
                    }

                    foodItemsAdapter.setData(mViewModel.itemsFound)
                    "Itens com $newText".also {  binding!!.txtItems.text = it }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvItems.removeAllViews()
                mViewModel.itemsFound.clear()
                return true
            }
        })
    }

    override fun onCategoryCardClick(
        position: Int,
        selectPosition: Int,
        itemsList: List<FoodCategoryModel>
    ) {
        val categoryName = itemsList[position].categoryName

        mViewModel.loadItems(categoryName)

        binding!!.txtItems.text = categoryName
        mViewModel.items.observe(viewLifecycleOwner) { items ->
            foodItemsAdapter.setData(items)
        }
    }

    override fun onFoodCardClick(
        position: Int,
        items: List<FoodItemsModel>
     ) {
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
        mViewModel.loadCategories()

        mViewModel.categories.observe(viewLifecycleOwner) { categories ->
            foodCategoryAdapter.setData(categories)
        }
    }

    private fun setupRecyclerViews() {
        rvCategories = binding!!.rvCategories
        rvItems = binding!!.rvItems

        foodCategoryAdapter = FoodCategoryAdapter(mContext, this@FoodAndCategoryFragment)
        foodCategoryAdapter.setHasStableIds(true)
        rvCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.setHasFixedSize(true)
        rvCategories.adapter = foodCategoryAdapter

        foodItemsAdapter = FoodItemsAdapter(mContext, this@FoodAndCategoryFragment)
        foodItemsAdapter.setHasStableIds(true)
        rvItems.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.startLayoutAnimation()
        rvItems.adapter = foodItemsAdapter
    }

}