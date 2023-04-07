package com.example.networkconnection.retrofit

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://umayadia-apisample.azurewebsites.net"

interface PersonService {

    @GET("api/persons/{name}")
    suspend fun loadPerson(
        @Path("name") name: String,
    ): Response
}

@Serializable
data class Person(
    @SerializedName("name") val name: String,
    @SerializedName("note") val note: String,
    @SerializedName("age") val age: Int,
    @SerializedName("registerDate") val registerDate: String
)

@Serializable
data class Response(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Person
)