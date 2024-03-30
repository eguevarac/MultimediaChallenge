package com.example.multimediachallenge.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.multimediachallenge.databinding.FragmentMainBinding
import com.example.multimediachallenge.utils.Dialogs
import com.example.multimediachallenge.utils.StorageManager
import com.example.multimediachallenge.utils.TypeOfTextFragment


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var isCameraToVideo = false

    private val contractCameraForPictures: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { userClickSave ->
            if (userClickSave) {
                Dialogs.nameToFileDialog(
                    requireContext(),
                    "Ponle nombre a la foto",
                    true
                )
            }
        }
    private val contractCameraForVideos: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { userClickSave ->
            if (userClickSave) {
                Dialogs.nameToFileDialog(
                    requireContext(),
                    "Ponle nombre al vídeo",
                    false
                )
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
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToRecorderFragment()
                )
            }
            btnCaptureImg.setOnClickListener {
                isCameraToVideo = false
                Log.i(">", "Va a entrar en la función")
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
            btnVisualizationSound.setOnClickListener { }
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
            btnEditionSound.setOnClickListener { }
            btnEditionImg.setOnClickListener { }
            btnEditionVideo.setOnClickListener { }

            btnWhatsApp.setOnClickListener { }
            btnMaps.setOnClickListener { }
            btnChrome.setOnClickListener { }
        }
    }

    private fun checkCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            launchPictureOrVideoContract()
        }
    }

    private fun launchPictureOrVideoContract() {
        if (!isCameraToVideo) {
            contractCameraForPictures.launch(StorageManager.createAuxUri(requireContext()))
            /*CameraManager.startContract(
                requireContext(),
                contractCameraForPictures
            )*/
        } else {
            contractCameraForVideos.launch(StorageManager.createAuxUri(requireContext()))
            /*CameraManager.startContract(
                requireContext(),
                contractCameraForVideos
            )*/
        }
    }
}