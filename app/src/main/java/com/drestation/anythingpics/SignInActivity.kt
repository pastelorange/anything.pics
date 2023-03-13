package com.drestation.anythingpics

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            this.onSignInResult(it)
        }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val uid = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, PinboardActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}