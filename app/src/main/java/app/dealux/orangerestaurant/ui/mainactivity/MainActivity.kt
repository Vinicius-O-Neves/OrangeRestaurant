package app.dealux.orangerestaurant.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.dealux.orangerestaurant.adapter.category.FoodCategoryAdapter
import app.dealux.orangerestaurant.data.model.FoodCategoryModel
import app.dealux.orangerestaurant.data.retrofit.RetrofitInstance
import app.dealux.orangerestaurant.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity :
    AppCompatActivity(),
    View.OnClickListener,
    FoodCategoryAdapter.MyOnClickListener {

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

        CoroutineScope(Dispatchers.Main.immediate).launch {
            setupRecyclerView()
            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.getCategorys()
                } catch (e: IOException) {
                    Log.d("Retrofit", "You might not have internet")
                    return@launchWhenCreated
                }
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Retrofit", "data successfuly load")
                    foodCategoryAdapter.setData(response.body()!!)
                } else {
                    Log.d("Retrofit", "Response not successful")
                }
            }
        }
    }

    private suspend fun setupRecyclerView() = binding!!.rvCategorys.apply {
        foodCategoryAdapter = FoodCategoryAdapter(this@MainActivity, this@MainActivity)
        adapter = foodCategoryAdapter
        layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        this.startLayoutAnimation()
    }

    override fun onClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onCardClick(position: Int, items: List<FoodCategoryModel>) {
    }

}