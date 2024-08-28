package com.example.mvvm.views.assign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.CouncilRequest
import com.example.mvvm.domain.Supervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AssignViewModel
    @Inject
    constructor(
        private val projectRepository: ProjectRepository,
        private val api: MyApi,
    ) : BaseViewModel() {

        private val _supervisors = MutableLiveData<List<String>>()
        val supervisors: LiveData<List<String>> get() = _supervisors

        private val _supervisorData = MutableLiveData<List<Supervisor>>()
        val supervisorData: LiveData<List<Supervisor>> get() = _supervisorData

        private val _response = MutableLiveData<Any>()
        val response: LiveData<Any> get() = _response

        fun removeSuperVisor(email: String) {
            val list = _supervisors.value?.toMutableList() ?: mutableListOf()
            list.remove(email)
            _supervisors.value = list
        }

        fun addSuperVisor(email: String) {
            val list = _supervisors.value?.toMutableList() ?: mutableListOf()
            list.add(email)
            _supervisors.value = list
        }

        fun getAllSupervisor() {
            viewModelScope.launch {
                try {
                    val response = api.getUserByRole("supervisor")
                    _supervisorData.value = response.data
                } catch (e: HttpException) {
                    // Handle HTTP exception
                    println("HTTP error: ${e.message()}")
                } catch (e: IOException) {
                    // Handle network IO exception
                    println("Network error: ${e.message}")
                }
            }
        }

        fun approveProject(topicId: Long) {
            val emails = _supervisors.value ?: emptyList()

            viewModelScope.launch {
                try {
                    val response = api.approveProject(topicId, emails.getOrElse(0) { "" })
                    _response.value = response
                } catch (e: HttpException) {
                    // Handle HTTP exception
                    println("HTTP error: ${e.message()}")
                } catch (e: IOException) {
                    // Handle network IO exception
                    println("Network error: ${e.message}")
                }
            }
        }

        fun assignProject(topicId: Long, time: String) {
            val emails = _supervisors.value ?: emptyList()

            val councilRequest = CouncilRequest(
                lecturer1Email = emails.getOrNull(0),
                lecturer2Email = emails.getOrNull(1),
                lecturer3Email = emails.getOrNull(2),
                lecturer4Email = emails.getOrNull(3),
                lecturer5Email = emails.getOrNull(4),
            )
            viewModelScope.launch {
                try {
                    val response = api.setCouncil(topicId, councilRequest)
                    val res = api.setProjectTime(topicId, time)
                    _response.value = res
                } catch (e: HttpException) {
                    // Handle HTTP exception
                    println("HTTP error: ${e.message()}")
                } catch (e: IOException) {
                    // Handle network IO exception
                    println("Network error: ${e.message}")
                }
            }
        }
    }
