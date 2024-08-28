package com.example.mvvm.datacore

import android.accounts.NetworkErrorException
import com.example.mvvm.di.IODispatcher
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

abstract class BaseDataSource {
    @Inject
    lateinit var dispatchersProvider: IODispatcher

    protected fun getContext() = dispatchersProvider

    protected suspend fun <R> result(
        block: suspend () -> Response<R>,
    ): DataResult<R> {
        try {
            val response = block.invoke()
            if (response.isSuccessful) {
                val result = response.body()
                println("[DEBUG] $result")

                if (result == null) return DataResult.Error(Exception("Response is null"))

                return DataResult.Success(result)
            } else {
                return DataResult.Error(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DataResult.Error(e)
        }
    }

    protected suspend fun <R> returnResult(
        block: suspend () -> BaseResponse<R>,
    ): DataResult<R> {
        try {
            val response = block.invoke()
            if (response.code == 200) {
                val data = response.data
                val message = response.message
                println("[DEBUG] ${response.code} $message $data")

                if (data == null) {
                    if (message == null) return DataResult.Error(Exception("Unknown error"))
                    return DataResult.Error(Exception(message))
                }

                return DataResult.Success(data)
            } else if (response.code == 500) {
                return DataResult.Error(Exception("Server error"))
            } else {
                return DataResult.Error(Exception(response.message))
            }
        } catch (e: NetworkErrorException) {
            e.printStackTrace()
            return DataResult.Error(Exception("Network error"))
        } catch (e: HttpException) {
            e.printStackTrace()
            val errorResponse = e.response()?.errorBody()?.getErrorResponse()
            if (errorResponse != null) {
                return DataResult.Error(Exception(errorResponse.message))
            }

            return DataResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            return DataResult.Error(Exception("Unknown error"))
        }
    }

    protected suspend fun returnIfSuccess(
        block: suspend () -> BaseResponse<*>,
    ): DataResult<Boolean> {
        try {
            val response = block.invoke()
            if (response.code == 200) {
                val data = response.data
                val message = response.message
                println("[DEBUG] ${response.code} $message $data")

                if (data == null) {
                    if (message == null) return DataResult.Error(Exception("Unknown error"))
                    return DataResult.Error(Exception(message))
                }

                return DataResult.Success(true)
            } else if (response.code == 500) {
                return DataResult.Error(Exception("Server error"))
            } else {
                return DataResult.Error(Exception(response.message))
            }
        } catch (e: NetworkErrorException) {
            e.printStackTrace()

            return DataResult.Error(Exception("Network error"))
        } catch (e: Exception) {
            e.printStackTrace()

            return DataResult.Error(e)
        }
    }
}

fun ResponseBody.getErrorResponse(): ErrorResponse {
    return Gson().fromJson(this.string(), ErrorResponse::class.java)
}
