package com.example.mvvm.views

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
class MainViewModel @Inject constructor(val userRepository: UserRepository, val tokenRepository: TokenRepository) : BaseViewModel() {
    val canAutoLogin = MutableLiveData(false)

    fun getProfile() {
        runFlow(Dispatchers.IO) {
            userRepository.getMyProfile().onCompletion { hideLoading() }.collect {

                Timber.d("Get my profile collect $it")

                canAutoLogin.postValue(true)

                if (it.isResearcher()) {
                    val user = it.getResearcher()
                    AppState.userId = user.id
                    AppState.userRole = UserRole.RESEARCHER
                } else if (it.isSupervisor()) {
                    val user = it.getSupervisor()
                    AppState.userId = user.id
                    AppState.userRole = UserRole.SUPERVISOR
                } else {
                    AppState.userRole = UserRole.ADMIN
                }
            }
        }
    }

    fun getLoginState(): Boolean {
        return tokenRepository.getLoginState()
    }

    fun setLoggedIn() {
        tokenRepository.setLoginState(true)
        AppState.logined = true
    }

    fun clearLoggedIn() {
        tokenRepository.setLoginState(false)
        AppState.logined = false
    }

}
