package com.example.mvvm.data.repository

import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.source.UserDataSource
import com.example.mvvm.datacore.BaseRepository
import com.example.mvvm.views.auth.RegisterInfo
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(private val remote: UserDataSource) : UserRepository, BaseRepository() {
        override suspend fun login(email: String, password: String) = runFlow {
            remote.login(email, password)
        }

        override suspend fun registerResearcher(registerInfo: RegisterInfo) = runFlow {
            remote.registerResearcher(registerInfo)
        }

        override suspend fun registerSupervisor(registerInfo: RegisterInfo) = runFlow {
            remote.registerSupervisor(registerInfo)
        }

        override suspend fun getMyProfile() = runFlow {
            remote.getMyProfile().map { it.profile }
        }
    }
