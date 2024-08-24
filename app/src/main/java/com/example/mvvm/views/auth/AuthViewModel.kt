package com.example.mvvm.views.auth

import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.UserRepository
import com.example.mvvm.datacore.token.TokenRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(private val userRepository: UserRepository, private val tokenRepository: TokenRepository) : BaseViewModel() {
        private val _userRole = MutableLiveData<UserRole?>(null)
        private val _loginSuccess = MutableLiveData<Boolean>(false)

        val userRole: MutableLiveData<UserRole?> = _userRole
        val loginSuccess: MutableLiveData<Boolean> = _loginSuccess

        fun signIn(userName: String, password: String) {
            runFlow(Dispatchers.IO) {
                userRepository.login(userName, password).collect {
                    Timber.d("collect $it")
                    tokenRepository.saveToken(it.token)
                    _loginSuccess.postValue(true)
                }
            }
        }

        fun getProfile() {
            runFlow(Dispatchers.IO) {
                userRepository.getMyProfile().collect {
                    Timber.d("Get my profile collect $it")

                    if (it.isResearcher()) {
                        _userRole.postValue(UserRole.RESEARCHER)
                        val user = it.getResearcher()
                        AppState.userId = user.id
                    } else if (it.isSupervisor()) {
                        _userRole.postValue(UserRole.SUPERVISOR)
                        val user = it.getSupervisor()
                        AppState.userId = user.id
                    } else {
                        _userRole.postValue(UserRole.ADMIN)
                    }
                }
            }
        }
    }
