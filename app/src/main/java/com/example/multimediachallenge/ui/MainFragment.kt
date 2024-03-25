package com.example.multimediachallenge.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.multimediachallenge.databinding.FragmentMainBinding
import com.example.multimediachallenge.utils.AudioRecordingManager
import com.example.multimediachallenge.utils.CameraManager


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

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

    private val recordAudioIntent = Intent().apply {
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
            btnCaptureText.setOnClickListener { findNavController().navigate(MainFragmentDirections.actionMainFragmentToTextEditionFragment()) }
            btnCaptureSound.setOnClickListener {
                AudioRecordingManager.startAudioRecording(
                    requireContext(),
                    contractRecordAudio,
                    recordAudioIntent
                )
            }
            btnCaptureImg.setOnClickListener {
                CameraManager.startContract(
                    requireContext(),
                    contractCameraForPictures
                )
            }
            btnCaptureVideo.setOnClickListener {
                CameraManager.startContract(
                    requireContext(),
                    contractCameraForVideos
                )
            }

            btnVisualizationText.setOnClickListener { }
            btnVisualizationSound.setOnClickListener { }
            btnVisualizationImg.setOnClickListener { }
            btnVisualizationVideo.setOnClickListener { }

            btnEditionText.setOnClickListener { }
            btnEditionSound.setOnClickListener { }
            btnEditionImg.setOnClickListener {  }
            btnEditionVideo.setOnClickListener {  }

            btnWhatsApp.setOnClickListener {  }
            btnMaps.setOnClickListener {  }
            btnChrome.setOnClickListener {  }
        }
    }
}