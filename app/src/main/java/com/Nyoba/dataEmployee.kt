package com.Nyoba

data class Employee(
    val id: Int,
    val name: String,
    val position: String,
    val tasks: List<Task>,
    var isExpanded: Boolean = false
)