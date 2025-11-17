package com.example.dna

data class TaskResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val created_at: String,
    val updated_at: String
)
