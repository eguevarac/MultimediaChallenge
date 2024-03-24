package com.example.multimediachallenge

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.multimediachallenge.databinding.FragmentMainBinding
import java.io.File


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding
    private lateinit var imgUri: Uri
    private lateinit var contractCamera: ActivityResultLauncher<Uri>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContract()

        with(binding){
            btnCaptureText.setOnClickListener {  }
            btnCaptureSound.setOnClickListener {  }
            btnCaptureImg.setOnClickListener { takePicture() }
            btnCaptureVideo.setOnClickListener {  }

            btnVisualizationText.setOnClickListener {  }
            btnVisualizationSound.setOnClickListener {  }
            btnVisualizationImg.setOnClickListener {  }
            btnVisualizationVideo.setOnClickListener {  }

            btnEditionText.setOnClickListener {  }
            btnEditionSound.setOnClickListener {  }
            btnEditionImg.setOnClickListener {  }
            btnEditionVideo.setOnClickListener {  }

            btnWhatsApp.setOnClickListener {  }
            btnMaps.setOnClickListener {  }
            btnChrome.setOnClickListener {  }
        }
    }

    private fun takePicture() {
        imgUri = createImgUri()
        contractCamera.launch(imgUri)
    }

    private fun createImgUri(): Uri {
        val img = File(requireContext().filesDir, "CameraImg.png")
        Log.i(">", requireContext().filesDir.toString())

        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.multimediachallenge.MainFragment.FileProvider",
            img
        )
    }

    private fun setContract() {
        contractCamera =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { userClickSave ->
                if (userClickSave) {

                    showTextInputDialog(requireContext())
                }
            }
    }

    private fun showTextInputDialog(context: Context) {
        val editText = EditText(context)
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Ponle nombre a la foto")
            .setView(editText)
            .setPositiveButton("Aceptar") { dialog, _ ->
                // TODO: añadir lo que hace en caso de guardar
                //addImageToGallery()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }


    fun saveFile(context: Context, imgHighUri: Uri, imgName: String) {

        val file = File(context.filesDir, "/img/" +imgName)

        val bytes = context.contentResolver.openInputStream(imgHighUri)?.readBytes()

        file.writeBytes(bytes!!)
    }
}