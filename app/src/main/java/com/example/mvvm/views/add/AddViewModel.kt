package com.example.mvvm.views.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.domain.Supervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AddViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
    ) : BaseViewModel() {
        private val _documents = MutableLiveData<List<Document>>()
        val documents: LiveData<List<Document>> get() = _documents

        private val _supervisors = MutableLiveData<List<Supervisor>>()
        val supervisors: LiveData<List<Supervisor>> get() = _supervisors

        private val _researchers = MutableLiveData<List<Researcher>>()
        val researchers: LiveData<List<Researcher>> get() = _researchers

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

        fun addReport(report: ResearcherReport, projectId: Long) {
            runFlow(Dispatchers.IO) {
                projectRepository.addResearcherReport(report, projectId)
            }
        }

        fun addProject(project: Project) {
            runFlow(Dispatchers.IO) {
                projectRepository.addProject(project)
            }
        }
    }
