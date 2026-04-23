package io.github.onsaenaro.endpoint.auth.controller

import io.github.onsaenaro.endpoint.auth.service.AuthService
import io.github.onsaenaro.endpoint.user.dto.UserLoginDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody dto: UserLoginDto): ResponseEntity<*> {
        return authService.login(dto)
    }
}