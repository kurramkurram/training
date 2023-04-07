package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.networkconnection.retrofit.BASE_URL
import com.example.networkconnection.retrofit.Person
import com.example.networkconnection.retrofit.Response
import com.google.gson.annotations.SerializedName
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
import kotlinx.serialization.Serializable

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope.launch {
//            try {
//                val retrofit = Retrofit.Builder()
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl(BASE_URL)
//                    .build()
//
//                val service = retrofit.create(PersonService::class.java)
//                val response = service.loadPerson("Shakespeare")
//                Log.d(TAG, "#onCreate $response")
//                CoroutineScope(Dispatchers.Main).launch {
//                    findViewById<TextView>(R.id.textview).text = response.data.name
//                }
//            } catch (e: Exception) {
//                Log.e(TAG, "#onCreate\$lifecycleScope $e")
//            }
//        }

        lifecycleScope.launch {
            val httpClient = HttpClient(Android) {
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
                            Log.v(TAG, "#onCreate message -> $message")
                        }
                    }
                    level = LogLevel.ALL
                }

                install(ResponseObserver) {
                    onResponse {
                        Log.d(TAG, "#onCreate $it")
                    }
                }

                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
            val response = httpClient.get<Response>("$BASE_URL/api/persons/Shakespeare")
            Log.d(TAG, "#onCreate ${response.success}")

            CoroutineScope(Dispatchers.Main).launch {
                findViewById<TextView>(R.id.textview).text = response.data.note
            }
        }
    }
}