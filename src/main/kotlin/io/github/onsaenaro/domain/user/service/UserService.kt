package io.github.onsaenaro.domain.user.service

import io.github.onsaenaro.domain.user.dto.UserRequestDto
import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.entity.UserRole
import io.github.onsaenaro.domain.user.entity.UserTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val passwordEncoder: PasswordEncoder
) {

    fun join(dto: UserRequestDto): Long {
        val id = UserTable.insertAndGetId {
            it[username] = dto.username
            it[password] = passwordEncoder.encode(dto.password)!!
            it[role] = UserRole.USER
        }
        return id.value
    }

    fun findAll(): List<UserResponseDto> =
        UserTable.selectAll().map {
            UserResponseDto(
                it[UserTable.id].value,
                it[UserTable.username],
                it[UserTable.role]
            )
        }

    fun findUserById(id: Long): UserResponseDto? =
        UserTable.select( UserTable.id eq id ).firstOrNull()?.let {
            UserResponseDto(
                it[UserTable.id].value,
                it[UserTable.username],
                it[UserTable.role]
            )
        }

}