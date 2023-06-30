package com.company0ne.demoretrofit.Api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET("photos")
    fun getData(): Call<JsonArray>
}
