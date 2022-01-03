package app.dealux.orangerestaurant.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.adapter.FoodCategoryAdapter
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var foodCategoryAdapter: FoodCategoryAdapter

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupRecyclerView()
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getCategorys()
            } catch (e: IOException) {
                Log.e("Retrofit", "You might not have internet")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                foodCategoryAdapter.items = response.body()!!
            } else {
                Log.e("Retrofit", "Response not successful")
            }
        }
    }

    private fun setupRecyclerView() = binding!!.rvCategorys.apply {
        foodCategoryAdapter = FoodCategoryAdapter()
        adapter = foodCategoryAdapter
        layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
    }
}