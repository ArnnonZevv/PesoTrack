package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.pangan.pesotracker.R
import edu.cit.pangan.pesotracker.data.models.LoginRequest
import edu.cit.pangan.pesotracker.data.network.RetrofitClient
import edu.cit.pangan.pesotracker.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)

        val etUsername  = findViewById<EditText>(R.id.etUsername)
        val etPassword  = findViewById<EditText>(R.id.etPassword)
        val btnLogin    = findViewById<Button>(R.id.btnLogin)
        val tvError     = findViewById<TextView>(R.id.tvError)
        val tvRegister  = findViewById<TextView>(R.id.tvGoToRegister)

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            // Client-side validation
            if (username.isEmpty() || password.isEmpty()) {
                tvError.text = "Username and password are required."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            btnLogin.isEnabled  = false
            tvError.visibility  = View.GONE

            lifecycleScope.launch {
                try {
                    val res = RetrofitClient.getService()
                        .login(LoginRequest(username, password))

                    session.saveSession(res.token, res.username, res.fullname)

                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    tvError.text       = "Invalid username or password."
                    tvError.visibility = View.VISIBLE
                    btnLogin.isEnabled = true
                }
            }
        }
    }
}