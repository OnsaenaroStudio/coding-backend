package io.github.onsaenaro.utils

import io.github.onsaenaro.data.ResponseForm
import org.springframework.http.ResponseEntity

fun emptyResponse(code: Int) = ResponseForm(code, null, null)

fun <T> responseGenerator(code: Int, data: T?, message: String?) = ResponseEntity.status(code).body(
    ResponseForm(code, data, message)
)