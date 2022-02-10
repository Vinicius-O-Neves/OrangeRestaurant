package app.dealux.orangerestaurant.ui.fragments.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.databinding.FragmentSelectTableBinding
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import app.dealux.orangerestaurant.ui.fragments.viewmodel.SelectTableFragmentViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.coroutines.*
import javax.inject.Inject


class SelectTableFragment : Fragment(){

    private var binding: FragmentSelectTableBinding? = null
    private lateinit var mContext: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<SelectTableFragmentViewModel> { viewModelFactory}

    private lateinit var codeScanner: CodeScanner

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectTableBinding.inflate(layoutInflater)

        val cameraRequestPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                scanQrCode()
            } else {
                return@registerForActivityResult
            }
        }

        cameraRequestPermission.launch(android.Manifest.permission.CAMERA)

        return binding!!.root
    }

    private fun scanQrCode() {
        val scannerView = binding!!.scannerView

        codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.decodeCallback = DecodeCallback { qrCode ->
            CoroutineScope(Dispatchers.IO).launch {
                mViewModel.saveTableNumber(qrCode.text)

                withContext(Dispatchers.Main) {
                    binding!!.txtScanner.text = qrCode.text

                    val cartFragment = CartFragment()
                    val fragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                            .hide(requireActivity().supportFragmentManager.findFragmentByTag("select_table_fragment")!!)
                            .replace(R.id.fragment, cartFragment, "cart_fragment")
                            .disallowAddToBackStack()

                    Handler(Looper.getMainLooper()).postDelayed({
                        CoroutineScope(Dispatchers.IO).cancel()
                        CoroutineScope(Dispatchers.Main).cancel()
                        fragmentTransaction.commit()
                    }, 400)
                }
            }
        }
        codeScanner.startPreview()
    }

}