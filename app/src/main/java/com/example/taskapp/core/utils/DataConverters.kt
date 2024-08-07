package com.example.taskapp.core.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DataConverters @Inject constructor() {
    fun convertMillisToDateString(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun formatTimeToString(hour: Int, minute: Int): String {
        return "${hour}:${minute}"
    }

    fun formatDateStringToLocalDateTime(date: String, time: String): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")


        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)


        return LocalDateTime.of(localDate, localTime)
    }
}