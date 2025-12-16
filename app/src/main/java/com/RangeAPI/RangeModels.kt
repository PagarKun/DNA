package com.RangeAPI

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class RangeApiResponse(
    @SerializedName("count")
    val count: Int?,

    @SerializedName("assignees")
    val assignees: List<Assignee>?
)

@Parcelize
data class Assignee(
    @SerializedName("clickup_id")
    val clickupId: Int?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("role")
    val role: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("total_spent_hours")
    val totalSpentHours: Int?,

    @SerializedName("expected_hours")
    val expectedHours: Int?,

    @SerializedName("actual_work_hours")
    val actualWorkHours: Int?,

    @SerializedName("on_time_completion_percentage")
    val onTimePercentage: Float?,

    @SerializedName("total_tasks")
    val totalTask: Int?,

    @SerializedName("tasks")
    val tasks: List<Task>?
): Parcelable

@Parcelize
data class Task(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("description")
    val deskripsi: String?,

    @SerializedName("priority")
    val priority: String?,

    @SerializedName("project_name")
    val projectName: String?,

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
    val timeSpentHours: Int?,

    @SerializedName("time_efficiency_percentage")
    val timeEfficiencyPercentage: Float?,

    @SerializedName("remaining_time")
    val remainingTime: String?,

    @SerializedName("background_color")
    val backgroundColor: Int

): Parcelable
