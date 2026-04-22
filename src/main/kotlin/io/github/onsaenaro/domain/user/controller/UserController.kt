package io.github.onsaenaro.domain.user.controller

import io.github.onsaenaro.domain.user.dto.UserRequestDto
import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun join(@RequestBody dto: UserRequestDto): ResponseEntity<UserRequestDto> {
        userService.join(dto)
        return ResponseEntity(dto, HttpStatus.CREATED)
    }

    @GetMapping
    fun users(): ResponseEntity<List<UserResponseDto>> =
        ResponseEntity.ok(userService.findAll())

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<UserResponseDto> =
        userService.findUserById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}