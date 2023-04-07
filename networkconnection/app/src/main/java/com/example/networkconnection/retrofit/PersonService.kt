package com.example.networkconnection.retrofit

import com.example.networkconnection.data.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonService {

    @GET("api/persons/{name}")
    suspend fun loadPerson(
        @Path("name") name: String,
    ): Response
}