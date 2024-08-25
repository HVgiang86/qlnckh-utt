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
    constructor(private val projectRepository: ProjectRepository,
                private val userRepository: UserRepository,
                private val api: MyApi) : BaseViewModel() {
    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>>
        get() = _projects

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> = _profile

    private val _getListResearch = MutableLiveData<Map<String, List<ResearcherSupervisor>>>()
    val getListResearch: LiveData<Map<String, List<ResearcherSupervisor>>> = _getListResearch

    private val _getListSupervisor = MutableLiveData<Map<String, List<ResearcherSupervisor>>>()
    val getListSupervisor: LiveData<Map<String, List<ResearcherSupervisor>>> = _getListSupervisor

    private val _delete = MutableLiveData<Unit>()
    val delete: LiveData<Unit> = _delete

    fun getHistory() {
        runFlow(Dispatchers.IO) {
            if (AppState.userRole == UserRole.SUPERVISOR) {
                projectRepository.getProjectHistorySupervisor(AppState.userId).collect {
                    _projects.postValue(it)
                }
            } else if (AppState.userRole == UserRole.RESEARCHER) {
                projectRepository.getProjectHistoryResearcher(AppState.userId).collect {
                    _projects.postValue(it)
                }
            }
        }
    }

    fun getProfile() {
        runFlow(Dispatchers.IO) {
            userRepository.getMyProfile().onCompletion { hideLoading() }.collect {
                Timber.d("Get my profile collect $it")
                _profile.postValue(it)
            }
        }
    }

    fun getListResearcherSupervisor(type: String) {
        viewModelScope.launch {
            try {
                val response = api.getResearchSupervisor(type)
                if (type == Role.RESEARCHER.value) {
                    _getListResearch.postValue(response)
                } else if (type == Role.SUPERVISOR.value) {
                    _getListSupervisor.postValue(response)
                }
            } catch (e: HttpException) {
                println("HTTP error: ${e.message()}")
            } catch (e: IOException) {
                println("Network error: ${e.message}")
            }
        }
    }

    fun deleteResearcherSupervisor(mail: String) {
        viewModelScope.launch {
            try {
                val response = api.deleteResearchSupervisor(mail)
                _delete.postValue(response)
            } catch (e: HttpException) {
                println("HTTP error: ${e.message()}")
            } catch (e: IOException) {
                println("Network error: ${e.message}")
            }
        }
    }
}
