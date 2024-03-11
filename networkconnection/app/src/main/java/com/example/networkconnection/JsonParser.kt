package com.example.networkconnection

import org.json.JSONObject

class JsonParser(private val jsonString: String) {

    val jsonObject = parse()

    fun parse(): JSONObject = JSONObject(jsonString)

    fun getProperty(key: String): Any = jsonObject.get(key)
}