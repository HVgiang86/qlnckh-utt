package com.example.mvvm.data

import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.data.source.api.model.response.LoginResponse
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.views.auth.RegisterInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<LoginResponse>
    suspend fun registerResearcher(registerInfo: RegisterInfo): Flow<Researcher>
    suspend fun registerSupervisor(registerInfo: RegisterInfo): Flow<Supervisor>
    suspend fun getMyProfile(): Flow<ProfileResponse>
    suspend fun updateProfile(email: String, request: UpdateProfileRequest): Flow<ProfileResponse>
}
