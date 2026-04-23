package io.github.onsaenaro.controller.user.dto

import io.github.onsaenaro.controller.user.entity.UserRole
import java.util.*

data class UserResponseDto(
    val uuid: UUID,
    val username: String,
    val email: String,
    val nickname: String,
    val role: UserRole
)