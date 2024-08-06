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
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun formatDateStringToLocalDateTime(date: String): LocalDateTime {
        val originalFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val originalLocalDate = LocalDate.parse(date, originalFormatter)
        return LocalDateTime.of(originalLocalDate, LocalTime.of(0, 0, 0))
    }
}