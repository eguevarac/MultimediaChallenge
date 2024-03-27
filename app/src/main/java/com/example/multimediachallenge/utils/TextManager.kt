package com.example.multimediachallenge.utils

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

object TextManager {

    fun addTextFileToStorage(context: Context, fileName: String, fileContent: String) {
        val content = createContentToTextFile(fileName)
        val finalUri = saveTextFile(context, content, fileContent, fileName)
        clearContents(context, content, finalUri)
        Toast.makeText(context, "Archivo de texto añadido con éxito", Toast.LENGTH_SHORT).show()
    }

    private fun createContentToTextFile(fileName: String): ContentValues {
        val fileType = "text/plain"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private fun saveTextFile(
        context: Context,
        content: ContentValues,
        fileContent: String,
        fileName: String
    ): Uri {
        var finalUri: Uri? = null
        var fileOutputStream: FileOutputStream? = null
        context.applicationContext.contentResolver.also { resolver ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                finalUri = resolver.insert(
                    MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL),
                    content
                )
                val outputStream = context.contentResolver.openOutputStream(finalUri!!)
                outputStream?.use { it.write(fileContent.toByteArray()) }
            } else {

                try {
                    val downloadsDirectory =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDirectory, fileName)
                    fileOutputStream = FileOutputStream(file)
                    fileOutputStream!!.write(fileContent.toByteArray())
                    // Escanear el archivo para que aparezca en la galería de medios
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(file.path),
                        arrayOf("text/plain")
                    ) { _, uri ->
                        finalUri = uri
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    fileOutputStream?.close()
                }
            }
        }
        return finalUri!!
    }


    private fun clearContents(context: Context, content: ContentValues, finalUri: Uri) {
        content.clear()
        content.put(MediaStore.MediaColumns.IS_PENDING, 0)
        context.applicationContext.contentResolver.update(finalUri, content, null, null)
    }
}