package com.android.healthgate.model

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

data class ApiResponse(
    val success: Boolean = false,
    val message: String? = null,
    val data: Any? = null
)

data class ErrorResponse(val message: String? = null)
