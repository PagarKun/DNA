package com.Adapter


data class Karyawan(
    val foto: Int,
    val nama: String,
    val keahlian: String,
    val jamKerja: String,
    val jumlahTask: Int,
    val periode: String,
    val taskList: List<Task>,
    var isExpanded: Boolean = false,

    val totalTaskFromApi: Int,
    val totalSpentHoursFromApi: Int,
    val totalActualHoursFormApi: Int,
    var isBebanExpanded: Boolean = false


)


