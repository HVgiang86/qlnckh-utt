package com.example.mvvm.base

interface ErrorMessage {
    fun getErrorMessage(): String
}

class UnknownError : ErrorMessage {
    override fun getErrorMessage() = "Unknown Error"
}
