package com.example.dna

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitEmployees {

    private const val BASE_URL = ""

    val instace: ApiServiceEmployees by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiServiceEmployees::class.java)

    }
}