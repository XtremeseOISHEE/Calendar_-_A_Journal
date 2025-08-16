package com.example.hellokotlinapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.hellokotlinapp.MainActivity2
import com.example.hellokotlinapp.data.remote.AuthRepository
import com.example.hellokotlinapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
/*import android.util.Log*/

class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding
    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Proper ViewModel initialization
        vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(
                    AuthRepository(
                        FirebaseAuth.getInstance(),
                        FirebaseFirestore.getInstance()
                    )
                ) as T
            }
        })[AuthViewModel::class.java]

        // Login button
        b.btnLogin.setOnClickListener {
            val emailOrUsername = b.etEmailOrUsername.text.toString()
            val pass = b.etPassword.text.toString()

            MainScope().launch {
                vm.login(emailOrUsername, pass) { ok, err ->
                    Toast.makeText(
                        this@LoginActivity,
                        if (ok) "Logged in!" else err ?: "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (ok) {
                        /*Log.d("LoginActivity", "Login successful, launching MainActivity2")*/
                        startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                        finish()
                    }
                }
            }
        }

        // Navigate to Signup
        b.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
