package com.example.cointrack.util.extensions

import com.google.firebase.Timestamp
import timber.log.Timber
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime?.format(pattern: String): String {

    return try {

        this?.let {

            val formatter = DateTimeFormatter.ofPattern(pattern)
            formatter.format(it)

        } ?: ""

    } catch (e: DateTimeException) {

        e.printStackTrace()
        ""
    }
}

fun LocalDateTime?.toFirebaseTimestamp(): Timestamp? {

    return try {

        val instant = this?.atZone(ZoneId.systemDefault())?.toInstant()

        instant?.let {

            Timestamp(it.epochSecond, it.nano)
        }

    } catch (e: DateTimeException) {

        e.printStackTrace()
        null
    }

}

fun String?.toLocalDateTime(pattern: String): LocalDateTime? {

    return try {

        this?.let {

            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
            LocalDateTime.parse(this, formatter)

        }

    } catch (e: DateTimeException) {

        e.printStackTrace()
        null
    }
}

object DateTimeFormatters {

    const val FIREBASE_TIMESTAMP_FORMAT = "EEE MMM dd HH:mm:ss O yyyy"
    const val TRANSACTION_DATE_FORMAT = "dd/MM/yyyy"
    const val STATISTICS_DATE_FORMAT = "MMMM / yyyy"
}