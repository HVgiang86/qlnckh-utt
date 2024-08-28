package com.example.mvvm.data.source

import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.LoginRequest
import com.example.mvvm.data.source.api.model.request.ResearcherReg
import com.example.mvvm.data.source.api.model.request.SupervisorReg
import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.data.source.api.model.response.LoginResponse
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.datacore.BaseDataSource
import com.example.mvvm.datacore.DataResult
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.views.auth.RegisterInfo
import javax.inject.Inject

interface UserDataSource {
    suspend fun login(email: String, password: String): DataResult<LoginResponse>
    suspend fun registerResearcher(data: RegisterInfo): DataResult<Researcher>
    suspend fun registerSupervisor(data: RegisterInfo): DataResult<Supervisor>
    suspend fun getMyProfile(): DataResult<ProfileResponse>
    suspend fun updateProfile(email: String, request: UpdateProfileRequest): DataResult<ProfileResponse>

    suspend fun getAllSupervisor(): DataResult<List<Supervisor>>
}

class UserDataSourceImpl
    @Inject
    constructor(private val myApi: MyApi) : UserDataSource, BaseDataSource() {
        override suspend fun login(email: String, password: String) = returnResult {
            myApi.login(LoginRequest(email, password))
        }

        override suspend fun registerResearcher(data: RegisterInfo) = returnResult {
            val body = ResearcherReg(
                data.name,
                data.email,
                data.password,
                data.birthday,
                data.major,
                data.className,
            )
            myApi.registerResearcher(body)
        }

        override suspend fun registerSupervisor(data: RegisterInfo) = returnResult {
            val body = SupervisorReg(
                data.name,
                data.email,
                data.password,
                data.birthday,
                data.faculty,
                data.department,
                data.title,
            )
            myApi.registerSupervisor(body)
        }

        override suspend fun getMyProfile() = returnResult {
            myApi.getMyProfile()
        }

        override suspend fun updateProfile(email: String, request: UpdateProfileRequest) = returnResult {
            myApi.updateProfile(request, email)
        }

        override suspend fun getAllSupervisor() = returnResult {
            myApi.getAllSupervisor()
        }
    }
