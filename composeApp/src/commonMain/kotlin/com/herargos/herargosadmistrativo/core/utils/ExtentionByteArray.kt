package com.herargos.herargosadmistrativo.core.utils

// Extensiones de ayuda para identificar el tipo
fun ByteArray.isJpeg() = size >= 3 && this[0].toUByte() == 0xFFu.toUByte() && this[1].toUByte() == 0xD8u.toUByte() && this[2].toUByte() == 0xFFu.toUByte()

fun ByteArray.isPng() = size >= 8 && this[0].toUByte() == 0x89u.toUByte() && this[1].toUByte() == 0x50u.toUByte() && this[2].toUByte() == 0x4Eu.toUByte() && this[3].toUByte() == 0x47u.toUByte()

fun ByteArray.isWebp() = size >= 12 && this[8].toUByte() == 0x57u.toUByte() && this[9].toUByte() == 0x45u.toUByte() && this[10].toUByte() == 0x42u.toUByte() && this[11].toUByte() == 0x50u.toUByte()