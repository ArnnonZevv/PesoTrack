package edu.cit.pangan.pesotracker.data.network

import edu.cit.pangan.pesotracker.data.models.*
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/expenses")
    suspend fun getExpenses(): List<ExpenseResponse>

    @POST("api/expenses")
    suspend fun addExpense(@Body request: ExpenseRequest): ExpenseResponse

    @PUT("api/expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: Long,
        @Body request: ExpenseRequest
    ): ExpenseResponse

    @DELETE("api/expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: Long)
}