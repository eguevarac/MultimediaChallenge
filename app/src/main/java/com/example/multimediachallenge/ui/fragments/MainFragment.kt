package com.example.multimediachallenge.ui.fragments

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.multimediachallenge.data.ImgToEdit
import com.example.multimediachallenge.databinding.FragmentMainBinding
import com.example.multimediachallenge.utils.Dialogs
import com.example.multimediachallenge.utils.StorageManager
import com.example.multimediachallenge.utils.enums.TypeFile
import com.example.multimediachallenge.utils.enums.TypeOfTextFragment


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var isCameraToVideo = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCaptureText.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToTextEditionFragment(
                        TypeOfTextFragment.TextCreation
                    )
                )
            }

            btnCaptureSound.setOnClickListener {
                checkRecorderPermission()
            }

            btnCaptureImg.setOnClickListener {
                isCameraToVideo = false
                checkCameraPermissions()
            }

            btnCaptureVideo.setOnClickListener {
                isCameraToVideo = true
                checkCameraPermissions()
            }

            btnVisualizationText.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToTextEditionFragment(
                        TypeOfTextFragment.TextReader
                    )
                )
            }

            btnVisualizationSound.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAudioFragment()
                )
            }

            btnVisualizationImg.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToImgFragment()
                )
            }

            btnVisualizationVideo.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToVideoFragment()
                )
            }

            btnEditionText.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToTextEditionFragment(
                        TypeOfTextFragment.TextEdition
                    )
                )
            }

            btnEditionSound.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "No se ha implementado esta funcionalidad",
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnEditionImg.setOnClickListener {
                isCameraToVideo = false
                checkReadMediaImagesPermission()
            }
            btnEditionVideo.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "No se ha implementado esta funcionalidad",
                    Toast.LENGTH_SHORT
                ).show()
            }

            btnWhatsApp.setOnClickListener {
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (intent != null) {
                    startActivity(intent)
                }
            }
            btnMaps.setOnClickListener {
                Dialogs.queryToMapsDialog(requireContext())
            }
            btnChrome.setOnClickListener {
                Dialogs.queryToWikipediaDialog(requireContext())
            }
        }
    }


    //Permissions -----------------------------------------------------------------
    private fun checkRecorderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRecorderPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            try {
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage("com.sec.android.app.voicenote")
                startActivity(intent!!)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "El dispositivo no dispone de la grabadora solicitada",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private val requestRecorderPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                try {
                    val intent =
                        requireContext().packageManager.getLaunchIntentForPackage("com.sec.android.app.voicenote")
                    startActivity(intent!!)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "El dispositivo no dispone de la grabadora solicitada",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun checkCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            launchPictureOrVideoContract()
        }
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchPictureOrVideoContract()
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun checkReadMediaImagesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestReadMediaImagesPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                requestReadMediaImagesPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            contractGallery.launch("image/*")
        }
    }

    private val requestReadMediaImagesPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contractGallery.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    //Contracts -----------------------------------------------------------------
    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                contractCropImage.launch(
                    CropImageContractOptions(
                        uri, CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            outputCompressFormat = Bitmap.CompressFormat.PNG
                        )
                    )
                )
            }
        }

    private val contractCropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToImgEditorFragment(
                    ImgToEdit(result.uriContent!!)
                )
            )
        }
    }

    private fun launchPictureOrVideoContract() {
        if (!isCameraToVideo) {
            contractCameraForPictures.launch(StorageManager.createAuxUri(requireContext()))
        } else {
            contractCameraForVideos.launch(StorageManager.createAuxUri(requireContext()))
        }
    }

    private val contractCameraForPictures: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { userClickSave ->
            if (userClickSave) {
                Dialogs.nameToFileDialog(
                    requireContext(),
                    "Ponle nombre a la foto",
                    TypeFile.Picture
                )
            }
        }

    private val contractCameraForVideos: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { userClickSave ->
            if (userClickSave) {
                Dialogs.nameToFileDialog(
                    requireContext(),
                    "Ponle nombre al vídeo",
                    TypeFile.Video
                )
            }
        }
}