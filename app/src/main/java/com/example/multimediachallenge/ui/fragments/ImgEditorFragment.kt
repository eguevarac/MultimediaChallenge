package com.example.multimediachallenge.ui.fragments

import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.multimediachallenge.databinding.FragmentImgEditorBinding
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditor.Builder
import kotlinx.coroutines.launch
import java.io.File


class ImgEditorFragment : Fragment() {

    private lateinit var binding: FragmentImgEditorBinding
    private lateinit var photoEditor: PhotoEditor
    private val args: ImgEditorFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentImgEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPhotoEditor()

        with(binding) {
            photoEditorView.source.setImageURI(args.imgToEdit.uri)
            btnSave.setOnClickListener { saveImg() }
            btnUndo.setOnClickListener { photoEditor.undo() }
            btnRedo.setOnClickListener { photoEditor.redo() }

            chbEraser.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    photoEditor.brushEraser()
                }
            }

            chbBrush.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    photoEditor.setBrushDrawingMode(true)
                    chbEraser.isChecked = false
                } else {
                    photoEditor.setBrushDrawingMode(false)
                    chbEraser.isChecked = false
                }
            }

            chbFilter.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showFilters()
                } else {
                    // TODO: hideFilters
                }
            }
        }
    }

    private fun showFilters() {
        // TODO: sólo queda el filtro y santas pascuas
        /*photoEditor.apply {
        setFilterEffect(PhotoFilter.VIGNETTE)
    }*/
    }

    private fun saveImg() {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val timestamp = System.currentTimeMillis()
        val fileName = "$timestamp.jpg"
        val filePath = File(directory, fileName)
        Log.i(">", "dirección ->   $filePath")

        viewLifecycleOwner.lifecycleScope.launch {
            photoEditor.saveAsFile(filePath.absolutePath, object : PhotoEditor.OnSaveListener {
                override fun onSuccess(imagePath: String) {
                    // Escanear el archivo para que aparezca en la galería
                    refreshGallery(filePath.absolutePath)
                }

                override fun onFailure(exception: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "No se ha podido guardar la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun refreshGallery(filePath: String) {
        MediaScannerConnection.scanFile(
            requireContext(),
            arrayOf(filePath),
            arrayOf("image/jpeg"),
            null
        )

        Toast.makeText(requireContext(), "La imagen se ha guardado con éxito!", Toast.LENGTH_SHORT)
            .show()
    }

    private fun setupPhotoEditor() {

        val defaultTypeface: Typeface = Typeface.DEFAULT

        photoEditor = Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .setDefaultTextTypeface(defaultTypeface)
            .build()
    }
}