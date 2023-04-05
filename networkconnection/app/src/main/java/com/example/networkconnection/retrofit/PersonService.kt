package com.example.networkconnection.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

val BASE_URL = "https://umayadia-apisample.azurewebsites.net"

interface PersonService {

    @GET("api/persons/{name}")
    fun loadPerson(
        @Path("name") name: String,
    ): Call<ResponseBody>
}

data class Person(val name: String, val note: String, val age: Int, val registerDate: String)