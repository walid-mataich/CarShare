package com.example.frontend.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun formatConversationTime(raw: String): String {
    return try {
        // ISO format: 2025-12-15T14:49:03.067455Z
        val dateTime = OffsetDateTime.parse(raw)
            .atZoneSameInstant(ZoneId.systemDefault())

        val now = OffsetDateTime.now().atZoneSameInstant(ZoneId.systemDefault())

        when {
            ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate()) == 0L -> {
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            }
            ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate()) == 1L -> {
                "Yesterday"
            }
            else -> {
                dateTime.format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))
            }
        }
    } catch (e: Exception) {
        raw
    }
}
