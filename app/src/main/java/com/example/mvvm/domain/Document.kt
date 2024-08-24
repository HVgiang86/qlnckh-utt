package com.example.mvvm.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Document(
    val id: Long,
    val title: String,
    val url: String,
) : Parcelable
