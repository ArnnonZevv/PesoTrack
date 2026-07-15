package edu.cit.pangan.pesotracker.ui

import android.app.DatePickerDialog
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
import java.util.Calendar

class AddEditExpenseActivity : AppCompatActivity() {

    private var expenseId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_expense)

        val tvTitle    = findViewById<TextView>(R.id.tvTitle)
        val etAmount   = findViewById<EditText>(R.id.etAmount)
        val spinnerCat = findViewById<Spinner>(R.id.spinnerCategory)
        val etDate     = findViewById<EditText>(R.id.etDate)
        val etNote     = findViewById<EditText>(R.id.etNote)
        val btnSave    = findViewById<Button>(R.id.btnSave)
        val tvError    = findViewById<TextView>(R.id.tvError)

        // Categories
        val categories = arrayOf("Food", "Transportation", "Bills", "Shopping", "Others")
        spinnerCat.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, categories
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
            // Default to today's date
            val cal = Calendar.getInstance()
            etDate.setText("%04d-%02d-%02d".format(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            ))
        }

        // Make date field open a DatePickerDialog instead of the keyboard
        etDate.isFocusable         = false
        etDate.isFocusableInTouchMode = false
        etDate.isClickable         = true
        etDate.setOnClickListener  { showDatePicker(etDate) }

        btnSave.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            val date      = etDate.text.toString().trim()
            val note      = etNote.text.toString().trim()
            val category  = spinnerCat.selectedItem.toString()

            // Validation
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
                tvError.text = "Please pick a date."
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            tvError.visibility = View.GONE
            btnSave.isEnabled  = false

            val token = SessionManager(this).getToken()
                ?: run { finish(); return@setOnClickListener }

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
                    finish()
                } catch (e: Exception) {
                    val session = SessionManager(this@AddEditExpenseActivity)
                    if (!session.handleUnauthorized(e)) {
                        tvError.text       = "Failed to save. Check your connection."
                        tvError.visibility = View.VISIBLE
                        btnSave.isEnabled  = true
                    }
                }
            }
        }
    }

    private fun showDatePicker(etDate: EditText) {
        // Parse whatever date is already in the field, fall back to today
        val cal     = Calendar.getInstance()
        val current = etDate.text.toString()
        if (current.isNotEmpty()) {
            try {
                val parts = current.split("-")
                cal.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            } catch (e: Exception) { /* use today */ }
        }

        DatePickerDialog(
            this,
            { _, year, month, day ->
                // month is 0-indexed in Calendar, so +1 for display
                etDate.setText("%04d-%02d-%02d".format(year, month + 1, day))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}