package edu.cit.pangan.pesotracker.data.models

// Auth
data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(
    val fullname: String,
    val email: String,
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val fullname: String,
    val role: String
)

// Expense
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