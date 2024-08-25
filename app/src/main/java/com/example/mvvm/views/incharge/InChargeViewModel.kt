package com.example.mvvm.views.incharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectState.*
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InChargeViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
    ) : BaseViewModel() {
        private val _inCharge = MutableLiveData<Project?>(null)
        val inCharge: LiveData<Project?> get() = _inCharge

        fun getInCharge() {
            runFlow(Dispatchers.IO) {
                Timber.d("get in charge ${AppState.userId} ${AppState.userRole}")
                if (AppState.userRole == UserRole.RESEARCHER) {
                    projectRepository.getInChargeResearcher(AppState.userId).collect {
                        Timber.d("get in charge $it")
                        _inCharge.postValue(it)
                    }
                } else {
                    projectRepository.getInChargeSupervisor(AppState.userId).collect {
                        Timber.d("get in charge $it")
                        _inCharge.postValue(it.firstOrNull())
                    }
                }
            }
        }

        fun addReport(report: ResearcherReport) {
//        runFlow(Dispatchers.IO) {
//            projectRepository.addResearcherReport(report)
//        }
        }

        fun cancelProject() {
            _inCharge.value = inCharge.value?.copy(state = CANCELLED)
        }

        fun pauseProject() {
            _inCharge.value = inCharge.value?.copy(state = PAUSED)
        }

        fun resumeProject() {
            _inCharge.value = inCharge.value?.copy(state = IN_PROGRESS)
        }
    }

fun MutableLiveData<List<Project>>.addValue(value: Project) {
    val list = this.value?.toMutableList() ?: mutableListOf()
    list.add(value)
    this.value = list
}
