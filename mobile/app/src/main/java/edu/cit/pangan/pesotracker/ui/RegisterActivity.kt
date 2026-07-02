package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.pangan.pesotracker.data.RegisterRequest
import edu.cit.pangan.pesotracker.data.SessionManager
import edu.cit.pangan.pesotracker.databinding.ActivityRegisterBinding
import edu.cit.pangan.pesotracker.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvGoLogin.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val fullname = binding.etFullname.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (fullname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are required.")
            return
        }
        if (password.length < 8) {
            showError("Password must be at least 8 characters.")
            return
        }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.register(
                    RegisterRequest(fullname, email, username, password)
                )
                setLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    sessionManager.saveSession(response.body()!!)
                    startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                    finish()
                } else if (response.code() == 409) {
                    showError("That username is already taken.")
                } else {
                    showError("Registration failed (server said: ${response.code()}).")
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

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnRegister.isEnabled = !loading
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = android.view.View.VISIBLE
    }
}
