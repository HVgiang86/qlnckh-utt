package com.example.mvvm.views.projectdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.source.api.MyApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel
@Inject
constructor(
    private val api: MyApi,
    private val projectRepository: ProjectRepository
) : BaseViewModel() {

    private val _response = MutableLiveData<Any>()
    val response: LiveData<Any> get() = _response

    fun setTopicScore(topicId: Long, score: Double) {
        runFlow {
            projectRepository.setProjectScore(topicId, score).onCompletion { hideLoading() }.collect {
                _response.postValue(it)
            }
        }
    }

    fun markFinish(projectId: Long) {
        runFlow {
            projectRepository.setUnderReview(projectId).onCompletion { hideLoading() }.collect {
                _response.postValue(it)
            }
        }
    }
}
