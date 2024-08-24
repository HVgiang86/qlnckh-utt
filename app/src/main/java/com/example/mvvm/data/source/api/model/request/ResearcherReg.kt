package com.example.mvvm.data.source.api.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResearcherReg(
    @Expose
    val fullName: String,
    @Expose
    val email: String,
    @Expose
    val password: String,
    @Expose
    val dateOfBirth: String,
    @Expose
    val major: String,
    @Expose
    val className: String,
) : Parcelable
