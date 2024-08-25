package com.example.mvvm.views.assign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.CouncilRequest
import com.example.mvvm.di.NetworkModule
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.Supervisor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AssignViewModel
@Inject constructor(
    private val projectRepository: ProjectRepository,
    private val api: MyApi,
) : BaseViewModel() {

    private val _supervisors = MutableLiveData<List<Supervisor>>()
    val supervisors: LiveData<List<Supervisor>> get() = _supervisors

    private val _allSupervisors = MutableLiveData<List<Supervisor>>()
    val allSupervisors: LiveData<List<Supervisor>> get() = _allSupervisors

    private val _researchers = MutableLiveData<List<Researcher>>()
    val researchers: LiveData<List<Researcher>> get() = _researchers

    fun getAllSupervisor() {
        viewModelScope.launch {
            try {
                val response = api.getUserByRole("")
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
        viewModelScope.launch {
            try {
                val response = api.approveProject(topicId, "gv1@gmail.com")
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
        val emails = _supervisors.value?.map { it.email } ?: emptyList()

        val councilRequest = CouncilRequest(
            lecturer1Email = emails.getOrNull(0),
            lecturer2Email = emails.getOrNull(1),
            lecturer3Email = emails.getOrNull(2),
            lecturer4Email = emails.getOrNull(3),
            lecturer5Email = emails.getOrNull(4)
        )
        viewModelScope.launch {
            try {
                val response = api.setCouncil(topicId, councilRequest)
                val res = api.setProjectTime(topicId, time)
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
