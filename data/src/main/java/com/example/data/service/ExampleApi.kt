package com.example.data.service

import retrofit2.Response
import retrofit2.http.GET

interface ExampleApi {
    @GET("api/app_versions?flatform=android")
    suspend fun getAvailableVersion(): Response<String>
}
