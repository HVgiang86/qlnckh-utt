package com.example.mvvm.base

import retrofit2.Response

class RetrofitException : RuntimeException {
    private val errorType: String
    private lateinit var responses: Response<*>
    private var unexpectedThrowable: Throwable? = null
    private var errorMessage: String? = null

    private constructor(type: String, cause: Throwable) : super(cause.message, cause) {
        errorType = type
        unexpectedThrowable = cause
    }

    private constructor(type: String, response: Response<*>) {
        errorType = type
        responses = response
    }

    constructor(type: String, msg: String?) {
        errorType = type
        errorMessage = msg
    }

    fun getErrorMessage() = errorMessage

    fun getMessageDataError(): String? =
        if (errorMessage?.isNotEmpty() == true) {
            errorMessage
        } else {
            responses.message()
        }

    fun getMessageError(): String? =
        when (errorType) {
            NetworkErrorType.SERVER -> {
                errorMessage
            }

            NetworkErrorType.NETWORK -> {
                getNetworkErrorMessage(cause)
            }

            NetworkErrorType.HTTP -> {
                errorMessage.let {
                    if (it.isNullOrEmpty()) responses.code().getHttpErrorMessage() else it
                }
            }

            else -> unexpectedThrowable?.message
        }

    private fun getNetworkErrorMessage(throwable: Throwable?): String = throwable?.message.toString()

    private fun Int.getHttpErrorMessage(): String =
        when (this) {
            in 300..308 -> "It was transferred to a different URL. I'm sorry for causing you trouble"
            in 400..451 -> "An error occurred on the application side. Please try again later!"
            in 500..511 -> "A server error occurred. Please try again later!"
            else -> "A server error occurred. Please try again later!"
        }

    companion object {
        fun toNetworkError(cause: Throwable): RetrofitException = RetrofitException(NetworkErrorType.NETWORK, cause)

        fun toHttpError(response: Response<*>): RetrofitException = RetrofitException(NetworkErrorType.HTTP, response)

        fun toUnexpectedError(cause: Throwable): RetrofitException = RetrofitException(NetworkErrorType.UNEXPECTED, cause)

        fun toServerError(response: Response<*>): RetrofitException = RetrofitException(NetworkErrorType.SERVER, response)
    }
}
