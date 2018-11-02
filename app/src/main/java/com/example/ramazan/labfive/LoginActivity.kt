package com.example.ramazan.labfive

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    val usersList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gson = GsonBuilder().create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build()

        val apiEndpoint = retrofit.create(ApiEndpoint::class.java)

        val call = apiEndpoint.getUsers()

        call.enqueue(object : Callback<List<User>> {

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                val users = response?.body()
                usersList.addAll(users!!)
                Log.d("Users2: ", usersList.toString())
            }

            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.e("Error: ", t?.message)
            }

        })

        btn_login.setOnClickListener {
            val username = edx_username.text.toString()
            var userId: Int? = 0
            for (user in usersList) {
                if (user.username == username)
                    userId = user.id
            }

            if (userId != 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user_id", userId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
