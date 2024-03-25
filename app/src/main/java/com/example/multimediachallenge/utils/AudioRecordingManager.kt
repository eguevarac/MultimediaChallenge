package com.example.multimediachallenge.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher

object AudioRecordingManager {

    // Función para iniciar la grabadora de audio
    fun startAudioRecording(
        context: Context,
        contractRecordAudio: ActivityResultLauncher<Intent>,
        recordAudioIntent: Intent
    ) {
        if (recordAudioIntent.resolveActivity(context.packageManager) != null) {
            contractRecordAudio.launch(recordAudioIntent)
        } else {
            // Manejar el caso en el que no se encuentre una aplicación para grabar audio
            Toast.makeText(
                context,
                "El dispositivo no tiene aplicación para grabar",
                Toast.LENGTH_SHORT
            ).show()
            try {
                context.startActivity(recordAudioIntent)
            } catch (ex: Exception) {
                Toast.makeText(
                    context,
                    "El dispositivo no tiene aplicación para grabar",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
}