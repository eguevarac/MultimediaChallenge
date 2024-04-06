package com.example.multimediachallenge.ui.fragments

import android.Manifest
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaController: MediaController
    private var isPlayingSound = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFind.setOnClickListener {
            checkReadMediaAudioPermission()
        }
        binding.vMediaController.setOnClickListener {
            if (isPlayingSound) {
                mediaController.show()
            }
        }
    }


    //Permissions -----------------------------------------------------------------
    private fun checkReadMediaAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestReadMediaAudioPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                requestReadMediaAudioPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            contractAudioStorage.launch("audio/*")
        }
    }

    private val requestReadMediaAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contractAudioStorage.launch("audio/*")
            } else {
                Toast.makeText(requireContext(), "No has aceptado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    //Contracts -----------------------------------------------------------------
    private val contractAudioStorage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                try {
                    loadAudio(uri)
                    isPlayingSound = true

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Ha habido un problema al intentar reproducir el audio",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    private fun loadAudio(uri: Uri) {
        mediaPlayer = if (Build.VERSION.SDK_INT >= 34) {
            MediaPlayer(requireContext())
        } else {
            MediaPlayer()
        }
        mediaController = MediaController(requireContext())

        setupMediaController()

        mediaPlayer.setDataSource(requireContext(), uri)
        mediaPlayer.prepare()

        mediaController.show()
    }

    private fun setupMediaController() {
        mediaController.setMediaPlayer(object : MediaController.MediaPlayerControl {
            override fun start() {
                mediaPlayer.start()
            }

            override fun pause() {
                mediaPlayer.pause()
            }

            override fun getDuration() = mediaPlayer.duration
            override fun getCurrentPosition() = mediaPlayer.currentPosition
            override fun seekTo(pos: Int) {
                mediaPlayer.seekTo(pos)
            }

            override fun isPlaying() = mediaPlayer.isPlaying
            override fun getBufferPercentage() = 0
            override fun canPause(): Boolean = true
            override fun canSeekBackward() = true
            override fun canSeekForward() = true
            override fun getAudioSessionId() = mediaPlayer.audioSessionId
        })

        mediaController.setAnchorView(binding.vMediaController)
    }
}