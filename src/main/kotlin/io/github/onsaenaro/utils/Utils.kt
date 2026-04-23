package io.github.onsaenaro.utils

import io.github.onsaenaro.data.ResponseForm

fun emptyResponse(code: Int) = ResponseForm(code, null, null)

fun <T> responseGenerator(code: Int, data: T?, message: String?) = org.springframework.http.ResponseEntity.status(code).body(
    ResponseForm(code, data, message)
)