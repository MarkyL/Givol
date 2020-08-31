package com.givol.utils

import android.text.format.DateFormat
import timber.log.Timber
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {

    val LONG_DATE_PATTERN = "dd/MM/yyyy"
    val ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'"
    val SHORT_TIME_PATTERN = "HH:mm"
    val DATE_TIME_PATTERN = "dd/MM/yy HH:mm"
    val DATE_TIME_SECONDS_PATTERN = "dd/MM/yy HH:mm:ss"


    fun getIsoDate(date: Long): String {
        val df = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
        return df.format(date)
    }

    fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(DATE_TIME_PATTERN,calendar).toString()
    }

    fun getDateWithSeconds(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        return DateFormat.format(DATE_TIME_SECONDS_PATTERN,calendar).toString()
    }

    fun getDateFormat(date: String): Date? {
        return try {
            val df = SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault())
            df.parse(date)
        } catch (e: ParseException) {
            Timber.e("unable to parse date $date")
            null
        }
    }

}