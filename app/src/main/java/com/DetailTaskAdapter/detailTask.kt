package com.DetailTaskAdapter

data class detailTaskModel(
    val judul: String,
    val judul2: String,
    val jam : String,
    val tanggal: String,
    val project: String,
    val progress: String,
    val level : String,
    var isExpanded: Boolean

)
