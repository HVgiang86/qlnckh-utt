package com.example.mvvm.views.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.GetListResearcherSupervisorResponse
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ResearcherSupervisor
import com.example.mvvm.domain.Role
import com.example.mvvm.domain.UserRole
import com.example.mvvm.domain.toResearcherSupervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val api: MyApi
) : BaseViewModel() {
    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>>
        get() = _projects

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> = _profile

    private val _getListResearch = MutableLiveData<List<ResearcherSupervisor>>()
    val getListResearch: LiveData<List<ResearcherSupervisor>> = _getListResearch

    private val _getListSupervisor = MutableLiveData<List<ResearcherSupervisor>>()
    val getListSupervisor: LiveData<List<ResearcherSupervisor>> = _getListSupervisor

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> = _delete

    fun getHistory() {
        runFlow {
            if (AppState.userRole == UserRole.SUPERVISOR) {
                projectRepository.getProjectHistorySupervisor(AppState.userId).onCompletion { hideLoading() }.collect {
                    _projects.postValue(it)
                }
            } else if (AppState.userRole == UserRole.RESEARCHER) {
                projectRepository.getProjectHistoryResearcher(AppState.userId).onCompletion { hideLoading() }.collect {
                    _projects.postValue(it)
                }
            }
        }
    }

    fun getProfile() {
        runFlow{
            userRepository.getMyProfile().onCompletion { hideLoading() }.collect {
                Timber.d("Get my profile collect $it")
                _profile.postValue(it)
            }
        }
    }

    fun getListResearcherSupervisor(type: String) {

        if (type == Role.RESEARCHER.value) {
            getResearcher()
        } else if (type == Role.SUPERVISOR.value) {
            getSupervisor()
        }

    }

    private fun getResearcher() {
        runFlow {
            userRepository.getResearchers().onCompletion { hideLoading() }.collect {
                _getListResearch.postValue(it.map { it1 -> it1.toResearcherSupervisor() })
            }
        }
    }

    private fun getSupervisor() {
        runFlow {
            userRepository.getSupervisors().onCompletion { hideLoading() }.collect {
                _getListSupervisor.postValue(it.map { it1 -> it1.toResearcherSupervisor() })
            }
        }
    }

    fun deleteResearcherSupervisor(mail: String) {
        runFlow {
            userRepository.deleteResearcherSupervisor(mail).onCompletion { hideLoading() }.collect {
                _delete.postValue(it)
            }
        }
    }
}
