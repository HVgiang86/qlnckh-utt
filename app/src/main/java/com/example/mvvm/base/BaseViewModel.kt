package com.example.mvvm.base

import com.example.mvvm.utils.ext.isNull
import com.example.mvvm.utils.ext.notNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class BaseViewModel {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _errorResponse = MutableStateFlow<String?>(null)
    val errorResponse = _errorResponse.asStateFlow()
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

    fun <S : ErrorMessage> convertToRetrofitException(throwable: Throwable): RetrofitException {
        when (throwable) {
            is RetrofitException -> return throwable
            is IOException -> return RetrofitException.toNetworkError(throwable)
            is HttpException -> {
                throwable.response()
                val response = throwable.response()
                if (response == null || response.code() == HttpURLConnection.HTTP_BAD_GATEWAY) {
                    return RetrofitException.toUnexpectedError(throwable)
                }

                response.errorBody().isNull {
                    return RetrofitException.toServerError(response)
                }

                response.errorBody()?.notNull {
                    return when (response.code()) {
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                            RetrofitException.toServerError(response)
                        }

                        else -> {
                            RetrofitException.toHttpError(response)
                        }
                    }
                }
            }
        }
        return RetrofitException.toUnexpectedError(throwable)
    }
}
