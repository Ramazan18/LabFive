package com.example.ramazan.labfive

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id") var id: Int?,

    @SerializedName("name") var name: String?,

    @SerializedName("username") var username: String?,

    @SerializedName("email") var email: String?
)
