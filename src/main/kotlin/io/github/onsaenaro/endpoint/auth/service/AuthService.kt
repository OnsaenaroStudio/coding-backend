package io.github.onsaenaro.endpoint.auth.service

import io.github.onsaenaro.endpoint.user.dto.UserLoginDto
import io.github.onsaenaro.endpoint.user.entity.UserTable
import io.github.onsaenaro.endpoint.user.repository.UserRepository
import io.github.onsaenaro.util.isValidEmail
import io.github.onsaenaro.util.responseGenerator
import org.jetbrains.exposed.v1.core.ResultRow
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(dto: UserLoginDto): ResponseEntity<*> {
        val user = findByIdentifier(dto.identifier)
            ?: return responseGenerator(404, null, "User not found")

        val passwordMatches = passwordEncoder.matches(dto.password, user[UserTable.password])
        return if (passwordMatches) {
            responseGenerator(200, dto.identifier, "Login successful")
        } else {
            responseGenerator(401, null, "Invalid password")
        }
    }

    fun findByIdentifier(identifier: String): ResultRow? {
        return if (isValidEmail(identifier))
            userRepository.findByEmail(identifier)
        else
            userRepository.findByUsername(identifier)
    }
}