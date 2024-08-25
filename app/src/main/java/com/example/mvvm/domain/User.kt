package com.example.mvvm.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class User(
    open val id: Long,
    open val name: String? = "",
    open val email: String? = "",
    open val role: UserRole? = UserRole.RESEARCHER,
    open val dob: String? = "",
    open val status: Boolean? = true,
) : Parcelable
