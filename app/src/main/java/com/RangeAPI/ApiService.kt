package com.RangeAPI

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("workload/tasks-by-range")
    fun getAssigneeTasks(
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("assignees") assignees: String? = null
    ): Call<RangeApiResponse>

}
