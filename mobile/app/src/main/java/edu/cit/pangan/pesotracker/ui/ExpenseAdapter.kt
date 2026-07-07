package edu.cit.pangan.pesotracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cit.pangan.pesotracker.R
import edu.cit.pangan.pesotracker.data.models.ExpenseResponse

class ExpenseAdapter(
    private val onEdit:   (ExpenseResponse) -> Unit,
    private val onDelete: (ExpenseResponse) -> Unit
) : ListAdapter<ExpenseResponse, ExpenseAdapter.ViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ExpenseResponse>() {
            override fun areItemsTheSame(a: ExpenseResponse, b: ExpenseResponse) = a.id == b.id
            override fun areContentsTheSame(a: ExpenseResponse, b: ExpenseResponse) = a == b
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvAmount:   TextView = view.findViewById(R.id.tvAmount)
        val tvDate:     TextView = view.findViewById(R.id.tvDate)
        val tvNote:     TextView = view.findViewById(R.id.tvNote)
        val btnEdit:    Button   = view.findViewById(R.id.btnEdit)
        val btnDelete:  Button   = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = getItem(position)
        holder.tvCategory.text    = expense.category
        holder.tvAmount.text      = "₱%.2f".format(expense.amount)
        holder.tvDate.text        = expense.date
        holder.tvNote.text        = expense.note ?: ""
        holder.tvNote.visibility  = if (expense.note.isNullOrBlank()) View.GONE else View.VISIBLE
        holder.btnEdit.setOnClickListener   { onEdit(expense) }
        holder.btnDelete.setOnClickListener { onDelete(expense) }
    }
}