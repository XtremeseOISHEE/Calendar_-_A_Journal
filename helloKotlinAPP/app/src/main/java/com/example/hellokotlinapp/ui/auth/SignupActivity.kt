package com.example.hellokotlinapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hellokotlinapp.MainActivity
import com.example.hellokotlinapp.data.remote.AuthRepository
import com.example.hellokotlinapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var b: ActivitySignupBinding
    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Correct ViewModel initialization (passing auth + db)
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

        // Button click
        b.btnSignup.setOnClickListener {
            val fullName = b.etFullName.text.toString().trim()
            val username = b.etUsername.text.toString().trim()
            val email = b.etEmail.text.toString().trim()
            val birthDate = b.etBirthdate.text.toString().trim()
            val password = b.etPassword.text.toString().trim()

            // Validation
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() ||
                birthDate.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call signup
            lifecycleScope.launch {
                vm.signup(fullName, username, email, birthDate, password) { ok, err ->
                    runOnUiThread {
                        if (ok) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Signed up successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignupActivity,
                                err ?: "Signup failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
