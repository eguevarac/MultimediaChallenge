package com.example.multimediachallenge

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.OutputStream

object CameraManager {

    private lateinit var imgUri: Uri

    fun takePicture(context: Context, contractCamera: ActivityResultLauncher<Uri>) {
        imgUri = createImgUri(context)
        contractCamera.launch(imgUri)
    }

    private fun createImgUri(context: Context): Uri {
        val img = File(context.filesDir, "CameraImg.png")
        Log.i(">", context.filesDir.toString())

        return FileProvider.getUriForFile(
            context,
            "com.example.multimediachallenge.MainFragment.FileProvider",
            img
        )
    }

    fun showNameToPictureDialog(context: Context) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Ponle nombre a la foto")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->

                addImageToGallery(context, editText.text.toString())

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun addImageToGallery(context: Context, fileName: String) {
        val content = createContent(fileName)
        val finalUri = save(context, content)
        clearContents(context, content, finalUri)
    }

    private fun clearContents(context: Context, content: ContentValues, finalUri: Uri) {
        content.clear()
        content.put(MediaStore.MediaColumns.IS_PENDING, 0)
        context.applicationContext.contentResolver.update(finalUri, content, null, null )
    }


    private fun createContent(fileName: String): ContentValues {
        val fileType = "image/jpeg"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)


        }
    }

    private fun save(context: Context, content: ContentValues): Uri {
        var outputStream : OutputStream?
        var finalUri: Uri?
        context.applicationContext.contentResolver.also {resolver ->
            finalUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
        outputStream = resolver.openOutputStream(finalUri!!)
        }
        outputStream.use {
            getBitmap(context).compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
        }
        return finalUri!!
    }

    private fun getBitmap(context: Context): Bitmap{
        return BitmapFactory.decodeStream(
            context
                .applicationContext
                .contentResolver
                .openInputStream(imgUri)
        )
    }
}