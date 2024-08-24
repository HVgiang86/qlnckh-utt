package com.example.mvvm.data.source

import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.LoginRequest
import com.example.mvvm.data.source.api.model.request.ResearcherReg
import com.example.mvvm.data.source.api.model.request.SupervisorReg
import com.example.mvvm.data.source.api.model.response.LoginResult
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.datacore.BaseDataSource
import com.example.mvvm.datacore.DataResult
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import com.example.mvvm.views.auth.RegisterInfo
import javax.inject.Inject

interface UserDataSource {
    suspend fun login(email: String, password: String): DataResult<LoginResult>
    suspend fun registerResearcher(data: RegisterInfo): DataResult<Researcher>
    suspend fun registerSupervisor(data: RegisterInfo): DataResult<Supervisor>
    suspend fun getMyProfile(): DataResult<ProfileResponse>
}

class UserDataSourceImpl
    @Inject
    constructor(private val myApi: MyApi) : UserDataSource, BaseDataSource() {
        override suspend fun login(email: String, password: String) = returnResult {
            myApi.login(LoginRequest(email, password)).result
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

        override suspend fun getMyProfile(): DataResult<ProfileResponse> {
            return returnResult {
                myApi.getMyProfile().profile
            }
        }
    }
