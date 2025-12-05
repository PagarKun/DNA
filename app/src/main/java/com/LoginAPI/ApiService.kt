package com.LoginAPI

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("clickup/sync-all") //
    suspend fun syncClickUpData(): Response<Unit>
}