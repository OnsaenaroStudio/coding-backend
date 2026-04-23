package io.github.onsaenaro.controller.user.extension

import io.github.onsaenaro.controller.user.dto.UserResponseDto
import io.github.onsaenaro.controller.user.entity.UserTable
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toUserResponseDto() = UserResponseDto(
    this[UserTable.id].value,
    this[UserTable.username],
    this[UserTable.email],
    this[UserTable.nickname],
    this[UserTable.role]
)