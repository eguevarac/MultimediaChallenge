package com.example.multimediachallenge.ui

import android.Manifest
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.R
import com.example.multimediachallenge.databinding.FragmentRecorderBinding
import com.example.multimediachallenge.utils.StorageManager

class RecorderFragment : Fragment() {

    private lateinit var binding: FragmentRecorderBinding
    private val requestRecorderPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // TODO: aquí hay que hacer las cositas
                setupRecorder()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permisos denegados por el usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var recorder: MediaRecorder? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecorderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRec.setOnClickListener {
            checkRecorderPermission()
        }
    }

    private fun checkRecorderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRecorderPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            // TODO: aquí hay que hacer las cositas
            setupRecorder()
        }

    }

    private fun setupRecorder() {
        if (recorder == null) {
            initRecorder()
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
            recorder?.release()
            binding.btnRec.setImageResource(R.drawable.ic_record)
            Toast.makeText(requireContext(), "Se ha detenido la grabación", Toast.LENGTH_LONG)
                .show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun startRecording() {
        try {
            recorder!!.prepare()
            recorder!!.start()
            binding.btnRec.setImageResource(R.drawable.ic_stop)
            Toast.makeText(requireContext(), "Grabando...", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecorder() {
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(requireContext())
        } else {
            MediaRecorder()
        }
        recorder!!.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(StorageManager.createAuxUri(requireContext()).toString())
        }

    }
}