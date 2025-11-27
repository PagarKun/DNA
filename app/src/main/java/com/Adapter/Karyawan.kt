package com.Adapter

data class Karyawan(
    val foto: Int,
    val nama: String,
    val keahlian: String,
    val departemen: String = "Teknologi Informasi",
    val jamKerja: String,
    val jumlahTask: Int,
    val periode: String,
    val taskList: List<Task>,
    var isExpanded: Boolean = false,
    var isBebanExpanded: Boolean = false,
)

