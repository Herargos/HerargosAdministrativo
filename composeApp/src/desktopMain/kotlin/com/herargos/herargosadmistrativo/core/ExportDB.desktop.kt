package com.herargos.herargosadmistrativo.core

import com.herargos.herargosadmistrativo.data.database.DATABASE_NAME
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView


actual fun exportDatabase() {
    // Configura el JFileChooser
    val fileChooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
    fileChooser.dialogTitle = "Selecciona el directorio para exportar la base de datos"
    fileChooser.fileSelectionMode =
        JFileChooser.DIRECTORIES_ONLY // Solo permite seleccionar directorios
    fileChooser.isAcceptAllFileFilterUsed = false // No mostrar filtro de "Todos los archivos"

    // Muestra el diálogo y espera la selección del usuario
    val userSelection =
        fileChooser.showSaveDialog(null) // 'null' para que el diálogo sea modal y centrado en la pantalla

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        val selectedDirectory = fileChooser.selectedFile
        if (selectedDirectory != null) {
            // Llama a la función exportDatabase con el directorio seleccionado
            exportDatabaseNow(DATABASE_NAME, selectedDirectory)
        } else {
            println("No se seleccionó ningún directorio.")
        }
    } else {
        println("Exportación cancelada por el usuario.")
    }
}

fun exportDatabaseNow(databaseName: String, exportDirectory: File) {
    val tempDir = System.getProperty("java.io.tmpdir")
    val sourceFile = File(tempDir, databaseName)
    val destinationFile = File(exportDirectory, databaseName)

    if (sourceFile.exists()) {
        try {
            sourceFile.copyTo(destinationFile, overwrite = true)
            println("Database exported successfully to: ${destinationFile.absolutePath}")
        } catch (e: Exception) {
            println("Error exporting database: ${e.message}")
        }
    } else {
        println("Database file not found at: ${sourceFile.absolutePath}")
    }
}