package io.github.onsaenaro.domain.user.entity

import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable

object UserTable : UUIDTable("user") {
    val username = varchar("username", 30).uniqueIndex()
    val email = varchar("email", 30).uniqueIndex()
    val password = varchar("password", 255)
    val nickname = varchar("nickname", 30)
    val role = enumerationByName<UserRole>("role", 10)
}