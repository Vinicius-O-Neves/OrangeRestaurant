package app.dealux.orangerestaurant.ui.splashscreen

import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.dealux.orangerestaurant.OrangeRestaurant
import app.dealux.orangerestaurant.databinding.SplashScreenBinding
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.ui.splashscreen.di.MainComponent
import app.dealux.orangerestaurant.utils.auth.AWSAuth
import app.dealux.orangerestaurant.utils.auth.BiometricAuth
import javax.inject.Inject

class SplashScreen : AppCompatActivity(), View.OnClickListener {

    private var binding: SplashScreenBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mViewModel by viewModels<SplashScreenViewModel> { viewModelFactory }
    private lateinit var appComponent: MainComponent

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent =
            (applicationContext as OrangeRestaurant).appComponent.mainComponent().create()
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        listener()
    }

    private fun listener() {
        binding!!.btnLetsStart.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_lets_start -> lifecycleScope.launchWhenStarted { startAuth() }
        }
    }

    private fun startAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT < 28) {
                val keyGuardService: KeyguardManager = applicationContext.getSystemService(
                    KEYGUARD_SERVICE
                ) as KeyguardManager
                val packageManager: PackageManager = applicationContext.packageManager
                if (!packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                    AWSAuth(
                        this@SplashScreen,
                        this@SplashScreen,
                        hasFingerprint = false
                    )
                } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                    AWSAuth(
                        this@SplashScreen,
                        this@SplashScreen,
                        hasFingerprint = true
                    )
                }
                if (!keyGuardService.isKeyguardSecure) {
                    AWSAuth(
                        this@SplashScreen,
                        this@SplashScreen,
                        hasFingerprint = false
                    )
                }
            } else {
                    mViewModel.readFromDataStore.observe(this, { preference ->
                    when (preference) {
                        true ->
                            BiometricAuth(
                                this@SplashScreen,
                                this@SplashScreen
                            ).initializeBiometricAuth()
                        false ->
                            AWSAuth(
                                this@SplashScreen,
                                this@SplashScreen,
                                hasFingerprint = true
                            )
                        else -> {
                            val biometricManager = BiometricManager.from(this)
                            val dialog = AlertDialog.Builder(this@SplashScreen)
                                .setTitle("Entrar com biometria?")
                                .setMessage("Você gostaria de entrar utilizando biometria?")
                                .setPositiveButton("Sim") { _, _ ->
                                    lifecycleScope.launchWhenStarted {
                                        mViewModel.saveToDataStore(true)
                                    }
                                    when (preference) {
                                        true ->
                                            when (biometricManager.canAuthenticate(
                                                BiometricManager.Authenticators.BIOMETRIC_WEAK or DEVICE_CREDENTIAL
                                            )) {
                                                BiometricManager.BIOMETRIC_SUCCESS ->
                                                    BiometricAuth(
                                                        this@SplashScreen,
                                                        this@SplashScreen
                                                    ).initializeBiometricAuth()
                                                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                                                    AWSAuth(
                                                        this@SplashScreen,
                                                        this@SplashScreen,
                                                        hasFingerprint = false
                                                    )
                                                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                                                    AWSAuth(
                                                        this@SplashScreen,
                                                        this@SplashScreen,
                                                        hasFingerprint = false
                                                    )
                                                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED ->
                                                    AWSAuth(
                                                        this@SplashScreen,
                                                        this@SplashScreen,
                                                        hasFingerprint = false
                                                    )
                                                else -> {
                                                    AWSAuth(
                                                        this@SplashScreen,
                                                        this@SplashScreen,
                                                        hasFingerprint = false
                                                    )
                                                }
                                            }
                                        false ->
                                            AWSAuth(
                                                this@SplashScreen,
                                                this@SplashScreen,
                                                hasFingerprint = false
                                            )
                                    }
                                }
                                .setNegativeButton("Não") { _, _ ->
                                    lifecycleScope.launchWhenStarted {
                                        mViewModel.saveToDataStore(false)
                                    }
                                    AWSAuth(
                                        this@SplashScreen,
                                        this@SplashScreen,
                                        hasFingerprint = true
                                    )
                                }.create()
                            dialog.show()
                        }
                    }
                })
            }
        }
    }

}