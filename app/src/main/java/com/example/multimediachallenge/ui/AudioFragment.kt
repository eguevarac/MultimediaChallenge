package com.example.multimediachallenge.ui

import android.content.Intent
import android.media.MediaPlayer
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
import com.example.multimediachallenge.utils.FixedMediaController

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaController: MediaController

    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                try {
                    mediaPlayer = if (Build.VERSION.SDK_INT >= 34) {
                        MediaPlayer(requireContext())
                    } else {
                        MediaPlayer()
                    }
                    mediaController = FixedMediaController(requireContext())
                    
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

                    mediaController.setAnchorView(binding.mediaController)

                    mediaPlayer.setDataSource(requireContext(), uri)
                    mediaPlayer.prepare()

                    mediaController.show()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Ha habido un problema al intentar reproducir el audio",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

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
            contractGallery.launch("audio/*")
        }

    }
}