package com.example.multimediachallenge.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object StorageManager {

    //Img storage -------------------------------------------------------------------
    fun addImageToGallery(context: Context, fileName: String) {
        val content = createContentToPicture(fileName)
        val finalUri = savePicture(context, content)
        clearContents(context, content, finalUri)
        Toast.makeText(
            context,
            "Imagen añadida a la galería con éxito",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun createContentToPicture(fileName: String): ContentValues {
        val fileType = "image/jpeg"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private fun savePicture(context: Context, content: ContentValues): Uri {
        var outputStream: OutputStream?
        var finalUri: Uri?
        context.applicationContext.contentResolver.also { resolver ->
            finalUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
            outputStream = resolver.openOutputStream(finalUri!!)
        }
        outputStream.use {
            getBitmap(context).compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
        }
        return finalUri!!
    }

    private fun getBitmap(context: Context): Bitmap {
        return BitmapFactory.decodeStream(
            context
                .applicationContext
                .contentResolver
                .openInputStream(createAuxUri(context))
        )
    }
    //Img storage -------------------------------------------------------------------

    //Video storage -------------------------------------------------------------------
    fun addVideoToStorage(context: Context, fileName: String) {
        val content = createContentToVideo(fileName)
        val finalUri = saveVideo(context, content)
        clearContents(context, content, finalUri)
        Toast.makeText(context, "Vídeo añadido a la galería con éxito", Toast.LENGTH_SHORT).show()
    }

    private fun createContentToVideo(fileName: String): ContentValues {
        val fileType = "video/mp4"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private fun saveVideo(context: Context, content: ContentValues): Uri {
        var finalUri: Uri?
        context.applicationContext.contentResolver.also { resolver ->
            finalUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content)
        }
        val sourceInputStream = context.contentResolver.openInputStream(createAuxUri(context))
        val destinationOutputStream = context.contentResolver.openOutputStream(finalUri!!)

        sourceInputStream?.use { input ->
            destinationOutputStream?.use { output ->
                input.copyTo(output)
            }
        }
        return finalUri!!
    }
    //Video storage -------------------------------------------------------------------

    //Text storage -------------------------------------------------------------------
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

                    if (file.exists()) {
                        file.delete()
                    }
                    fileOutputStream = FileOutputStream(file)
                    fileOutputStream!!.write(fileContent.toByteArray())

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
    //Text storage -------------------------------------------------------------------

    //Commons -------------------------------------------------------------------
    fun createAuxUri(context: Context): Uri {
        val file = File(context.filesDir, "auxFile")
        Log.i(">", context.filesDir.toString())

        return FileProvider.getUriForFile(
            context,
            "com.example.multimediachallenge.ui.MainFragment.FileProvider",
            file
        )
    }

    private fun clearContents(context: Context, content: ContentValues, finalUri: Uri) {
        content.clear()
        content.put(MediaStore.MediaColumns.IS_PENDING, 0)
        context.applicationContext.contentResolver.update(finalUri, content, null, null)
    }
}