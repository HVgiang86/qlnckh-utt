package com.example.mvvm.utils.utils

import android.util.Patterns

object AppUtils {

    fun isValidatedName(name: String?): Boolean {
        return !name.isNullOrEmpty()
    }

    fun isValidatedEmail(email: String?): Boolean {
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidatedPassword(password: String?): Boolean {
        return !password.isNullOrEmpty() && password.length >= 6
    }

    fun isValidatedConfirmPass(password: String?, confirmPass: String?): Boolean {
        return !confirmPass.isNullOrEmpty() && confirmPass == password
    }
}
