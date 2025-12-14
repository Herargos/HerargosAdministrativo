package com.herargos.herargosadmistrativo.data

import java.io.File

const val APP_NAME = "PanOregano"

fun getAppFolder(): File {
    val userHome = System.getProperty("user.home")
    val appDataLocalDir = File(userHome, "AppData${File.separator}Local${File.separator}$APP_NAME")
    if (!appDataLocalDir.exists()) {
        appDataLocalDir.mkdirs()
    }
    return appDataLocalDir
}