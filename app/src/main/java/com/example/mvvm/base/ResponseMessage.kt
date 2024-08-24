package com.example.mvvm.base

data class ResponseMessage(
    val message: String,
    val bgType: BGType,
)

enum class BGType {
    BG_TYPE_NORMAL,
    BG_TYPE_SUCCESS,
    BG_TYPE_WARNING,
    BG_TYPE_ERROR,
}

object AppConstants {
    const val DEFAULT_MESSAGE_ERROR = "Có lỗi không xác định"
    const val EMAIL_PASSWORD_MESSAGE_ERROR = "Tài khoản hoặc mật khẩu không đúng"
    const val SIGN_UP_MESSAGE_ERROR = "Tài khoản đã tồn tại"

    const val PREF_NAME = "room_picker_prefs"
    const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"

    const val TOAST_DURATION = 1000
}
