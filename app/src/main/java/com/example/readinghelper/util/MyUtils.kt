package com.example.readinghelper.util

import java.text.SimpleDateFormat
import java.util.*

object MyUtils {
    var MESSAGE_AUTHENTICATION_FAILED = "Firebase authentication failed, please check your internet connection"
    var MESSAGE_INVALIDE_ROOM_NAME = "Enter a valid Name"
    var EXTRA_ROOM_NAME = "EXTRA_ROOM_NAME"
    const val OPEN_ACTIVITY = 1
    const val SHOW_TOAST = 2
    const val UPDATE_MESSAGES = 1
    fun convertTime(timestamp: Long): String {
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat("HH:mm")
        val date = Date(timestamp)
        sdf.setTimeZone(TimeZone.getDefault())
        return sdf.format(date)
    }
}