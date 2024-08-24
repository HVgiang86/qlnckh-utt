package com.example.mvvm.data

import com.example.mvvm.data.source.api.model.response.LoginResult
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.views.auth.RegisterInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<LoginResult>
    suspend fun registerResearcher(registerInfo: RegisterInfo): Flow<Researcher>
    suspend fun registerSupervisor(registerInfo: RegisterInfo): Flow<Supervisor>
    suspend fun getMyProfile(): Flow<ProfileResponse>
}
