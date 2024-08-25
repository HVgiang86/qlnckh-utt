package com.example.mvvm.domain

object AppState {
    var logined: Boolean = false
    var userId: Long = 1
    var name: String = ""
    var email: String = ""
    var hasInChargeProject: Boolean = false
    var firstAppOpen: Boolean = false
    var userRole: UserRole = UserRole.RESEARCHER
}
