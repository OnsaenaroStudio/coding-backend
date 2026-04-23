package io.github.onsaenaro.controller.user.dto

data class UserRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val nickname: String
)