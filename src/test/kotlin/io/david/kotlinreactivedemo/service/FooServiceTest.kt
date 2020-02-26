package io.david.kotlinreactivedemo.service

import io.david.kotlinreactivedemo.model.domain.FooData
import io.david.kotlinreactivedemo.model.exceptions.NotFoundException
import io.david.kotlinreactivedemo.model.repo.FooRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.toFlux

@ExtendWith(MockKExtension::class)
class FooServiceTest {

    @InjectMockKs
    private lateinit var fooService: FooService

    @MockK
    private lateinit var fooRepository: FooRepository

    private val id = "test-1111"
    private val id2 = "test-2222"
    private val value = "value-1111"
    private val value2 = "value-2222"
    private val idList = listOf(id)
    private val fooData = FooData(id = id, value = value)
    private val fooData2 = FooData(id = id2, value = value2)

    private val fooFlow = flowOf(fooData, fooData2)

    @Test
    fun `getFooData returned valid object, returned to invoker`() {
        coEvery { fooRepository.findById(id) } returns fooData

        runBlocking {
            val result = fooService.getFooData(id)

            coVerify(exactly = 1) { fooRepository.findById(id) }

            assertThat(result.id).isEqualTo(id)
            assertThat(result.value).isEqualTo(value)
        }
    }

    @Test
    fun `getFooData returned null, NotFoundException thrown`() {
        coEvery { fooRepository.findById(id) } returns null

        assertThatThrownBy {
            runBlocking {
                fooService.getFooData(id)
            }
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `getAllTheFoos returned flow of FooData, returned to invoker as list`() {

        coEvery { fooRepository.findAllByIdIn(idList) } returns fooFlow

        runBlocking {
            val result = fooService.getAllTheFoos(idList)

            coVerify(exactly = 1) { fooRepository.findAllByIdIn(idList) }

            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].id).isEqualTo(id)
            assertThat(result[0].value).isEqualTo(value)
        }
    }

    @Test
    fun `getFooData returned empty flow, empty list returned`() {
        coEvery { fooRepository.findAllByIdIn(idList) } returns emptyFlow<FooData>()

            runBlocking {
                val result = fooService.getAllTheFoos(idList)

                assertThat(result).isEmpty()
            }

    }


    @Test
    fun `getAllTheFoosAsBars returned flow of FooData, returned to invoker as list of BarData`() {

        coEvery { fooRepository.findAllByIdIn(idList) } returns fooFlow

        runBlocking {
            val result = fooService.getAllTheFoosAsBars(idList)

            coVerify(exactly = 1) { fooRepository.findAllByIdIn(idList) }

            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].id).isEqualTo(id)
            assertThat(result[0].value).isEqualTo(value)
        }
    }

    @Test
    fun `getAllTheFoosAsBars returned empty flow, empty list returned`() {
        coEvery { fooRepository.findAllByIdIn(idList) } returns emptyFlow<FooData>()

        runBlocking {
            val result = fooService.getAllTheFoosAsBars(idList)

            assertThat(result).isEmpty()
        }

    }

    // Look at console output to see result
    @Test
    fun `printTheFoos returned flow of FooData, objects printed to console`() {

        coEvery { fooRepository.findAllByIdIn(idList) } returns fooFlow

        runBlocking {
            fooService.printTheFoos(idList)

            coVerify(exactly = 1) { fooRepository.findAllByIdIn(idList) }
        }
    }
}
