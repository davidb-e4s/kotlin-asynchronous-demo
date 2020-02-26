package io.david.kotlinreactivedemo.model.repo

import io.david.kotlinreactivedemo.model.domain.FooData
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FooRepository : CoroutineCrudRepository<FooData, String> {

    suspend fun findAllByIdIn(ids: List<String>) : Flow<FooData>

}
