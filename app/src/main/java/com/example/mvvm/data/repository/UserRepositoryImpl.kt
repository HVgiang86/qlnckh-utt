package com.example.mvvm.data.repository

import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.source.UserDataSource
import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.datacore.BaseRepository
import com.example.mvvm.domain.Researcher
import com.example.mvvm.views.auth.RegisterInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(private val remote: UserDataSource) : UserRepository, BaseRepository() {
    override suspend fun login(email: String, password: String) = runFlow {
        remote.login(email, password)
    }

    override suspend fun registerResearcher(registerInfo: RegisterInfo) = runFlow {
        println("repo call once")
        remote.registerResearcher(registerInfo)
    }

    override suspend fun registerSupervisor(registerInfo: RegisterInfo) = runFlow {
        remote.registerSupervisor(registerInfo)
    }

    override suspend fun getMyProfile() = runFlow {
        remote.getMyProfile()
    }

    override suspend fun updateProfile(email: String, request: UpdateProfileRequest) = runFlow {
        remote.updateProfile(email, request)
    }

    override suspend fun getSupervisors() = runFlow {
        remote.getAllSupervisor()
    }

    override suspend fun getResearchers() = runFlow {
        remote.getAllResearcher()
    }

    override suspend fun deleteResearcherSupervisor(mail: String) = runFlow {
        remote.deleteResearcherSupervisor(mail)
    }

    override suspend fun getProfileByEmail(email: String) = runFlow {
        remote.getProfileByEmail(email)
    }
}
