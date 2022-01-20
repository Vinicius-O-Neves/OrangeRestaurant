package app.dealux.orangerestaurant.utils.auth

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.utils.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AWSAuth(
    val context: Context,
    var activity: FragmentActivity,
    var hasFingerprint: Boolean,
) : AuthInterface {

    private lateinit var connectWithBiometric: ImageView

    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var anniversary: Button
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    init {
        rememberUser(context, activity) { initializeAWSAuth() }
    }

    fun initializeAWSAuth() {
        CoroutineScope(Dispatchers.Main).launch {
            val builder = AlertDialog.Builder(activity, R.style.DialogTheme)
            val itemView =
                LayoutInflater.from(activity).inflate(R.layout.layout_register_aws, null)

            // View
            builder.setView(itemView)
            val dialog = builder.create()
            dialog.show()

            firstName = itemView.findViewById<View>(R.id.first_name) as TextInputEditText
            lastName = itemView.findViewById<View>(R.id.last_name) as TextInputEditText
            anniversary = itemView.findViewById<View>(R.id.anniversary) as Button
            email = itemView.findViewById<View>(R.id.email) as TextInputEditText
            password = itemView.findViewById<View>(R.id.password) as TextInputEditText
            val btnContinue = itemView.findViewById<View>(R.id.next) as AppCompatButton
            val btnLogin = itemView.findViewById<View>(R.id.btn_login) as TextView

            anniversary.setOnClickListener {
                val datePicker = DatePickerFragment { day, month, year ->
                    onDateSelected(
                        day,
                        month + 1,
                        year
                    )
                }
                datePicker.show(activity.supportFragmentManager, "datePicker")
            }

            btnContinue.setOnClickListener {
                validate()
            }

            btnLogin.setOnClickListener {
                inflateLoginLayout(context, activity) { initializeAWSAuth() }
            }

            connectWithBiometric =
                itemView.findViewById<View>(R.id.connect_with_biometric) as AppCompatImageView

            connectWithBiometric.setOnClickListener {
                BiometricAuth(context, activity).initializeBiometricAuth()
            }
            verifyFingerprintJob()
        }
    }

    private fun validate(): String? {
        val verifyPassword = password.text.toString()
        when {
            verifyPassword.length < 8 -> {
                password.error = "A senha deve conter no minímo 8 caracteres"
                return null
            }
            !verifyPassword.matches(".*[A-Z].*".toRegex()) -> {
                password.error = "A senha deve conter 1 letra maiúscula"
                return null
            }
            !verifyPassword.matches(".*[@#\$%^&+=].*".toRegex()) -> {
                password.error = "A senha deve conter 1 caractere especial"
                return null
            }
            else -> {
                save(
                    firstName.text.toString(),
                    lastName.text.toString(),
                    email.text.toString(),
                    verifyPassword,
                    context,
                    activity
                ) { initializeAWSAuth() }
            }
        }
        return null
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        if (day <= 9 && month <= 9) {
            "0$day/0$month/$year".also { anniversary.text = it }
        } else if (day <= 9) {
            "0$day/$month/$year".also { anniversary.text = it }
        } else if (month <= 9) {
            "$day/0$month/$year".also { anniversary.text = it }
        } else {
            "$day/$month/$year".also { anniversary.text = it }
        }
    }

    private fun verifyFingerprintJob() {
        if (hasFingerprint) {
            connectWithBiometric.visibility = View.VISIBLE
        } else {
            connectWithBiometric.visibility = View.GONE
        }
    }

}