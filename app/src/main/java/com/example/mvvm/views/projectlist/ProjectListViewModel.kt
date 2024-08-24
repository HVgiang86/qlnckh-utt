package com.example.mvvm.views.projectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.domain.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel
    @Inject
    constructor(private val projectRepository: ProjectRepository) : BaseViewModel() {
        private val _projects = MutableLiveData<List<Project>>(emptyList())
        val projects: LiveData<List<Project>> get() = _projects

        fun getAllProject() {
            runFlow(Dispatchers.IO) {
                projectRepository.getAllProject().collect {
                    Timber.d("get projects $it")
                    _projects.postValue(it)
                }
            }
        }
    }
