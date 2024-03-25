package com.example.multimediachallenge

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.multimediachallenge.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    private val contractCameraForPictures: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { userClickSave ->
            if (userClickSave) {
                CameraManager.showNameToPictureDialog(requireContext(), "Ponle nombre a la foto", true)
            }
        }
    private val contractCameraForVideos: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.CaptureVideo()) { userClickSave ->
            if (userClickSave) {
                CameraManager.showNameToPictureDialog(requireContext(), "Ponle nombre al vídeo", false)
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


        with(binding){
            btnCaptureText.setOnClickListener {  }
            btnCaptureSound.setOnClickListener {  }
            btnCaptureImg.setOnClickListener { CameraManager.startContract(requireContext(), contractCameraForPictures) }
            btnCaptureVideo.setOnClickListener { CameraManager.startContract(requireContext(), contractCameraForVideos) }

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
}