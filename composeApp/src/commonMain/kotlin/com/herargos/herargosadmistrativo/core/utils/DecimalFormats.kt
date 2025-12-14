package com.herargos.herargosadmistrativo.core.utils

import java.text.DecimalFormat

fun Double.toStringRounded(): String {
    val format = DecimalFormat("0.00")
    return format.format(this)
}