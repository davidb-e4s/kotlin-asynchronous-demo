package io.david.kotlinreactivedemo.model.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "foo-data")
data class FooData(val id: String, val value: String)
