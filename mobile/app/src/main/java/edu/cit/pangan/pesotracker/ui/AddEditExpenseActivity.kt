package edu.cit.pangan.pesotracker.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.pangan.pesotracker.R
import edu.cit.pangan.pesotracker.data.models.ExpenseRequest
import edu.cit.pangan.pesotracker.data.network.RetrofitClient
import edu.cit.pangan.pesotracker.utils.SessionManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddEditExpenseActivity : AppCompatActivity() {

    private var expenseId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_expense)

        val tvTitle     = findViewById<TextView>(R.id.tvTitle)
        val etAmount    = findViewById<EditText>(R.id.etAmount)
        val spinnerCat  = findViewById<Spinner>(R.id.spinnerCategory)
        val etDate      = findViewById<EditText>(R.id.etDate)
        val etNote      = findViewById<EditText>(R.id.etNote)
        val btnSave     = findViewById<Button>(R.id.btnSave)
        val tvError     = findViewById<TextView>(R.id.tvError)

        val categories  = arrayOf("Food", "Transportation", "Bills", "Shopping", "Others")
        spinnerCat.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        // Pre-fill if editing
        expenseId = intent.getLongExtra("expense_id", -1L)
        if (expenseId != -1L) {
            tvTitle.text = "Edit Expense"
            etAmount.setText(intent.getDoubleExtra("expense_amount", 0.0).toString())
            etDate.setText(intent.getStringExtra("expense_date") ?: "")
            etNote.setText(intent.getStringExtra("expense_note") ?: "")
            val cat = intent.getStringExtra("expense_category") ?: "Food"
            spinnerCat.setSelection(categories.indexOf(cat).coerceAtLeast(0))
        } else {
            tvTitle.text = "Add Expense"
            etDate.setText(LocalDate.now().toString())
        }

        btnSave.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val date      = etDate.text.toString().trim()
            val note      = etNote.text.toString().trim()
            val category  = spinnerCat.selectedItem.toString()

            // Input validation
            if (amountStr.isEmpty()) {
                tvError.text = "Amount is required."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                tvError.text = "Amount must be a positive number."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                tvError.text = "Date is required."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE
            btnSave.isEnabled  = false

            val token = SessionManager(this).getToken() ?: run { finish(); return@setOnClickListener }
            val request = ExpenseRequest(amount, category, date, note.ifBlank { null })

            lifecycleScope.launch {
                try {
                    if (expenseId != -1L) {
                        RetrofitClient.getService(token).updateExpense(expenseId, request)
                        Toast.makeText(
                            this@AddEditExpenseActivity,
                            "Expense updated.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        RetrofitClient.getService(token).addExpense(request)
                        Toast.makeText(
                            this@AddEditExpenseActivity,
                            "Expense added.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish() // Returns to DashboardActivity, which reloads in onResume
                } catch (e: Exception) {
                    tvError.text       = "Failed to save. Check your connection."
                    tvError.visibility = View.VISIBLE
                    btnSave.isEnabled  = true
                }
            }
        }
    }
}