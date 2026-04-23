package io.github.onsaenaro.user.service

import io.github.onsaenaro.user.dto.UserRequestDto
import io.github.onsaenaro.user.entity.UserRole
import io.github.onsaenaro.user.entity.UserTable
import io.github.onsaenaro.user.extension.toUserResponseDto
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserService(
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createUser(dto: UserRequestDto): UUID {
        if (findUserByUsername(dto.username) != null)
            throw IllegalArgumentException("Username already exists")

        return UserTable.insertAndGetId {
            it[username] = dto.username
            it[email] = dto.email
            it[password] = passwordEncoder.encode(dto.password)!! // 절대 null로 받을 수 없음.
            it[nickname] = dto.nickname
            it[role] = UserRole.USER
        }.value
    }

    fun findAll() = UserTable.selectAll().map {
        it.toUserResponseDto()
    }

    fun findUserByUUID(uuid: UUID) = UserTable.selectAll().where{
        UserTable.id eq uuid
    }.firstOrNull()?.toUserResponseDto()

    fun findUserByUsername(username: String) = UserTable.selectAll().where{
        UserTable.username eq username
    }.firstOrNull()?.toUserResponseDto()

}