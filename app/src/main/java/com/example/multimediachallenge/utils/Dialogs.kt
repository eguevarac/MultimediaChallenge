package com.example.multimediachallenge.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object Dialogs {

    fun nameToFileDialog(context: Context, title: String, typeFile: TypeFile) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->
                if (typeFile == TypeFile.Picture) {
                    StorageManager.addImageToGallery(context, editText.text.toString())
                } else if (typeFile == TypeFile.Video) {
                    StorageManager.addVideoToStorage(context, editText.text.toString())
                } else if (typeFile == TypeFile.Audio) {
                    // TODO: tengo que hacer aquí lo de guardar el audio
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }
}