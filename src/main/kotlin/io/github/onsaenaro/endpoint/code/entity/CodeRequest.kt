package io.github.onsaenaro.endpoint.code.entity

data class CodeRequest(
    val language: Language,
    val code: String,
    val input: String,
)

enum class Language(val exp: String, val image: String) {
    KOTLIN("kt", "zenika/kotlin"),
    PYTHON("py", "python:3.12-slim"),
    JAVA("java", "openjdk:21-slim"),
    CPP("cpp", "gcc:latest")
}