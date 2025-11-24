package com.MemberAPI

data class Todo (
    val users: List<MemberRequest>
)
data class MemberRequest(
    val id: Int?,
    val username: String,
    val name: String,
    val photo: String?
)