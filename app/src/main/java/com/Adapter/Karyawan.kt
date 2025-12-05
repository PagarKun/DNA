package com.Adapter

// Buka file data class Karyawan Anda dan ganti isinya dengan ini
data class Karyawan(
    val foto: Int,
    val nama: String,
    val keahlian: String, // Ini kita isi dengan email dari API
    val jamKerja: String,
    val jumlahTask: Int,
    val periode: String,
    val taskList: List<Task>,
    var isExpanded: Boolean = false,
    var isBebanExpanded: Boolean = false // <-- TAMBAHKAN INI
)


