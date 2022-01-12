package app.dealux.orangerestaurant.ui.foodfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.databinding.FoodFragmentBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val args = this.arguments

        CoroutineScope(Dispatchers.Main.immediate).launch {
            val toolbar = mView.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            val imageDescription1 = args?.get("imageDescription1").toString()
            toolbar.title = ""
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            mView.findViewById<TextView>(R.id.food_name).text = args?.get("foodName").toString()
            mView.findViewById<TextView>(R.id.serve).text = args?.get("serve").toString()
            "R$ ${
                args?.getString("foodPrice").toString()
            } ".also { mView.findViewById<TextView>(R.id.food_price).text = it }
            Glide.with(mView.context).load(imageDescription1)
                .into(mView.findViewById(R.id.imageview))
        }

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}