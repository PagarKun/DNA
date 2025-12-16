package com.MemberAPI

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("clickup/members")
    fun getTodo(): Call<Todo>
}