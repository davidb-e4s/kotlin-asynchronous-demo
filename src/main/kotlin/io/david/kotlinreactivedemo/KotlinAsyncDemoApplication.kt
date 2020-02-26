package io.david.kotlinreactivedemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinAsyncDemoApplication

fun main(args: Array<String>) {
	runApplication<KotlinAsyncDemoApplication>(*args)
}
