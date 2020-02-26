package io.david.kotlinreactivedemo.model.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The item you requested could not be found")
class NotFoundException : RuntimeException() {
}
