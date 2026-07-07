package edu.cit.pangan.pesotracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cit.pangan.pesotracker.R
import edu.cit.pangan.pesotracker.data.models.ExpenseResponse
import edu.cit.pangan.pesotracker.data.network.RetrofitClient
import edu.cit.pangan.pesotracker.utils.SessionManager
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var adapter: ExpenseAdapter
    private lateinit var tvTotal: TextView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)

        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish(); return
        }

        setContentView(R.layout.activity_dashboard)

        val tvWelcome  = findViewById<TextView>(R.id.tvWelcome)
        val rvExpenses = findViewById<RecyclerView>(R.id.rvExpenses)
        val fabAdd     = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)
        val btnLogout  = findViewById<Button>(R.id.btnLogout)
        tvTotal        = findViewById(R.id.tvTotal)
        tvEmpty        = findViewById(R.id.tvEmpty)

        tvWelcome.text = "Hello, ${session.getFullname()}"

        adapter = ExpenseAdapter(
            onEdit = { expense -> openAddEdit(expense) },
            onDelete = { expense -> confirmDelete(expense) }
        )

        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter       = adapter

        fabAdd.setOnClickListener { openAddEdit(null) }

        btnLogout.setOnClickListener {
            session.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadExpenses()
    }

    // Reload whenever returning from AddEditExpenseActivity
    override fun onResume() {
        super.onResume()
        loadExpenses()
    }

    private fun loadExpenses() {
        val token = session.getToken() ?: return
        lifecycleScope.launch {
            try {
                val expenses = RetrofitClient.getService(token).getExpenses()
                adapter.submitList(expenses)
                updateTotal(expenses)
                tvEmpty.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Toast.makeText(
                    this@DashboardActivity,
                    "Failed to load expenses.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateTotal(expenses: List<ExpenseResponse>) {
        val total = expenses.sumOf { it.amount }
        tvTotal.text = "Total: ₱%.2f".format(total)
    }

    private fun openAddEdit(expense: ExpenseResponse?) {
        val intent = Intent(this, AddEditExpenseActivity::class.java).apply {
            if (expense != null) {
                putExtra("expense_id",       expense.id)
                putExtra("expense_amount",   expense.amount)
                putExtra("expense_category", expense.category)
                putExtra("expense_date",     expense.date)
                putExtra("expense_note",     expense.note ?: "")
            }
        }
        startActivity(intent)
    }

    private fun confirmDelete(expense: ExpenseResponse) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Delete ₱%.2f – %s?".format(expense.amount, expense.category))
            .setPositiveButton("Delete") { _, _ -> deleteExpense(expense.id) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteExpense(id: Long) {
        val token = session.getToken() ?: return
        lifecycleScope.launch {
            try {
                RetrofitClient.getService(token).deleteExpense(id)
                Toast.makeText(this@DashboardActivity, "Deleted.", Toast.LENGTH_SHORT).show()
                loadExpenses()
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Failed to delete.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}