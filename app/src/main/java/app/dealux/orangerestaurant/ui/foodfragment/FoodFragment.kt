package app.dealux.orangerestaurant.ui.foodfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.itemstoadd.AddIngredientAdapter
import app.dealux.orangerestaurant.adapter.itemstoadd.MeatPointAdapter
import app.dealux.orangerestaurant.data.model.AddIngredientModel
import app.dealux.orangerestaurant.data.model.MeatPointModel
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.databinding.FoodFragmentBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException

class FoodFragment : Fragment(),
    AddIngredientAdapter.MyOnClickListener,
    MeatPointAdapter.MyOnClickListener,
    View.OnClickListener {

    private var binding: FoodFragmentBinding? = null
    private lateinit var args: Bundle

    private lateinit var navBar: BottomNavigationView

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FoodFragmentBinding.inflate(layoutInflater)

        args = this.arguments!!
        category = args.getString("category").toString()
        CoroutineScope(Dispatchers.Main).launch {
            navBar = (activity as AppCompatActivity).findViewById(R.id.bottom_nav_view)
            val toolbar = binding!!.toolbar
            val imageDescription1 = args.get("imageDescription1").toString()
            toolbar.title = ""
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navBar.visibility = View.GONE
            Glide.with(activity!!.applicationContext).load(imageDescription1)
                .into(binding!!.imageview)
            binding!!.foodName.text = args.get("foodName").toString()
            binding!!.serve.text = args.get("serve").toString()
            "R$ ${
                args.getString("foodPrice").toString()
            } ".also { binding!!.foodPrice.text = it }
            binding!!.foodDescription.text = args.get("foodDescription").toString()
        }

        when (category) {
            "Hambúgueres" -> {
                binding!!.wouldLikeAddSomeIngredient.visibility = View.VISIBLE
                binding!!.addIngredientRv.visibility = View.VISIBLE
                binding!!.howWouldLikeTheMeatPoint.visibility = View.VISIBLE
                binding!!.meatPointRv.visibility = View.VISIBLE
                loadFromRetrofit()
                setupRecyclerViews()
            }
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                "Hambúgueres" -> {
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
            AddIngredientAdapter(activity!!.applicationContext, this@FoodFragment)
        addIngredientAdapter.setHasStableIds(true)
        rvItemsToAdd.adapter = addIngredientAdapter
        rvItemsToAdd.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvItemsToAdd.setHasFixedSize(true)

        meatPointAdapter =
            MeatPointAdapter(activity!!.applicationContext, this@FoodFragment)
        meatPointAdapter.setHasStableIds(true)
        rvMeatPoint.adapter = meatPointAdapter
        rvMeatPoint.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvMeatPoint.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int, selectPosition: Int?, items: List<AddIngredientModel>) {
    }

    override fun onMeatPointCardClick(position: Int, items: List<MeatPointModel>) {
    }

}