package com.example.mvvm.views.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.UserRepository
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.domain.Supervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddViewModel
@Inject constructor(
    private val projectRepository: ProjectRepository, private val userRepository: UserRepository
) : BaseViewModel() {
    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>> get() = _documents

    private val _supervisors = MutableLiveData<List<Supervisor>>()
    val supervisors: LiveData<List<Supervisor>> get() = _supervisors

    private val _researchers = MutableLiveData<List<Researcher>>()
    val researchers: LiveData<List<Researcher>> get() = _researchers

    private val _selectedSupervisor = MutableLiveData<Supervisor?>()
    val selectedSupervisor: LiveData<Supervisor?> get() = _selectedSupervisor

    private val _selectedResearcher = MutableLiveData<List<Researcher>>()
    val selectedResearcher: LiveData<List<Researcher>> get() = _selectedResearcher

    private val _addProjectDone = MutableLiveData(false)
    val addProjectDone: LiveData<Boolean> get() = _addProjectDone

    private val _addReportDone = MutableLiveData(false)
    val addReportDone: LiveData<Boolean> get() = _addReportDone

    fun addDocument(document: Document) {
        val list = _documents.value?.toMutableList() ?: mutableListOf()
        list.add(document)
        _documents.value = list
    }

    fun removeDocument(document: Document) {
        val list = _documents.value?.toMutableList() ?: mutableListOf()
        list.remove(document)
        _documents.value = list
    }

    fun getSupervisors() {
        runFlow(Dispatchers.IO) {
            userRepository.getSupervisors().onCompletion { hideLoading() }.collect {
                Timber.d("Supervisors: $it")
                _supervisors.postValue(it)
            }
        }
    }

    fun removeSupervisor() {
        _selectedSupervisor.value = null
    }

    fun setSupervisor(supervisor: Supervisor) {
        _selectedSupervisor.value = supervisor
    }

    fun removeResearcher(researcher: Researcher) {
        val list = _selectedResearcher.value?.toMutableList() ?: mutableListOf()
        list.remove(researcher)
        _selectedResearcher.value = list
    }

    fun addReport(report: ResearcherReport, projectId: Long) {
        runFlow(Dispatchers.IO) {
            projectRepository.addReport(report, projectId).onCompletion { hideLoading() }.collect {
                uploadFileAddReport(report.id)
                _addReportDone.postValue(true)
            }
        }
    }

    fun addProject(project: Project) {
        runFlow(Dispatchers.IO) {
            projectRepository.addProject(project).onCompletion { hideLoading() }.flatMapConcat { project ->
                uploadFileAddProject(project.id)
                projectRepository.addResearcherToProject(project.id, AppState.email)
            }.collect {
                _addProjectDone.postValue(true)
            }
        }
    }

    fun uploadFileAddProject(projectId: Long) {
        runFlow(Dispatchers.IO) {
            documents.value?.forEach {
                projectRepository.addAttachmentToProject(projectId, it.title, it.url).onCompletion { hideLoading() }.collect {
                    Timber.d("Upload file success: $it")
                }
            }
        }
    }

    fun uploadFileAddReport(reportId: Long) {
        runFlow {
            documents.value?.forEach {
                projectRepository.addAttachmentToReport(reportId, it.title, it.url).onCompletion { hideLoading() }.collect {
                    Timber.d("Upload file success: $it")
                }
            }
        }
    }

}
