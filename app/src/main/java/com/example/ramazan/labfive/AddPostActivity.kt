package com.example.ramazan.labfive

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

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

        btn_save.setOnClickListener{
            val post_title = edx_title.text.toString()
            val post_body = edx_body.text.toString()

            val post = Post(1, title = post_title, body = post_body)

            val call = apiEndpoint.createPost(post)

            call.enqueue(object : Callback<Post> {

                override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                    val post = response?.body()
                    Log.d("Post: ", post.toString())
                }

                override fun onFailure(call: Call<Post>?, t: Throwable?) {
                    Log.e("Error: ", t?.message)
                }

            })


            val result = Intent()
            result.putExtra("post_title", post_title)
            result.putExtra("post_body", post_body)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}
