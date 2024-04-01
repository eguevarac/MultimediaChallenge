package com.example.multimediachallenge.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.example.multimediachallenge.utils.enums.TypeFile

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

    fun queryToWikipediaDialog(context: Context) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Qué quieres buscar en wikipedia?")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->
                ExternalAppsManager.lookingForInformationAtWikipedia(
                    context,
                    editText.text.toString()
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    fun queryToMapsDialog(context: Context) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Qué dirección quieres busacr en maps?")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->
                ExternalAppsManager.lookingForAddressAtMaps(context, editText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }
}