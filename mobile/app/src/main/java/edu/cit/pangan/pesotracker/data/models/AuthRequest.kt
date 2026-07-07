package edu.cit.pangan.pesotrack.data.models

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val fullname: String, val email: String, val username: String, val password: String)
data class AuthResponse(val token: String, val userId: Long, val username: String, val fullname: String, val role: String)