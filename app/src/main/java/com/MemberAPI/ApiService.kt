package com.MemberAPI

import com.MemberAPI.MemberRequest
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("clickup/members")
    fun getTodo(): Call<Todo>
}