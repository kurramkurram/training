package com.example.networkconnection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.networkconnection.retrofit.BASE_URL
import com.example.networkconnection.retrofit.PersonService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val service = retrofit.create(PersonService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            try {
                val response = service.loadPerson("Shakespeare")
                Log.d(TAG, "#onCreate $response")
                CoroutineScope(Dispatchers.Main).launch {
                    findViewById<TextView>(R.id.textview).text = response.data.name
                }
            } catch (e: Exception) {
                Log.e(TAG, "#onCreate\$lifecycleScope $e")
            }
        }
    }
}