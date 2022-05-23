package com.umc.clone_flo.data.datasource.api

import com.umc.clone_flo.data.entity.User
import com.umc.clone_flo.data.entity.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

}