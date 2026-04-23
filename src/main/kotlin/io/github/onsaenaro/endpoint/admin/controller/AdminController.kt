package io.github.onsaenaro.endpoint.admin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {

    @GetMapping("/admin")
    fun admin(): String = "admin"
}