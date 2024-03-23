package com.example.ginnex.models

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val contactInfo: String,
    val profilePic: String?,
    val role: String
)

