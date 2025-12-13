package com.Adapter
import android.os.Parcelable
import com.DetailTaskAdapter.detailTaskModel
import com.RangeAPI.Assignee
import com.RangeAPI.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val judul: String,
    val jumlahTask: Int,
    var isExpanded: Boolean,
    var detailTaskList: List <Task>
): Parcelable
@Parcelize
data class AssigneeClass (
    val username: String,
    val email: String,
    val ass: List<Assignee>
): Parcelable
