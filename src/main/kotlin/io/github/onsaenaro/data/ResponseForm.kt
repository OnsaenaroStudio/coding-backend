package io.github.onsaenaro.data

data class ResponseForm<T>(
    val code: Int,
    val data: T?,
    val message: String?
)
