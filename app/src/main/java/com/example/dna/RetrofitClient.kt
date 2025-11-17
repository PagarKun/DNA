package com.example.dna

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ⚠️ Ganti ini sesuai kondisi kamu:
    // Kalau pakai emulator → pakai "http://10.0.2.2:8080/"
    // Kalau pakai HP fisik → pakai IP laptop kamu, misal "http://192.168.0.108:8080/"
    private const val BASE_URL = "http://192.168.0.108:8080/"

    val instance: ApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(ApiService::class.java)
    }
}