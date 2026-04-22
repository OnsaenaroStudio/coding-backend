package io.github.onsaenaro.domain.user.controller

import io.github.onsaenaro.domain.user.dto.UserRequestDto
import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun singUp(@RequestBody dto: UserRequestDto): UUID =
        userService.createUser(dto)

    @GetMapping
    fun users(): List<UserResponseDto> =
        userService.findAll()

    @GetMapping("/{username}")
    fun findById(@PathVariable username: String): UserResponseDto? =
        userService.findUserByUsername(username)

    @GetMapping("/uuid/{uuid}")
    fun findByUUID(@PathVariable uuid: UUID): UserResponseDto? =
        userService.findUserByUUID(uuid)
}