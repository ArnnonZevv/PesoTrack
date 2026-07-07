package edu.cit.pangan.pesotrack.data.models

data class ExpenseRequest(
    val amount: Double,
    val category: String,
    val date: String,
    val note: String?
)

data class ExpenseResponse(
    val id: Long,
    val amount: Double,
    val category: String,
    val date: String,
    val note: String?
)