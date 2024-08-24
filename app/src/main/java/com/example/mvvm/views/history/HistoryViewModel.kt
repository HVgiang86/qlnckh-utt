package com.example.mvvm.views.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(private val projectRepository: ProjectRepository) : BaseViewModel() {
        private val _projects = MutableLiveData<List<Project>>()

        val projects: LiveData<List<Project>>
            get() = _projects

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
    }
