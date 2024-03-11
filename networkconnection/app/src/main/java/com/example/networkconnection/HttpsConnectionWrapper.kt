package com.example.networkconnection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

class HttpsConnectionWrapper {
    suspend fun doHttpsRequest(uri: String, header: Map<String, String>? = null): String {
        return withContext(Dispatchers.IO) {
            val url = URL(uri)
            val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            try {
                connection.requestMethod = "GET"
                header?.forEach { (k, v) ->
                    connection.setRequestProperty(k, v)
                }

                val input = connection.inputStream
                readInputStream(input)
            } finally {
                connection.disconnect()
            }
        }
    }

    private fun readInputStream(input: InputStream): String {
        val builder = StringBuffer()
        try {
            InputStreamReader(input, StandardCharsets.UTF_8).use { it ->
                BufferedReader(it).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        builder.append(line)
                    }
                }
            }
        } catch (e: IOException) {
            println("exception = $e")
        }
        return builder.toString()
    }
}