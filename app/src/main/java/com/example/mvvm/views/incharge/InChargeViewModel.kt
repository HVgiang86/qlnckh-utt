package com.example.mvvm.views.incharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectState.CANCEL
import com.example.mvvm.domain.ProjectState.IN_PROGRESS
import com.example.mvvm.domain.ProjectState.PAUSED
import com.example.mvvm.domain.ProjectState.UNDER_REVIEW
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.domain.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InChargeViewModel
@Inject constructor(
    private val projectRepository: ProjectRepository,
) : BaseViewModel() {
    private val _inCharge = MutableLiveData<Project?>(null)
    val inCharge: LiveData<Project?> get() = _inCharge

    fun getInCharge() {
        runFlow {
            Timber.d("get in charge ${AppState.userId} ${AppState.userRole}")
            if (AppState.userRole == UserRole.RESEARCHER) {
                projectRepository.getInChargeResearcher(AppState.userId).onCompletion { hideLoading() }.collect {
                    Timber.d("get in charge $it")
                    _inCharge.postValue(it)
                    AppState.hasInChargeProject = it != null
                }
            } else {
                projectRepository.getInChargeSupervisor(AppState.userId).onCompletion { hideLoading() }.collect {
                    Timber.d("get in charge $it")
                    _inCharge.postValue(it.firstOrNull())
                    AppState.hasInChargeProject = it.isNotEmpty()
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
        _inCharge.value = inCharge.value?.copy(state = CANCEL)
        runFlow {
            inCharge.value?.id?.let { projectRepository.setCancel(it) }?.onCompletion { hideLoading() }?.collect {
                getInCharge()
            }
        }

    }

    fun pauseProject() {
        _inCharge.value = inCharge.value?.copy(state = PAUSED)
        runFlow {
            inCharge.value?.id?.let { projectRepository.setPauseOrResume(it) }?.onCompletion { hideLoading() }?.collect {
                getInCharge()
            }
        }
    }

    fun resumeProject() {
        _inCharge.value = inCharge.value?.copy(state = IN_PROGRESS)
        runFlow {
            inCharge.value?.id?.let { projectRepository.setPauseOrResume(it) }?.onCompletion { hideLoading() }?.collect {
                getInCharge()
            }
        }
    }

    fun markFinish() {
        _inCharge.value = inCharge.value?.copy(state = UNDER_REVIEW)
        runFlow {
            inCharge.value?.id?.let { projectRepository.setUnderReview(it) }?.onCompletion { hideLoading() }?.collect {
                getInCharge()
            }
        }
    }
}

fun MutableLiveData<List<Project>>.addValue(value: Project) {
    val list = this.value?.toMutableList() ?: mutableListOf()
    list.add(value)
    this.value = list
}
