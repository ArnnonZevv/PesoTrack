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

    // Full list kept in memory — filter never calls the API again
    private var allExpenses: List<ExpenseResponse> = emptyList()

    private lateinit var tvTotal:     TextView
    private lateinit var tvEmpty:     TextView
    private lateinit var tvBreakdown: TextView
    private lateinit var spinnerFilter: Spinner

    private val categories = arrayOf("All", "Food", "Transportation", "Bills", "Shopping", "Others")
    private var currentFilter = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(this)

        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish(); return
        }

        setContentView(R.layout.activity_dashboard)

        val tvWelcome    = findViewById<TextView>(R.id.tvWelcome)
        val rvExpenses   = findViewById<RecyclerView>(R.id.rvExpenses)
        val fabAdd       = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)
        val btnLogout    = findViewById<Button>(R.id.btnLogout)
        tvTotal          = findViewById(R.id.tvTotal)
        tvEmpty          = findViewById(R.id.tvEmpty)
        tvBreakdown      = findViewById(R.id.tvBreakdown)
        spinnerFilter    = findViewById(R.id.spinnerFilter)

        tvWelcome.text = "Hello, ${session.getFullname()}"

        // Filter spinner
        spinnerFilter.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, categories
        )
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                currentFilter = categories[pos]
                applyFilter()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        adapter = ExpenseAdapter(
            onEdit   = { expense -> openAddEdit(expense) },
            onDelete = { expense -> confirmDelete(expense) }
        )

        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter       = adapter

        fabAdd.setOnClickListener    { openAddEdit(null) }
        btnLogout.setOnClickListener {
            session.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadExpenses()
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
    }

    private fun loadExpenses() {
        val token = session.getToken() ?: return
        lifecycleScope.launch {
            try {
                allExpenses = RetrofitClient.getService(token).getExpenses()
                applyFilter()
                updateSummary()
            } catch (e: Exception) {
                if (!session.handleUnauthorized(e)) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Failed to load expenses.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun applyFilter() {
        val filtered = if (currentFilter == "All") allExpenses
                       else allExpenses.filter { it.category == currentFilter }

        adapter.submitList(filtered)
        tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateSummary() {
        // Grand total
        val grand = allExpenses.sumOf { it.amount }
        tvTotal.text = "Total: ₱%.2f".format(grand)

        // Per-category breakdown
        if (allExpenses.isEmpty()) {
            tvBreakdown.visibility = View.GONE
            return
        }

        val map = mutableMapOf<String, Double>()
        allExpenses.forEach { map[it.category] = (map[it.category] ?: 0.0) + it.amount }

        val lines = map.entries
            .sortedByDescending { it.value }
            .joinToString("   ") { (cat, total) -> "$cat: ₱%.2f".format(total) }

        tvBreakdown.text       = lines
        tvBreakdown.visibility = View.VISIBLE
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
                if (!session.handleUnauthorized(e)) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Failed to delete.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}