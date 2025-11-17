package com.example.dna


data class TeamsResponse(
    val projects: List<Project>?
)

data class Project(
    val id: Int?,
    val name: String?,
    val description: String?,
    val created_at: String?,
    val updated_at: String?,
    val tasks: List<Task>?
)

data class Task(
    val id: Int?,
    val name: String?,
    val status: String?
)