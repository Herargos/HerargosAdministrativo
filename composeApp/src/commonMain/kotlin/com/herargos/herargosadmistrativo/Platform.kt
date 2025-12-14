package com.herargos.herargosadmistrativo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform