package app.dealux.orangerestaurant.utils.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import app.dealux.orangerestaurant.R
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

interface AuthInterface {

    fun configureAmplify(activity: FragmentActivity) {
        try {
            Amplify.addPlugin(AWSApiPlugin()) // UNCOMMENT this line once backend is deployed
            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(activity.applicationContext)
            Log.i("Amplify", "Initialized Amplify")
        } catch (e: AmplifyException) {
            Log.e("Amplify", "Could not initialize Amplify", e)
        }
    }

}

inline fun rememberUser(
    context: Context,
    activity: FragmentActivity,
    crossinline nextAction: () -> Unit
) {
    Amplify.Auth.rememberDevice({
        Log.i("AuthQuickStart", "Remember device succeeded")
        context.startActivity(Intent(context, MainActivity::class.java))
        activity.finish()
    },
        {
            Log.e("AuthQuickStart", "Remember device failed with error", it)
            inflateLoginLayout(context, activity, nextAction)
        })
}

inline fun inflateLoginLayout(
    context: Context,
    activity: FragmentActivity,
    crossinline nextAction: () -> Unit
) {
    Amplify.Auth.signOut(
        { Log.i("AuthQuickstart", "Signed out successfully") },
        { Log.e("AuthQuickstart", "Sign out failed", it) }
    )
    CoroutineScope(Dispatchers.Main).launch {
        val builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.layout_login, null)

        // View
        builder.setView(itemView)
        val dialog = builder.create()
        dialog.show()

        val email = itemView.findViewById<View>(R.id.email) as TextInputEditText
        val password = itemView.findViewById<View>(R.id.password) as TextInputEditText
        val btnContinue = itemView.findViewById<View>(R.id.next) as AppCompatButton
        val btnRegister = itemView.findViewById<View>(R.id.btn_register) as TextView

        btnContinue.setOnClickListener {
            Amplify.Auth.signIn(email.text.toString(), password.text.toString(),
                { result ->
                    if (result.isSignInComplete) {
                        Log.i("AuthQuickstart", "Sign in succeeded")
                        dialog.dismiss()
                        context.startActivity(Intent(context, MainActivity::class.java))
                        CoroutineScope(Dispatchers.IO).cancel()
                        activity.finish()
                    }
                },
                {
                    Log.e("AuthQuickstart", "Failed to sign in", it)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            activity.applicationContext,
                            "Verifique as informações inseridas",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }

        btnRegister.setOnClickListener {
            nextAction()
        }
    }
}

inline fun inflateConfirmLayout(
    email: String,
    context: Context,
    activity: FragmentActivity,
    crossinline nextAction: () -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        val builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        val itemView =
            LayoutInflater.from(activity).inflate(R.layout.layout_confirm_user, null)

        // View
        builder.setView(itemView)
        val dialog = builder.create()
        dialog.show()

        val verificationCode =
            itemView.findViewById<View>(R.id.confirm_user) as TextInputEditText
        val verificationTextInputEditText =
            itemView.findViewById<View>(R.id.ed_confirm_user) as TextInputLayout
        val btnConfirm = itemView.findViewById<View>(R.id.btn_confirm) as AppCompatButton

        btnConfirm.setOnClickListener {
            Amplify.Auth.confirmSignUp(
                email, verificationCode.text.toString(),
                { result ->
                    if (result.isSignUpComplete) {
                        Log.i("AuthQuickstart", "Confirm signUp succeeded")
                        inflateLoginLayout(context, activity, nextAction)
                    } else {
                        Log.i("AuthQuickstart", "Confirm sign up not complete")
                    }
                },
                {
                    Log.e("AuthQuickstart", "Failed to confirm sign up", it)
                }
            )
        }

        verificationTextInputEditText.setEndIconOnClickListener {
            verificationTextInputEditText.helperText = "Código enviado ao email"
        }
    }
}

inline fun save(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    context: Context,
    activity: FragmentActivity,
    crossinline nextAction: () -> Unit
 ) {
     val options = AuthSignUpOptions.builder()
         .userAttribute(AuthUserAttributeKey.email(), email)
         .build()
     Amplify.Auth.signUp(
         email, password, options,
         {
             Log.i("AuthQuickStart", "Sign up succeeded: $it")
             inflateConfirmLayout(email, context, activity) {
                 inflateLoginLayout(
                     context,
                     activity,
                     nextAction
                 )
             }
         },
         {
             Log.e("AuthQuickStart", "Sign up failed", it)
             Handler(Looper.getMainLooper()).post {
                 Toast.makeText(
                     activity.applicationContext,
                     "Falha ao se registrar! Tente novamente mais tarde",
                     Toast.LENGTH_LONG
                 ).show()
             }
         },
     )
 }