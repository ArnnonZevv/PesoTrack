package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.cit.pangan.pesotracker.data.SessionManager
import edu.cit.pangan.pesotracker.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Guard: shouldn't normally land here without a session, but just in case.
        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        binding.tvWelcome.text = "Welcome, ${sessionManager.getFullname() ?: sessionManager.getUsername()}!"
        binding.tvSubtitle.text =
            "You're logged in as ${sessionManager.getUsername()} (${sessionManager.getRole()})."

        binding.btnLogout.setOnClickListener {
            sessionManager.clear()
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
