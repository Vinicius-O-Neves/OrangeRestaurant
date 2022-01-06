package app.dealux.orangerestaurant.ui.foodfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.databinding.FoodFragmentBinding
import app.dealux.orangerestaurant.ui.foodandcategoryfragment.FoodAndCategoryFragment

class FoodFragment : Fragment() {

    private var binding: FoodFragmentBinding? = null
    private lateinit var mView: View

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.food_fragment, container, false)
        binding = FoodFragmentBinding.inflate(layoutInflater)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToFoodAndCategoryFragment()

    }

    private fun backToFoodAndCategoryFragment() {
        mView.findViewById<ImageView>(R.id.arrow_back).setOnClickListener {
            val foodAndCategoryFragment = FoodAndCategoryFragment()
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, foodAndCategoryFragment)
                .disallowAddToBackStack()
                .commit()
        }
    }
}