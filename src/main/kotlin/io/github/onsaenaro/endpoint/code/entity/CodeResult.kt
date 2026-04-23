package io.github.onsaenaro.endpoint.code.entity

data class CodeResult(
    val status: Result,
    val output: String,
    val stderr: String,
    val executionTimeMs: Long
)

enum class Result {
    SUCCESS,
    TIMEOUT,
    COMPILE_ERROR,
    RUNTIME_ERROR
}