package com.example.meteo_app_tp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
object DateTimeUtils {
    private val hourFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())

    fun formatToHourOnly(dateTime: String): String {
        val parsed = LocalDateTime.parse(dateTime)
        return parsed.format(hourFormatter)
    }

    fun formatToDateOnly(dateTime: String): String {
        val parsed = LocalDateTime.parse(dateTime)
        return parsed.format(dateFormatter)
    }
}