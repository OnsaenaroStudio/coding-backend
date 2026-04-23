package io.github.onsaenaro.endpoint.user.dto

data class UserRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val nickname: String
)