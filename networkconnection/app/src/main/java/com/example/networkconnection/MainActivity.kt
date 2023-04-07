package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.networkconnection.data.Response
import com.example.networkconnection.retrofit.PersonService
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "MainActivity"

        private const val BASE_URL = "https://umayadia-apisample.azurewebsites.net"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button_ktor).setOnClickListener(this)
        findViewById<Button>(R.id.button_retrofit).setOnClickListener(this)
    }

    /**
     * Retrofitを利用して通信を行う.
     *
     * @param name 取得する対象の名前
     * @return Response [Response]
     */
    private suspend fun getPersonWithRetrofit(name: String): Response {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val service = retrofit.create(PersonService::class.java)
        return service.loadPerson(name)
    }

    /**
     * Ktorを利用して通信を行う.
     *
     * @param name 取得する対象の名前
     * @return Response [Response]
     */
    private suspend fun getPersonWithKtor(name: String): Response = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json(kotlinx.serialization.json.Json.Default) {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )

            engine {
                connectTimeout = 60_000
                socketTimeout = 60_000
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v(TAG, "#onCreate ktor -> $message")
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse {
                Log.d(TAG, "#onCreate ktor $it")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }.get("$BASE_URL/api/persons/$name")

    override fun onClick(p0: View?) {
        val name = "Shakespeare"
        lifecycleScope.launch {
            try {
                val response = when (p0!!.id) {
                    R.id.button_retrofit -> getPersonWithRetrofit(name)
                    R.id.button_ktor -> getPersonWithKtor(name)
                    else -> return@launch
                }

                CoroutineScope(Dispatchers.Main).launch {
                    findViewById<TextView>(R.id.textview_name).text = response.data.name
                    findViewById<TextView>(R.id.textview_note).text = response.data.note
                    findViewById<TextView>(R.id.textview_age).text = response.data.age.toString()
                }
            } catch (e: Exception) {
                Log.e(TAG, "#onClick $e")
            }
        }
    }
}