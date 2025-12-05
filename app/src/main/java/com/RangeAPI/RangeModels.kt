package com.RangeAPI

import com.google.gson.annotations.SerializedName


data class RangeApiResponse(
    @SerializedName("count")
    val count: Int?,

    @SerializedName("assignees")
    val assignees: List<Assignee>?
)

data class Assignee(
    @SerializedName("clickup_id")
    val clickupId: Long?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("name")
    val name: String?,


    @SerializedName("tasks")
    val tasks: List<Task>?
)


data class Task(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("description")
    val Deskripsi: String?,

    @SerializedName("status_name")
    val statusName: String?,

    @SerializedName("start_date")
    val startDate: String?,

    @SerializedName("due_date")
    val dueDate: String?,

    @SerializedName("date_done")
    val dateDone: String?,

    @SerializedName("time_estimate_hours")
    val timeEstimateHours: Int?,

    @SerializedName("time_estimate")
    val timeEstimate: Long?,

    @SerializedName("time_spent_hours")
    val timeSpentHours: Int?
)
