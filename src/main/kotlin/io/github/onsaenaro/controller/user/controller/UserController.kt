package io.github.onsaenaro.controller.user.controller

import io.github.onsaenaro.controller.user.dto.UserRequestDto
import io.github.onsaenaro.controller.user.dto.UserResponseDto
import io.github.onsaenaro.controller.user.service.UserService
import io.github.onsaenaro.data.ResponseForm
import io.github.onsaenaro.utils.responseGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/list")
    fun users() = userService.findAll()

    @PostMapping("/signup")
    fun signUp(@RequestBody dto: UserRequestDto): ResponseEntity<ResponseForm<UUID>> {
        val uuid = runCatching {
            userService.createUser(dto)
        }.getOrElse {
            return responseGenerator(500, null, "USER_CREATE_FAILED")
        }

        return responseGenerator(201, uuid, "USER_CREATED")
    }

    @GetMapping("/search")
    fun users(
        @RequestParam(required = false) uuid: UUID?,
        @RequestParam(required = false) name: String?
    ): ResponseEntity<ResponseForm<List<UserResponseDto>>> {

        uuid?.let {
            val user = userService.findUserByUUID(it) ?: return responseGenerator(
                404, null, "User Not Found"
            )

            return responseGenerator(200, listOf(user), null)
        }

        name?.let {
            val user = userService.findUserByUsername(it) ?: return responseGenerator(
                404, null, "User Not Found"
            )

            return responseGenerator(200, listOf(user), null)
        }

        return responseGenerator(200, userService.findAll(), null)
    }
}