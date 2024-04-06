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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multimediachallenge.data.Constants
import com.example.multimediachallenge.databinding.FragmentImgEditorBinding
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditor.Builder
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.coroutines.launch
import java.io.File


class ImgEditorFragment : Fragment(), FiltersAdapter.FilterListener {

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
        setupAdapter()

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
                    binding.recyclerView.visibility = View.VISIBLE
                } else {
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }
    }


    private fun setupAdapter() {
        val adapter = FiltersAdapter(Constants.filters, this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
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

    override fun onFilterClick(filterName: String) {
        when (filterName) {
            "cancel" -> photoEditor.setFilterEffect(PhotoFilter.NONE)
            "autoFix" -> photoEditor.setFilterEffect(PhotoFilter.AUTO_FIX)
            "blackWhite" -> photoEditor.setFilterEffect(PhotoFilter.BLACK_WHITE)
            "brightness" -> photoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS)
            "contrast" -> photoEditor.setFilterEffect(PhotoFilter.CONTRAST)
            "crossProcess" -> photoEditor.setFilterEffect(PhotoFilter.CROSS_PROCESS)
            "documentary" -> photoEditor.setFilterEffect(PhotoFilter.DOCUMENTARY)
            "fillLight" -> photoEditor.setFilterEffect(PhotoFilter.FILL_LIGHT)
            "fishEye" -> photoEditor.setFilterEffect(PhotoFilter.FISH_EYE)
            "grain" -> photoEditor.setFilterEffect(PhotoFilter.GRAIN)
            "grayScale" -> photoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE)
            "lomish" -> photoEditor.setFilterEffect(PhotoFilter.LOMISH)
            "negative" -> photoEditor.setFilterEffect(PhotoFilter.NEGATIVE)
            "posterize" -> photoEditor.setFilterEffect(PhotoFilter.POSTERIZE)
            "saturate" -> photoEditor.setFilterEffect(PhotoFilter.SATURATE)
            "sepia" -> photoEditor.setFilterEffect(PhotoFilter.SEPIA)
            "sharpen" -> photoEditor.setFilterEffect(PhotoFilter.SHARPEN)
            "temperature" -> photoEditor.setFilterEffect(PhotoFilter.TEMPERATURE)
            "tint" -> photoEditor.setFilterEffect(PhotoFilter.TINT)
            "vignette" -> photoEditor.setFilterEffect(PhotoFilter.VIGNETTE)
        }
    }
}