package com.example.mvvm.data.source.api

import com.example.mvvm.data.source.api.model.request.CouncilRequest
import com.example.mvvm.data.source.api.model.request.LoginRequest
import com.example.mvvm.data.source.api.model.request.ResearcherReg
import com.example.mvvm.data.source.api.model.request.SupervisorReg
import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.data.source.api.model.response.BaseResponse
import com.example.mvvm.data.source.api.model.response.LoginResponse
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.domain.GetListResearcherSupervisorResponse
import com.example.mvvm.domain.ProjectResponse
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherSupervisor
import com.example.mvvm.domain.Supervisor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyApi {
    @POST("auth/register")
    suspend fun registerResearcher(
        @Body body: ResearcherReg,
    ): Response<BaseResponse<Researcher>>

    @POST("users/create-supervisor")
    suspend fun registerSupervisor(
        @Body body: SupervisorReg,
    ): Response<BaseResponse<Supervisor>>

    @POST("auth/login")
    suspend fun login(
        @Body login: LoginRequest,
    ): Response<LoginResponse>

    @GET("users/profile")
    suspend fun getMyProfile(): Response<BaseResponse<ProfileResponse>>

    @GET("topics")
    suspend fun getAllProject(): Response<List<ProjectResponse>>

    @PUT("put/{email}")
    suspend fun updateProfile(
        @Body body: UpdateProfileRequest,
        @Path("email") email: String,
    ): Response<BaseResponse<ProfileResponse>>

    @POST("topics/set-score/{id}")
    suspend fun setScore(
        @Path("id") topicId: Long,
        @Query("score") score: Double,
    ): Any

    @GET("users/{role}")
    suspend fun getUserByRole(
        @Path("role") role: String,
    ): Response<BaseResponse<List<Supervisor>>>

    @POST("topics/council/set-project-time/{id}")
    suspend fun setProjectTime(
        @Path("id") topicId: Long,
        @Query("date") date: String,
    ): Any

    @POST("topics/council/{id}")
    suspend fun setCouncil(
        @Path("id") topicId: Long,
        @Body request: CouncilRequest,
    ): Any

    @POST("topics/set-approve/{id}")
    suspend fun approveProject(
        @Path("id") topicId: Long,
        @Query("email") email: String,
    ): Any

    @GET("users/{type}")
    suspend fun getResearchSupervisor(
        @Path("type") type: String,
    ): Map<String, List<ResearcherSupervisor>>

    @POST("users/change-status/{mail}")
    suspend fun deleteResearchSupervisor(
        @Path("mail") mail: String,
    ): Unit
}
