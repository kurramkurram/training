package com.example.networkconnection.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://umayadia-apisample.azurewebsites.net"

interface PersonService {

    @GET("api/persons/{name}")
    suspend fun loadPerson(
        @Path("name") name: String,
    ): Response
}

data class Person(val name: String, val note: String, val age: Int, val registerDate: String)

data class Response(val success: Boolean, val data: Person)