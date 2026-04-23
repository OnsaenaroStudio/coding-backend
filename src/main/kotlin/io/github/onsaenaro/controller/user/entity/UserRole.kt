package io.github.onsaenaro.controller.user.entity

enum class UserRole(val permission: Int = 1) {
    ADMIN(10),
    USER(3),
}