package com.example.dna

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("clickup/members")
    fun getStaff(): Call <List<MemberRequest>>
}