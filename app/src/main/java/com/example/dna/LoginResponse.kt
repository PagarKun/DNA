package com.example.dna

data class LoginResponse(
    val api_message: String,
    val data: TokenData,
)

data class TokenData(
    val token: String
)