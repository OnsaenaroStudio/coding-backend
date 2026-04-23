package io.github.onsaenaro.endpoint.auth.service

import io.github.onsaenaro.endpoint.user.dto.UserLoginDto
import io.github.onsaenaro.endpoint.user.repository.UserRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository
) {

    fun login(dto: UserLoginDto) {
        val user = findByIdentifier(dto.identifier)
        println(user)
    }

    fun findByIdentifier(identifier: String): ResultRow? {
        return if (identifier.contains("@"))
            userRepository.findByEmail(identifier)
        else
            userRepository.findByUsername(identifier)
    }
}