package com.example.mvvm.domain

object AppState {
    var logined: Boolean = false
    var userId: Long = 1
    var name: String = ""
    var firstAppOpen: Boolean = false
    var userRole: UserRole = UserRole.RESEARCHER
}
