package com.Adapter
import com.DetailTaskAdapter.detailTaskModel

data class Task(
    val judul: String,
    val jumlahTask: Int,
    var isExpanded: Boolean,
    var detailTaskList: List <detailTaskModel>
)