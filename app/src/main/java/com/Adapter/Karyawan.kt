package com.Adapter

import androidx.annotation.ColorRes


data class Karyawan(
    // --- Data tampilan utama ---
    val id: Int,
    val foto: Int,
    val nama: String,
    val keahlian: String,
    val periode: String,

    // --- Data kalkulasi internal & tampilan detail ---
    val jamKerja: String,
    val taskList: List<Task>,

    // --- Data dari API  ---
    val totalTaskFromApi: Int,
    val totalSpentHoursFromApi: Int,
    val totalActualHoursFormApi: Int,
    val onTimepersentase: String,
    @ColorRes val performanceColor : Int,
    @ColorRes val peformanceTextColor: Int,

    // --- State kontrol UI ---
    var isBebanExpanded: Boolean = false
)
