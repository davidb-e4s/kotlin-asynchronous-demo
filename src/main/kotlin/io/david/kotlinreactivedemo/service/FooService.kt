package io.david.kotlinreactivedemo.service



import io.david.kotlinreactivedemo.model.domain.BarData
import io.david.kotlinreactivedemo.model.domain.FooData
import io.david.kotlinreactivedemo.model.exceptions.NotFoundException
import io.david.kotlinreactivedemo.model.repo.FooRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service


@Service
class FooService(private val fooRepository: FooRepository) {

    /**
     * With the new CoroutineCrudRepository Mono is no longer a thing. You will just get a Nullable object back,
     * if the object is null it means it's not been found. So here we throw a NotFoundException if this is the case
     * otherwise we return the object. When is a fantastic way to do this.
     */
    suspend fun getFooData(id: String): FooData {
        return when (val fooData = fooRepository.findById(id)) {
            null -> throw NotFoundException()
            else -> fooData
        }
    }

    /**
     * Returns a list of FooData. You can see we can simply convert a Flow to a List by calling toList(). This is much easier
     * than the Flux way of doing things
     */
    suspend fun getAllTheFoos(ids: List<String>): List<FooData> {
        return fooRepository.findAllByIdIn(ids).toList()
    }

    /**
     * Here we convert the Flow to a list and map it to a list of BarData objects
     */
    suspend fun getAllTheFoosAsBars(ids: List<String>): List<BarData> {
        return fooRepository.findAllByIdIn(ids).toList().map {
            BarData(
                    id = it.id,
                    value = it.value
            )
        }
    }

    /**
     * Here we iterate over the results using collect, inside collect we can work on the object as normal. You can start to see
     * how we're now writing asynchronous code in a 'synchronous' style
     */
    suspend fun printTheFoos(ids: List<String>) {
        fooRepository.findAllByIdIn(ids).collect {
            println(it)
        }
    }

    /**
     * Simply save the data as you would normally, no need to subscribe to the result using .subscribe to get it to actually save.
     */
    suspend fun saveFooData(fooData: FooData): FooData {
        return fooRepository.save(fooData)
    }

}
