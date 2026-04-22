package io.github.onsaenaro.domain.user.dto

import io.github.onsaenaro.domain.user.entity.UserRole

data class UserResponseDto(
    val id: Long,
    val username: String,
    val role: UserRole
) {
}