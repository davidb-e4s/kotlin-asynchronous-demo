package io.david.kotlinreactivedemo.model.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An unexpected error occurred when processing your request")
class TerminalException(val reason: String? = null) : RuntimeException(reason)
