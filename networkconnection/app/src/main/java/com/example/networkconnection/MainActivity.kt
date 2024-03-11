package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.networkconnection.data.Person
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
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
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
        findViewById<Button>(R.id.http_url_connection).setOnClickListener(this)
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
                    prettyPrint = true  // コードフォーマッタ
                    isLenient = true    // 引用符のない文字列、引用符のあるBooleanを許容
                    ignoreUnknownKeys = true    // data classに存在しない未定義のキーのJSONエレメントが存在していても無視する
                }
            )

            engine {
                connectTimeout = 60_000
                socketTimeout = 60_000
            }
        }

        install(Logging) {// Logging plugin
            logger = object : Logger { // カスタムログ出力
                override fun log(message: String) {
                    Log.v(TAG, "#getPersonWithKtor message -> $message")
                }
            }
            level = LogLevel.ALL    // ログの出力量（request/response, header/body）
        }

        install(ResponseObserver) { // Response plugin
            onResponse {// response
                Log.d(TAG, "#getPersonWithKtor onResponse -> $it")
            }
        }

        install(DefaultRequest) {// リクエストパラメータを指定
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }.use { it.get("$BASE_URL/api/persons/$name") }

    /**
     * HttpURLConnectionクラスを利用して通信を行う.
     *
     * @param name 取得する対象の名前
     * @return Response [Response]
     */
    private fun getPersonWithHttp(name: String): Response {
        var response: String
        runBlocking {
            response = HttpsConnectionWrapper().doHttpsRequest("$BASE_URL/api/persons/$name")
        }
        val parser = JsonParser(response)
        val data = parser.getProperty("data") as JSONObject
        val n = data.getString("name")
        val note = data.getString("note")
        val age = data.getInt("age")
        return Response(success = true, data = Person(n, note, age, ""))
    }

    override fun onClick(p0: View?) {
        val name = "Shakespeare"
        lifecycleScope.launch {
            try {
                val response = when (p0!!.id) {
                    R.id.button_retrofit -> getPersonWithRetrofit(name)
                    R.id.button_ktor -> getPersonWithKtor(name)
                    R.id.http_url_connection -> getPersonWithHttp(name)
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