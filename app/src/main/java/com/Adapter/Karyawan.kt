package com.Adapter

import androidx.annotation.ColorRes


data class Karyawan(
    //Data tampilan utama
    val id: Int,
    val foto: Int,
    val nama: String,
    val keahlian: String,
    val periode: String,


    val jamKerja: String,
    val taskList: List<Task>,

    // Data API
    val totalTaskFromApi: Int,
    val totalSpentHoursFromApi: Int,
    val totalActualHoursFormApi: Int,
    val onTimepersentase: String,
    @ColorRes val performanceColor : Int,
    @ColorRes val peformanceTextColor: Int,

    //State kontrol UI
    var isBebanExpanded: Boolean = false
)
