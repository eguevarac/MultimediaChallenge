package com.example.multimediachallenge.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentVideoBinding
import com.example.multimediachallenge.utils.FixedMediaController


class VideoFragment : Fragment() {

    private lateinit var mediaController: MediaController
    private lateinit var binding: FragmentVideoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFindVideo.setOnClickListener {
            checkReadMediaVideoPermission()
        }
    }

    //Permissions -----------------------------------------------------------------
    private fun checkReadMediaVideoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestReadMediaVideoPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
            } else {
                requestReadMediaVideoPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            contractGallery.launch("video/*")
        }
    }

    private val requestReadMediaVideoPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contractGallery.launch("video/*")
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    //Contracts -----------------------------------------------------------------
    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.video.setVideoURI(uri)
                mediaController = FixedMediaController(requireContext())
                mediaController.setMediaPlayer(binding.video)
                mediaController.show()
                binding.video.setMediaController(mediaController)
                binding.video.start()

            }
        }
}