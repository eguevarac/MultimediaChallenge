package com.example.multimediachallenge.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object Dialogs {

    fun nameToFileDialog(context: Context, title: String, isPicture: Boolean) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->
                if (isPicture) {
                    StorageManager.addImageToGallery(context, editText.text.toString())
                } else {
                    StorageManager.addVideoToStorage(context, editText.text.toString())
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