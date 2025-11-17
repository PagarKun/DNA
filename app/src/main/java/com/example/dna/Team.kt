package com.example.dna

data class Team(
    val id: String,
    val name: String,
    val color: String,
    val avatar: String?,
    val member: List<Member>
)
