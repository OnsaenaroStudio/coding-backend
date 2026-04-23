package io.github.onsaenaro.endpoint.user.dto

data class UserLoginDto(
    val identifier: String,
    val password: String
) {
}