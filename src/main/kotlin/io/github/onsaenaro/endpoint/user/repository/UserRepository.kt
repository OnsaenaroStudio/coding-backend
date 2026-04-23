package io.github.onsaenaro.endpoint.user.repository

import io.github.onsaenaro.endpoint.user.dto.UserRequestDto
import io.github.onsaenaro.endpoint.user.entity.UserRole
import io.github.onsaenaro.endpoint.user.entity.UserTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(readOnly = true)
class UserRepository {

    @Transactional
    fun insert(dto: UserRequestDto, encodedPassword: String): UUID {
        return UserTable.insertAndGetId {
            it[username] = dto.username
            it[email] = dto.email
            it[password] = encodedPassword
            it[nickname] = dto.nickname
            it[role] = UserRole.USER
        }.value
    }

    fun findAll(): List<ResultRow> =
        UserTable.selectAll().toList()

    fun findById(uuid: UUID): ResultRow? =
        UserTable.selectAll()
            .where { UserTable.id eq uuid }
            .firstOrNull()

    fun findByUsername(username: String): ResultRow? =
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .firstOrNull()

    fun findByEmail(email: String): ResultRow? =
        UserTable.selectAll()
            .where { UserTable.email eq email }
            .firstOrNull()
}