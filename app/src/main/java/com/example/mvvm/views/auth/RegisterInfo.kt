package com.example.mvvm.views.auth

import android.os.Parcelable
import com.example.mvvm.domain.UserRole
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class RegisterInfo(
    val name: String,
    val email: String,
    val password: String,
    val birthday: String,
    val role: UserRole,
    val faculty: String,
    val department: String,
    val title: String,
    val major: String,
    val className: String,
) : Serializable, Parcelable
