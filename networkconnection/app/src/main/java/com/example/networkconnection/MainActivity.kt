package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.networkconnection.retrofit.BASE_URL
import com.example.networkconnection.retrofit.PersonService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

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

        CoroutineScope(Dispatchers.IO).launch {
            val response = loadPerson.clone().execute()

            CoroutineScope(Dispatchers.Main).launch {
                if (response.isSuccessful) {
                    findViewById<TextView>(R.id.textview).text = response.body()?.string() ?: ""
                }
            }
        }
    }
}