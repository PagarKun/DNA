package com.MemberAPI

data class Todo (
    val users: List<MemberRequest>
)
data class MemberRequest(
    val id: Int?,
    val display_name: String,
    val name: String,
    val photo: String?,
    val role: String?
)