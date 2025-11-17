package com.example.dna


import com.example.dna.TeamsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // Ganti endpoint sesuai route di API Golang kamu
    @GET("api/clickup/tasks")
    fun getTeams(): Call<TeamsResponse>
}

