package io.github.onsaenaro.domain.user.extension

import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.entity.UserTable
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toUserResponseDto() = UserResponseDto(
    this[UserTable.id].value,
    this[UserTable.username],
    this[UserTable.email],
    this[UserTable.nickname],
    this[UserTable.role]
)