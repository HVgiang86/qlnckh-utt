package com.example.mvvm.datacore

import com.example.mvvm.di.IODispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository {
    @Inject
    lateinit var dispatchersProvider: IODispatcher

    /**
     * Make template code to get data with flow
     * @return a flow of data
     */
    protected suspend fun <R> flowContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        requestBlock: suspend CoroutineScope.() -> DataResult<R>,
    ): Flow<R> =
        withContext(context) {
            flow {
                when (val result = requestBlock()) {
                    is DataResult.Error -> {
                        throw result.exception
                    }

                    DataResult.Loading -> {} // no-op

                    is DataResult.Success -> {
                        emit(result.data)
                    }
                }
            }
        }

    protected suspend fun <R> flowContextWithoutException(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        requestBlock: suspend CoroutineScope.() -> DataResult<R>,
    ): Flow<R> =
        withContext(context) {
            flow {
                when (val result = requestBlock()) {
                    is DataResult.Error -> {
                        cancel()
                    }

                    DataResult.Loading -> {} // no-op

                    is DataResult.Success -> {
                        emit(result.data)
                    }
                }
            }
        }

    /**
     * Make template code to get data with flow in Offline mode
     * Run [initial] block when call and after that run [remoteRequest] block
     * If [remoteRequest] return [DataResult.Error] then run [localRequest] block
     * @return a flow of data
     */
    protected suspend fun <R> flowContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        initial: suspend CoroutineScope.() -> DataResult<R>,
        remoteRequest: suspend CoroutineScope.() -> DataResult<R>,
        localRequest: (suspend CoroutineScope.() -> DataResult<R>)? = null,
    ): Flow<R> =
        withContext(context) {
            flow {
                val initResult = initial()
                val remoteResult = remoteRequest()
                var previousData: R? = null

                // Initial data
                when (initResult) {
                    is DataResult.Error -> {} // no-op

                    is DataResult.Success -> {
                        emit(initResult.data)
                        previousData = initResult.data
                    }

                    is DataResult.Loading -> {} // no-op
                }

                // Remote data
                when (remoteResult) {
                    is DataResult.Success -> {
                        // Data comparing
                        if (previousData != remoteResult.data) {
                            emit(remoteResult.data)
                        }
                    }

                    is DataResult.Error -> {
                        val localResult = if (localRequest != null) localRequest() else initial()
                        // Run local data when remote data error
                        when (localResult) {
                            is DataResult.Error -> throw localResult.exception

                            DataResult.Loading -> {} // no-op

                            is DataResult.Success -> {
                                // Data comparing
                                if (previousData != localResult.data) {
                                    emit(localResult.data)
                                }
                            }
                        }
                    }

                    DataResult.Loading -> {} // no-op
                }
            }
        }

    protected suspend fun <R> flowContext(
        context: CoroutineContext = dispatchersProvider.dispatcher(),
        initial: suspend CoroutineScope.() -> DataResult<R>,
        remoteRequest: suspend CoroutineScope.() -> DataResult<R>,
        localRequest: (suspend CoroutineScope.() -> DataResult<R>)? = null,
        onRemoteSuccess: (suspend CoroutineScope.(R) -> Unit)? = null,
        onLocalSuccess: (suspend CoroutineScope.(R) -> Unit)? = null,
        onRemoteError: (suspend CoroutineScope.(Exception) -> Unit)? = null,
        onLocalError: (suspend CoroutineScope.(Exception) -> Unit)? = null,
    ): Flow<R> =
        withContext(context) {
            flow {
                val initResult = initial()
                val remoteResult = remoteRequest()
                var previousData: R? = null

                // Initial data
                when (initResult) {
                    is DataResult.Error -> {} // no-op

                    is DataResult.Success -> {
                        emit(initResult.data)
                        previousData = initResult.data
                    }

                    is DataResult.Loading -> {} // no-op
                }

                // Remote data
                when (remoteResult) {
                    is DataResult.Success -> {
                        // Data comparing
                        if (previousData != remoteResult.data) {
                            onRemoteSuccess?.invoke(this@withContext, remoteResult.data)
                            emit(remoteResult.data)
                        }
                    }

                    is DataResult.Error -> {
                        onRemoteError?.invoke(this@withContext, remoteResult.exception)

                        val localResult = if (localRequest != null) localRequest() else initial()
                        // Run local data when remote data error
                        when (localResult) {
                            is DataResult.Error -> {
                                onLocalError?.invoke(this@withContext, localResult.exception)
                                throw localResult.exception
                            }

                            DataResult.Loading -> {} // no-op

                            is DataResult.Success -> {
                                onLocalSuccess?.invoke(this@withContext, localResult.data)

                                // Data comparing
                                if (previousData != localResult.data) {
                                    emit(localResult.data)
                                }
                            }
                        }
                    }

                    DataResult.Loading -> {} // no-op
                }
            }
        }

    protected suspend fun <R> runFlow(request: suspend () -> DataResult<R>): Flow<R> = flow {
        when (val result = request()) {
            is DataResult.Success -> emit(result.data)
            is DataResult.Error -> throw result.exception
            DataResult.Loading -> {}
        }
    }
}
