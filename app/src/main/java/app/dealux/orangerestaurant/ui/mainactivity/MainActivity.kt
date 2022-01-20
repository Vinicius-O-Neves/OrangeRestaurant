package app.dealux.orangerestaurant.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.dealux.orangerestaurant.OrangeRestaurant
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.databinding.ActivityMainBinding
import app.dealux.orangerestaurant.ui.ProfileFragment
import app.dealux.orangerestaurant.ui.di.MainComponent
import app.dealux.orangerestaurant.ui.foodandcategoryfragment.FoodAndCategoryFragment

class MainActivity :
    AppCompatActivity(),
    View.OnClickListener {

    private var binding: ActivityMainBinding? = null

    lateinit var mainComponent: MainComponent

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as OrangeRestaurant).appComponent.mainComponent().create()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val foodAndCategoryFragment = FoodAndCategoryFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(foodAndCategoryFragment, "food_and_category_fragment")

        binding!!.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu -> setCurrentFragment(foodAndCategoryFragment, "food_and_category_fragment")
                R.id.profile -> setCurrentFragment(profileFragment, "profile_fragment")
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment, tag)
            commit()
        }

    override fun onClick(view: View) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding!!.bottomNavView.visibility = View.VISIBLE
    }
}