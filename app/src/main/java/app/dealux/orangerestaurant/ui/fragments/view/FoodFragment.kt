package app.dealux.orangerestaurant.ui.fragments.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.itemstoadd.AddIngredientAdapter
import app.dealux.orangerestaurant.adapter.itemstoadd.MeatPointAdapter
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.data.retrofit.model.AddIngredientModel
import app.dealux.orangerestaurant.data.retrofit.model.MeatPointModel
import app.dealux.orangerestaurant.data.sqlite.OrderEntity
import app.dealux.orangerestaurant.databinding.FoodFragmentBinding
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import app.dealux.orangerestaurant.ui.fragments.viewmodel.FoodFragmentViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class FoodFragment : Fragment(),
    View.OnClickListener {

    private var binding: FoodFragmentBinding? = null
    private lateinit var args: Bundle
    private lateinit var mContext: Context
    private lateinit var navBar: BottomNavigationView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<FoodFragmentViewModel> { viewModelFactory }

    private lateinit var rvItemsToAdd: RecyclerView
    private lateinit var addIngredientAdapter: AddIngredientAdapter
    private lateinit var addIngredientResponse: Deferred<Response<List<AddIngredientModel>>>
    private lateinit var category: String

    private lateinit var rvMeatPoint: RecyclerView
    private lateinit var meatPointAdapter: MeatPointAdapter
    private lateinit var meatPointResponse: Deferred<Response<List<MeatPointModel>>>

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
        binding = FoodFragmentBinding.inflate(layoutInflater)

        setupRecyclerViews()

        args = this.requireArguments()

        val toolbar = binding!!.toolbar
        val imageDescription1 = args.get("imageDescription1").toString()
        category = args.getString("category").toString()
        navBar = (activity as AppCompatActivity).findViewById(R.id.bottom_nav_view)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navBar.visibility = View.GONE
        Glide.with(requireActivity())
            .load("http://$imageDescription1")
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding!!.imageview)
        binding!!.foodName.text = args.get("foodName").toString()
        binding!!.serve.text = args.get("serve").toString()
        "R$ ${
            args.getString("foodPrice").toString()
        } ".also { binding!!.foodPrice.text = it }
        binding!!.foodDescription.text = args.get("foodDescription").toString()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (category) {
            "Hambúrgueres" -> {
                binding!!.wouldLikeAddSomeIngredient.visibility = View.VISIBLE
                binding!!.addIngredientRv.visibility = View.VISIBLE
                binding!!.howWouldLikeTheMeatPoint.visibility = View.VISIBLE
                binding!!.meatPointRv.visibility = View.VISIBLE
                loadFromRetrofit()
            }
        }

        listener()
    }

    private fun listener() {
        binding!!.decreaseItem.setOnClickListener(this)
        binding!!.increaseItem.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        var numberOfItem = binding!!.numberOfItem.text.toString().toInt()
        when (view.id) {
            R.id.increase_item -> {
                numberOfItem += 1
                binding!!.numberOfItem.text = numberOfItem.toString()
                insertDataToDatabase()
            }
            R.id.decrease_item -> {
                if (numberOfItem == 0) {
                    binding!!.numberOfItem.text = numberOfItem.toString()
                } else {
                    numberOfItem -= 1
                    binding!!.numberOfItem.text = numberOfItem.toString()
                }
            }
        }
    }

    private fun loadFromRetrofit() {
        CoroutineScope(Dispatchers.IO).launch {
            when (category) {
                "Hambúrgueres" -> {
                    addIngredientResponse = try {
                        async { RetrofitInstance.api.getItemsToAddInHamburguer() }
                    } catch (e: IOException) {
                        Log.d("Retrofit", "You might not have internet")
                        return@launch
                    }
                    meatPointResponse = try {
                        async { RetrofitInstance.api.getMeatPoints() }
                    } catch (e: IOException) {
                        Log.d("Retrofit", "You might not have internet")
                        return@launch
                    }
                }
            }
            if (addIngredientResponse.await().isSuccessful && addIngredientResponse.await()
                    .body() != null &&
                meatPointResponse.await().isSuccessful && meatPointResponse.await()
                    .body() != null
            ) {
                Log.d("Retrofit", "data items to add successfuly load")
                addIngredientAdapter.setData(addIngredientResponse.await().body()!!)
                Log.v("values", meatPointResponse.await().body()!!.toString())
                meatPointAdapter.setData(meatPointResponse.await().body()!!)
            } else {
                Log.d("Retrofit", "Response items to add not successful")
            }
        }
    }

    private fun setupRecyclerViews() {
        rvItemsToAdd = binding!!.addIngredientRv
        rvMeatPoint = binding!!.meatPointRv

        addIngredientAdapter =
            AddIngredientAdapter(requireContext())
        addIngredientAdapter.setHasStableIds(true)
        rvItemsToAdd.adapter = addIngredientAdapter
        rvItemsToAdd.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvItemsToAdd.setHasFixedSize(true)

        meatPointAdapter =
            MeatPointAdapter(requireContext())
        meatPointAdapter.setHasStableIds(true)
        rvMeatPoint.adapter = meatPointAdapter
        rvMeatPoint.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvMeatPoint.setHasFixedSize(true)
    }

    private fun insertDataToDatabase() {
        var itemsAdded = Gson().toJson(addIngredientAdapter.itemsAdded)
        itemsAdded = if (itemsAdded == "[]") {
            ""
        } else {
            Gson().toJson(addIngredientAdapter.itemsAdded)
        }

        val meatPoint = if (meatPointAdapter.meatPoint != "") {
            meatPointAdapter.meatPoint
        } else {
            ""
        }

        val order = OrderEntity(
            0,
            args.get("foodName").toString(),
            args.getString("foodPrice").toString(),
            args.get("frontCoverUrl").toString(),
            itemsAdded,
            meatPoint
        )

        mViewModel.addOrder(order)
        Toast.makeText(requireContext(), "Item adicionado com Sucesso!", Toast.LENGTH_SHORT).show()
    }


}