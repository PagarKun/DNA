package com.DetailTaskAdapter

import android.os.Parcelable
import com.bumptech.glide.Priority
import kotlinx.parcelize.Parcelize

@Parcelize
data class detailTaskModel(
    val judul: String,
    val desc: String,
    val priority: String,
    val jam : String,
    val tanggal: String,
    val project: String,
    val progress: String,
    val level : String,
    var isExpanded: Boolean

): Parcelable