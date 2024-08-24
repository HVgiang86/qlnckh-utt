package com.example.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    var job: Job? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _responseMessage = MutableSharedFlow<ResponseMessage>()
    val responseMessage: SharedFlow<ResponseMessage> = _responseMessage

    private val _exception = MutableStateFlow<Throwable?>(null)
    val exception = _exception.asStateFlow()

    private var loadingCount = 0

    fun showLoading() {
        loadingCount++
        _isLoading.update { loadingCount > 0 }
    }

    fun hideLoading() {
        loadingCount--
        _isLoading.update { loadingCount > 0 }
    }

    fun handleException(exception: Throwable) {
        _exception.value = exception
    }

    private var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        hideLoading()
        // show message
        println("BaseViewModel exceptionHandler: ${throwable.message}")
        handleMessage(
            message = throwable.message ?: AppConstants.DEFAULT_MESSAGE_ERROR,
            bgType = BGType.BG_TYPE_ERROR,
        )
        _exception.update { throwable }
    }

    private fun handleMessage(message: String, bgType: BGType) {
        viewModelScope.launch {
            _responseMessage.emit(
                ResponseMessage(
                    message = message,
                    bgType = bgType,
                ),
            )
        }
    }

    fun runFlow(
        context: CoroutineContext,
        block: suspend () -> Unit,
    ) {
        job = viewModelScope.launch(context + exceptionHandler) {
            showLoading()
            block.invoke()
            hideLoading()
        }
    }
}
