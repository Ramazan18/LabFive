package com.example.ramazan.labfive

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val postsList = ArrayList<Post>()

    val adapter = ListAdapter(postsList)

    var userId = 0

    private val ADD_TASK_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        userId = intent.getIntExtra("user_id", 1)
        Toast.makeText(this, userId.toString(), Toast.LENGTH_SHORT).show()

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

        val call = apiEndpoint.getPosts()

        call.enqueue(object : Callback<List<Post>> {

            override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {
                val posts = response?.body()
                postsList.addAll(posts!!.filter { it.userId == userId })
                Log.d("Filtered ", postsList.toString())
                recycler.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                recycler.adapter = adapter

            }

            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
                Log.e("Error: ", t?.message)
            }

        })

        fltBtn.setOnClickListener{
            val intent = Intent(this, AddPostActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TASK_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {
                val p_title = data?.getStringExtra("post_title")
                val p_body = data?.getStringExtra("post_body")
                postsList.add(Post(userId, null, p_title, p_body))
                adapter.notifyDataSetChanged()
            }
        }
    }
}
