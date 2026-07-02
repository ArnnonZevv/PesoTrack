package edu.cit.pangan.pesotracker.network

import edu.cit.pangan.pesotracker.data.AuthResponse
import edu.cit.pangan.pesotracker.data.LoginRequest
import edu.cit.pangan.pesotracker.data.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
