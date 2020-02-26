package io.david.kotlinreactivedemo.controller

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.david.kotlinreactivedemo.model.domain.FooData
import io.david.kotlinreactivedemo.model.exceptions.NotFoundException
import io.david.kotlinreactivedemo.service.FooService
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.UncategorizedMongoDbException
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [FooController::class])
class FooControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var fooService: FooService


    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun fooService() = mockk<FooService>(relaxed = true)
    }

    private val id = "test-1111"
    private val value = "value-1111"
    private val fooData = FooData(id = id, value = value)
    private val baseUrl = "/foo"

    val objectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    @BeforeEach
    fun setUp() {
        clearMocks(fooService)
    }

    @Test
    fun `getFooData called and FooData returned from service payload returned as JSON`() {

        coEvery { fooService.getFooData(id) } returns fooData

        val result = webTestClient.get()
                .uri("$baseUrl/$id")
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java)
                .returnResult()
                .responseBody


        coVerify(exactly = 1) { fooService.getFooData(id) }

        val fd = objectMapper.readValue(result, FooData::class.java)

        assertThat(fd.id).isEqualTo(id)
        assertThat(fd.value).isEqualTo(value)

    }

    @Test
    fun `getFooData called service throws NotFoundException which is rethrown`() {

        coEvery { fooService.getFooData(id) } throws NotFoundException()

        webTestClient.get()
                .uri("$baseUrl/$id")
                .exchange()
                .expectStatus().isNotFound

    }

    @Test
    fun `getFlexiFormView called service throws unexpected exception terminal exception thrown`() {

        coEvery { fooService.getFooData(id) } throws UncategorizedMongoDbException("", Throwable())

        webTestClient.get()
                .uri("$baseUrl/$id")
                .exchange()
                .expectStatus().is5xxServerError

    }

    @Test
    fun `saveFooData called and service returns saved FooData which is returned as JSON`() {

        coEvery { fooService.saveFooData(fooData) } returns fooData

        val result = webTestClient.post()
                .uri(baseUrl)
                .body(BodyInserters.fromValue(fooData))
                .exchange()
                .expectStatus().isCreated
                .expectBody(String::class.java)
                .returnResult()
                .responseBody


        coVerify(exactly = 1) { fooService.saveFooData(fooData) }

        val fd = objectMapper.readValue(result, FooData::class.java)

        assertThat(fd.id).isEqualTo(id)
        assertThat(fd.value).isEqualTo(value)

    }

    @Test
    fun `createFlexiFormView called service throws unexpected exception terminal exception thrown`() {

        coEvery { fooService.saveFooData(fooData) } throws UncategorizedMongoDbException("", Throwable())

        webTestClient.get()
                .uri("$baseUrl/$id")
                .exchange()
                .expectStatus().is5xxServerError

    }


}
