package com.example.mvvm.views.projectdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.data.source.api.MyApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel
    @Inject
    constructor(private val api: MyApi,
    ) : BaseViewModel() {

    private val _response = MutableLiveData<Any>()
    val response: LiveData<Any> get() = _response

    fun setTopicScore(topicId: Long, score: Double) {
        viewModelScope.launch {
            try {
                val response = api.setScore(topicId, score)
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
    }
