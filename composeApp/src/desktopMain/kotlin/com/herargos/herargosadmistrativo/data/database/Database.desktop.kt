package com.herargos.herargosadmistrativo.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.herargos.herargosadmistrativo.data.getAppFolder
import kotlinx.coroutines.Dispatchers
import java.io.File

const val APP_NAME = "PanOregano"

fun getDatabase(): AppDatabase {
    // Define la ruta completa para tu archivo de base de datos dentro de ese directorio
    val dbFile =
        File(getAppFolder(), DATABASE_NAME) // DATABASE_NAME es el nombre de tu archivo de DB

    // Configura y construye tu base de datos Room con la ruta absoluta
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}