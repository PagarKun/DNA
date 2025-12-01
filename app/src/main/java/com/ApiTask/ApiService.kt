package com.ApiTask

import com.MemberAPI.Todo
import retrofit2.Call
import retrofit2.http.GET

class ApiService {
    interface ApiService {

        @GET("clickup/tasks")
        fun getTask(): Call<List<TaskModul>>
    }
}