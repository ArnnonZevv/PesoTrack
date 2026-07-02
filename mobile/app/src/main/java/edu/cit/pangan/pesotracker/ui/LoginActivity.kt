package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.pangan.pesotracker.data.LoginRequest
import edu.cit.pangan.pesotracker.data.SessionManager
import edu.cit.pangan.pesotracker.databinding.ActivityLoginBinding
import edu.cit.pangan.pesotracker.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Already logged in from a previous session? Skip straight to dashboard.
        if (sessionManager.isLoggedIn()) {
            goToDashboard()
            return
        }

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter your username and password.")
            return
        }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(username, password))
                setLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    sessionManager.saveSession(response.body()!!)
                    goToDashboard()
                } else if (response.code() == 401) {
                    showError("Invalid username or password.")
                } else {
                    showError("Login failed (server said: ${response.code()}).")
                }
            } catch (e: IOException) {
                setLoading(false)
                showError("Can't reach the server. Is the backend running and BASE_URL correct?")
            } catch (e: Exception) {
                setLoading(false)
                showError("Something went wrong: ${e.message}")
            }
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnLogin.isEnabled = !loading
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = android.view.View.VISIBLE
    }
}
