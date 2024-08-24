package com.example.mvvm.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SupervisorComment(
    val id: Long,
    val content: String,
    val userName: String,
) : Parcelable
