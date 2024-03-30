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
import com.example.multimediachallenge.utils.CameraManager
import com.example.multimediachallenge.utils.TypeOfTextFragment


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var isCameraToVideo = false

    private val contractCameraForPictures: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { userClickSave ->
            if (userClickSave) {
                CameraManager.nameToFileDialog(requireContext(), "Ponle nombre a la foto", true)
            }
        }
    private val contractCameraForVideos: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { userClickSave ->
            if (userClickSave) {
                CameraManager.nameToFileDialog(requireContext(), "Ponle nombre al vídeo", false)
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

    /*private val recordAudioIntent = Intent().apply {
        action = "com.sec.android.app.voicenote.action.RECORD_NEW_SOUND"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    private val contractRecordAudio: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Aquí puedes manejar el resultado de la grabación de audio
                val data: Intent? = result.data
                // Procesar los datos si es necesario
            } else {
                // Si la acción se cancela o falla, aquí puedes manejarlo
            }
        }*/

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
                /*AudioRecordingManager.startAudioRecording(
                    requireContext(),
                    contractRecordAudio,
                    recordAudioIntent
                )*/
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
            CameraManager.startContract(
                requireContext(),
                contractCameraForPictures
            )
        } else {
            CameraManager.startContract(
                requireContext(),
                contractCameraForVideos
            )
        }
    }
}