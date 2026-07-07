package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.pangan.pesotracker.R
import edu.cit.pangan.pesotracker.data.models.RegisterRequest
import edu.cit.pangan.pesotracker.data.network.RetrofitClient
import edu.cit.pangan.pesotracker.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFullname  = findViewById<EditText>(R.id.etFullname)
        val etEmail     = findViewById<EditText>(R.id.etEmail)
        val etUsername  = findViewById<EditText>(R.id.etUsername)
        val etPassword  = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvError     = findViewById<TextView>(R.id.tvError)
        val tvLogin     = findViewById<TextView>(R.id.tvGoToLogin)

        tvLogin.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            val fullname = etFullname.text.toString().trim()
            val email    = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            // Client-side validation
            if (fullname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                tvError.text       = "All fields are required."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (password.length < 6) {
                tvError.text       = "Password must be at least 6 characters."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            tvError.visibility    = View.GONE

            lifecycleScope.launch {
                try {
                    val res = RetrofitClient.getService()
                        .register(RegisterRequest(fullname, email, username, password))

                    SessionManager(this@RegisterActivity)
                        .saveSession(res.token, res.username, res.fullname)

                    startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    tvError.text          = "Username already taken or invalid data."
                    tvError.visibility    = View.VISIBLE
                    btnRegister.isEnabled = true
                }
            }
        }
    }
}