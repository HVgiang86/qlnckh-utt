package com.example.mvvm.datacore

import com.example.mvvm.data.source.api.model.response.BaseResponse
import com.example.mvvm.di.IODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseDataSource {
    @Inject
    lateinit var dispatchersProvider: IODispatcher

    protected suspend fun <R> withResultContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        requestBlock: suspend CoroutineScope.() -> R,
        errorBlock: (suspend CoroutineScope.(Exception) -> DataResult.Error)? = null,
    ): DataResult<R> = withContext(context) {
        return@withContext try {
            val response = requestBlock()
            DataResult.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext errorBlock?.invoke(this, e) ?: DataResult.Error(e)
        }
    }

    protected fun getContext() = dispatchersProvider

    protected suspend fun <R> withResultContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        requestBlock: suspend CoroutineScope.() -> R,
    ): DataResult<R> = withResultContext(context, requestBlock, null)

    protected suspend fun <R> returnResult(
        requestBlock: suspend () -> R,
    ): DataResult<R> {
        return try {
            val response = requestBlock()
            if (response == null) {
                DataResult.Error(Exception("Response is null"))
            } else {
                DataResult.Success(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataResult.Error(e)
        }
    }

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

    protected suspend fun <R> resultWithBase(
        block: suspend () -> Response<BaseResponse<R>>,
    ): DataResult<R> {
        try {
            val response = block.invoke()
            if (response.isSuccessful) {
                val result = response.body()?.successfully
                println("[DEBUG] $result")

                if (result == null) return DataResult.Error(Exception("Response is null"))

                return DataResult.Success(result)
            } else {
                println("[DEBUG] ${response.code()} ${response.message()}")
                return DataResult.Error(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DataResult.Error(e)
        }
    }
}
