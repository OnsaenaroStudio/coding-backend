package io.github.onsaenaro.domain.user.service

import io.github.onsaenaro.domain.user.dto.UserRequestDto
import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.entity.UserRole
import io.github.onsaenaro.domain.user.entity.UserTable
import io.github.onsaenaro.domain.user.extension.toUserResponseDto
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(dto: UserRequestDto): UUID {
        if (findUserByUsername(dto.username) != null)
            throw IllegalArgumentException("Username already exists")

        val id = UserTable.insertAndGetId {
            it[username] = dto.username
            it[email] = dto.email
            it[password] = passwordEncoder.encode(dto.password)!!
            it[nickname] = dto.nickname
            it[role] = UserRole.USER
        }
        return id.value
    }

    @Transactional(readOnly = true)
    fun findAll(): List<UserResponseDto> =
        UserTable.selectAll().map {
            it.toUserResponseDto()
        }

    @Transactional(readOnly = true)
    fun findUserByUUID(uuid: UUID): UserResponseDto? =
        UserTable.selectAll().where{ UserTable.id eq uuid }
            .firstOrNull()
            ?.toUserResponseDto()

    @Transactional(readOnly = true)
    fun findUserByUsername(username: String): UserResponseDto? =
        UserTable.selectAll().where{ UserTable.username eq username }
            .firstOrNull()
            ?.toUserResponseDto()

}