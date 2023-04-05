package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networkconnection.retrofit.BASE_URL
import com.example.networkconnection.retrofit.PersonService
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).build()

    private val service = retrofit.create(PersonService::class.java)

    private val loadPerson = service.loadPerson("Shakespeare")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            kotlin.runCatching {
                loadPerson.clone().execute()
            }.onSuccess { response: Response<ResponseBody> ->
                if (response.isSuccessful) {
                    Log.d(TAG, "#onCreate\$onSuccess success")

                    response.body()?.string()?.let {
                        Log.d(TAG, it)
                    }
                } else {
                    Log.d(TAG, "#onCreate\$onSuccess failure")
                }
            }.onFailure {
                Log.d(TAG, "#onCreate\$onFailure failure")
            }
        }
    }
}