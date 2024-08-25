package com.example.mvvm.views.auth

import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.UserRepository
import com.example.mvvm.datacore.token.TokenRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject constructor(private val userRepository: UserRepository, private val tokenRepository: TokenRepository) : BaseViewModel() {
    private val _userRole = MutableLiveData<UserRole?>(null)
    private val _loginSuccess = MutableLiveData(false)

    private val _signUpSuccess = MutableLiveData(false)

    val userRole: MutableLiveData<UserRole?> = _userRole
    val loginSuccess: MutableLiveData<Boolean> = _loginSuccess
    val signUpSuccess: MutableLiveData<Boolean> = _signUpSuccess

    fun signIn(userName: String, password: String) {
        runFlow(Dispatchers.IO) {
            userRepository.login(userName, password).onCompletion { hideLoading() }.collect {
                Timber.d("collect $it")
                tokenRepository.clearToken()
                tokenRepository.saveToken(it.result.token)
                _loginSuccess.postValue(true)
            }
        }
    }

    fun getProfile() {
        runFlow(Dispatchers.IO) {
            userRepository.getMyProfile().onCompletion { hideLoading() }.collect {
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

    fun saveLoginState(state: Boolean) {
        tokenRepository.setLoginState(state)
    }

    fun signUpResearcher(registerInfo: RegisterInfo) {
        runFlow(Dispatchers.IO) {
            userRepository.registerResearcher(registerInfo).onCompletion { hideLoading() }.collect {
                Timber.d("collect $it")
                tokenRepository.clearToken()
                _signUpSuccess.postValue(true)
            }
        }
    }

    fun signUpSupervisor(registerInfo: RegisterInfo) {
        runFlow(Dispatchers.IO) {
            userRepository.registerSupervisor(registerInfo).onCompletion { hideLoading() }.collect {
                Timber.d("collect $it")
                tokenRepository.clearToken()
                _signUpSuccess.postValue(true)
            }
        }
    }
}
