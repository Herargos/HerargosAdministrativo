package com.herargos.herargosadmistrativo.core.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class) // Esta anotación sigue siendo necesaria.
fun dateTimeNow(): String {
    val nowInstant = Clock.System.now()
    val nowLocalDateTime = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    return nowLocalDateTime.format(fullDateTimeFormatter)
}

// Formateador para la cadena de entrada: "yyyy-MM-dd HH:mm:ss"
private val inputFormatter = LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    day() // Usa dayOfMonth para el día del mes
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}

// Formateador para la cadena de salida: "dd/MM/yyyy hh:mm a"
private val outputFormatter = LocalDateTime.Format {
    day()
    char('/')
    monthNumber()
    char('/')
    year()
    char(' ')
    amPmHour()
    char(':')
    minute()
    char(':')
    second()
    char(' ')
    amPmMarker(am = "am", pm = "pm")
}

fun String.convertDateTimeFormat(): String {
    val localDateTime = inputFormatter.parse(this)
    return localDateTime.format(outputFormatter)
}

private val fullDateTimeFormatter = LocalDateTime.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    day()
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}

private val dateFormatter = LocalDate.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    day()
}

fun daysInMonth(month: Month, year: Int): Int {
    return when (month) {
        Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

@OptIn(ExperimentalTime::class)
fun getTodayDateFormatted(): String {
    return Clock.System.todayIn(TimeZone.currentSystemDefault()).format(dateFormatter)
}

@OptIn(ExperimentalTime::class)
fun getStartOfWeekFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysSinceMonday = today.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
    val monday = today.minus(daysSinceMonday, DateTimeUnit.DAY)
    val startOfDayMonday = LocalDateTime(monday.year, monday.month, monday.day, 0, 0, 0)
    return startOfDayMonday.format(fullDateTimeFormatter)
}

@OptIn(ExperimentalTime::class)
fun getEndOfWeekFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysUntilSunday = DayOfWeek.SUNDAY.isoDayNumber - today.dayOfWeek.isoDayNumber
    val sunday = today.plus(daysUntilSunday, DateTimeUnit.DAY)
    val endOfDaySunday = LocalDateTime(sunday.year, sunday.month, sunday.day, 23, 59, 59)
    return endOfDaySunday.format(fullDateTimeFormatter)
}

@OptIn(ExperimentalTime::class)
fun getStartOfMonthFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val firstDayOfMonth = LocalDate(today.year, today.month, 1)
    val startOfDayMonth = LocalDateTime(
        firstDayOfMonth.year, firstDayOfMonth.month,
        firstDayOfMonth.day, 0, 0, 0
    )
    return startOfDayMonth.format(fullDateTimeFormatter)
}

@OptIn(ExperimentalTime::class)
fun getEndOfMonthFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysInCurrentMonth = daysInMonth(today.month, today.year)
    val lastDayOfMonth = LocalDate(today.year, today.month, daysInCurrentMonth)
    val endOfDayMonth = LocalDateTime(
        lastDayOfMonth.year, lastDayOfMonth.month,
        lastDayOfMonth.day, 23, 59, 59
    )
    return endOfDayMonth.format(fullDateTimeFormatter)
}

@OptIn(ExperimentalTime::class)
fun getStartOfYearFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val firstDayOfYear = LocalDate(today.year, Month.JANUARY, 1)
    val startOfYear = LocalDateTime(
        firstDayOfYear.year, firstDayOfYear.month,
        firstDayOfYear.day, 0, 0, 0
    )
    return startOfYear.format(fullDateTimeFormatter)
}

@OptIn(ExperimentalTime::class)
fun getEndOfYearFormatted(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val lastDayOfYear = LocalDate(today.year, Month.DECEMBER, 31)
    val endOfYear = LocalDateTime(
        lastDayOfYear.year, lastDayOfYear.month,
        lastDayOfYear.day, 23, 59, 59
    )
    return endOfYear.format(fullDateTimeFormatter)
}