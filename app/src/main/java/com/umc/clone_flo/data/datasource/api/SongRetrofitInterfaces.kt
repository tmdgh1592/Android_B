package com.umc.clone_flo.data.datasource.api

import com.umc.clone_flo.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterfaces {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}