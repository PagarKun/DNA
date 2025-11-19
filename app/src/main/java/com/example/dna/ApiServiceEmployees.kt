package com.example.dna

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceEmployees {

    @GET("items")
    fun getItemsEmployees(): Call<List<dnaEmployees>>
}