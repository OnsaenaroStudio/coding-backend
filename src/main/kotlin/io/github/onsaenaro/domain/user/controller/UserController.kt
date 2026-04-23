package io.github.onsaenaro.domain.user.controller

import io.github.onsaenaro.data.ResponseForm
import io.github.onsaenaro.domain.user.dto.UserRequestDto
import io.github.onsaenaro.domain.user.dto.UserResponseDto
import io.github.onsaenaro.domain.user.service.UserService
import io.github.onsaenaro.util.responseGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody dto: UserRequestDto): ResponseEntity<ResponseForm<UUID>> {
        return try {
            val uuid = userService.createUser(dto)
            responseGenerator(201, uuid, "USER_CREATED")
        } catch (e: IllegalArgumentException) {
            responseGenerator(400, null, e.message)
        } catch (_: Exception) {
            responseGenerator(500, null, "USER_CREATE_FAILED")
        }
    }

    @GetMapping
    fun users(
        @RequestParam(required = false) uuid: UUID?,
        @RequestParam(required = false) username: String?
    ): ResponseEntity<ResponseForm<List<UserResponseDto>>> {
        val result = when {
            uuid != null -> userService.findUserByUUID(uuid)?.let { listOf(it) }
            username != null -> userService.findUserByUsername(username)?.let { listOf(it) }
            else -> userService.findAll()
        } ?: return responseGenerator(404, null, "User Not Found")

        return responseGenerator(200, result, null)
    }
}