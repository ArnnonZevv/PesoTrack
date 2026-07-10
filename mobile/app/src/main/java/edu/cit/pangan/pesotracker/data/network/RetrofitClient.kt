package edu.cit.pangan.pesotracker.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Keep this as localhost for local testing.
    // Change to your Render URL before building the final APK.
    private const val BASE_URL = "http://10.0.2.2:8080/"
    // 10.0.2.2 is how the Android emulator reaches your PC's localhost.
    // On a real device on the same WiFi, use your PC's local IP e.g. http://192.168.1.x:8080/

    private fun buildClient(token: String? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .build()
    }

    fun getService(token: String? = null): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}