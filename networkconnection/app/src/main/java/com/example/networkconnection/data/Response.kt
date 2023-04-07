package com.example.networkconnection.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Person
)

@Serializable
data class Person(
    @SerializedName("name") val name: String,
    @SerializedName("note") val note: String,
    @SerializedName("age") val age: Int,
    @SerializedName("registerDate") val registerDate: String
)
