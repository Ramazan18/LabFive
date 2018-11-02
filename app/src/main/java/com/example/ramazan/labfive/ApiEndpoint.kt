package com.example.ramazan.labfive

import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {

    @GET("users/")
    fun getUsers(): Call<List<User>>

    @GET("posts/")
    fun getPosts(): Call<List<Post>>

    @FormUrlEncoded
    @POST("posts/")
    fun createPost(@Field("title") title: String,
                   @Field("body") body: String,
                   @Field("userId") userId: Int): Call<Post>

    @POST("posts/")
    fun createPost(@Body post: Post): Call<Post>
}