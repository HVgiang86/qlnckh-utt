package com.example.mvvm.data.source.api.model.request

import com.google.gson.annotations.Expose

data class LoginRequest(
    @Expose val username: String,
    @Expose val password: String,
)
