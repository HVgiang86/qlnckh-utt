package com.example.mvvm.datacore

import com.example.mvvm.di.IODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseDataSource {
    @Inject
    lateinit var dispatchersProvider: IODispatcher

    protected suspend fun <R> withResultContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        requestBlock: suspend CoroutineScope.() -> R,
        errorBlock: (suspend CoroutineScope.(Exception) -> DataResult.Error)? = null,
    ): DataResult<R> =
        withContext(context) {
            return@withContext try {
                val response = requestBlock()
                DataResult.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext errorBlock?.invoke(this, e)
                    ?: DataResult.Error(e)
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
            }

            DataResult.Success(response)
        } catch (e: Exception) {
            println("BASE_SOURCE Error: ${e.message}")
            e.printStackTrace()
            DataResult.Error(e)
        }
    }
}
