package io.github.onsaenaro.domain.user.entity

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object UserTable : LongIdTable("user") {
    val username = varchar("username", 10).uniqueIndex()
    val password = varchar("password", 255)
    val role = enumerationByName<UserRole>("role", 10)
}