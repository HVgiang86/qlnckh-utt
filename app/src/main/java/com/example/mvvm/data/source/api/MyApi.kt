package com.example.mvvm.data.source.api

import com.example.mvvm.data.source.api.model.request.LoginRequest
import com.example.mvvm.data.source.api.model.request.ResearcherReg
import com.example.mvvm.data.source.api.model.request.SupervisorReg
import com.example.mvvm.data.source.api.model.response.GetProfileResponse
import com.example.mvvm.data.source.api.model.response.LoginResponse
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {
    @POST("auth/register")
    suspend fun registerResearcher(
        @Body body: ResearcherReg,
    ): Researcher

    @POST("users/create-supervisor")
    suspend fun registerSupervisor(
        @Body body: SupervisorReg,
    ): Supervisor

    @POST("auth/login")
    suspend fun login(
        @Body login: LoginRequest,
    ): Response<LoginResponse>

    @GET("users/profile")
    suspend fun getMyProfile(): Response<GetProfileResponse>
}
