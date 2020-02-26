package io.david.kotlinreactivedemo.controller

import io.david.kotlinreactivedemo.model.domain.FooData
import io.david.kotlinreactivedemo.model.exceptions.NotFoundException
import io.david.kotlinreactivedemo.model.exceptions.TerminalException
import io.david.kotlinreactivedemo.service.FooService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/foo")
class FooController(private val fooService: FooService) {

    /**
     * Gets FooData. Notice the suspend keyword here, this tells the compiler this method can be suspended when it's not doing anything
     * @param: [id] - The ID of the FooData to get
     * @return: [FooData] - Returns the FooData
     * @throws [NotFoundException] - The FooData could not be found
     * @throws [Exception] - An unexpected error has occurred
     */
    @GetMapping("/{id}")
    suspend fun getFooData(@PathVariable("id") id: String): FooData {

        try {
            return fooService.getFooData(id)
        } catch (e: Exception) {
            if (e is NotFoundException) throw e
            // Obviously you'd have your proper HTTP error handling here. NotFoundException actually throws a 404, Terminal returns a 500 using Springs @ResponseStatus annotation
            throw TerminalException()
        }

    }


    /**
     * Saves some FooData
     * @param: [fooData] - The FooData object to save
     * @return: [FooData] - The saved FooData
     * @throws [Exception] - Oh no! There was an error saving FooData
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun saveFooData(@RequestBody fooData: FooData): FooData {

        try {
            return fooService.saveFooData(fooData)
        } catch (e: Exception) {
            throw TerminalException()
        }

    }
}
