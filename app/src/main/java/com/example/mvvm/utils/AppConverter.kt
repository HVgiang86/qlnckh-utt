package com.example.mvvm.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateToDDMMYYYY(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(date)
}
