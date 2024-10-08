package com.example.mvvm.views.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.source.api.model.request.UpdateProfileRequest
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.datacore.token.TokenRepository
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val userRepository: UserRepository, val tokenRepository: TokenRepository) : BaseViewModel() {
    private val _profileResearcher = MutableLiveData<Researcher>()
    private val _profileSupervisor = MutableLiveData<Supervisor>()
    private val _profileAdmin = MutableLiveData<ProfileResponse>()
    val profileResearcher: LiveData<Researcher> = _profileResearcher
    val profileSuperVisor: LiveData<Supervisor> = _profileSupervisor
    val profileAdmin: LiveData<ProfileResponse> = _profileAdmin

    val loggedOut = MutableLiveData(false)

    fun getProfile() {
        runFlow {
            userRepository.getMyProfile().onCompletion { hideLoading() }.collect {
                Timber.d("Get my profile collect $it")

                if (it.isResearcher()) {
                    _profileResearcher.postValue(it.getResearcher())
                } else if (it.isSupervisor()) {
                    _profileSupervisor.postValue(it.getSupervisor())
                } else {
                    _profileAdmin.postValue(it)
                }
            }
        }
    }

    fun getProfileByEmail(email: String) {
        runFlow {
            userRepository.getProfileByEmail(email).onCompletion { hideLoading() }.collect {
                Timber.d("Get my profile collect $it")

                if (it.isResearcher()) {
                    _profileResearcher.postValue(it.getResearcher())
                } else if (it.isSupervisor()) {
                    _profileSupervisor.postValue(it.getSupervisor())
                } else {
                    _profileAdmin.postValue(it)
                }
            }
        }
    }

    fun updateProfile(email: String, request: UpdateProfileRequest) {
        runFlow {
            userRepository.updateProfile(email, request).onCompletion { hideLoading() }.collect {
                Timber.d("Update profile collect $it")
                getProfile()
            }
        }
    }

    fun logout() {
        tokenRepository.clearToken()
        tokenRepository.clearCookie()
        tokenRepository.setLoginState(false)
        loggedOut.value = true
    }
}
