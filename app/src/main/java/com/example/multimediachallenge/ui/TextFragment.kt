package com.example.multimediachallenge.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.multimediachallenge.R
import com.example.multimediachallenge.databinding.FragmentTextBinding
import com.example.multimediachallenge.utils.StorageManager
import com.example.multimediachallenge.utils.TypeOfTextFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class TextFragment : Fragment() {

    private lateinit var binding: FragmentTextBinding
    private val args: TextFragmentArgs by navArgs()
    private var textToShow = ""

    private val contractGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val body = readTextFileContentFromUri(requireContext(), uri)
                if (body != null) {
                    val spanned = HtmlCompat.fromHtml(body, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.etBody.setText(spanned)
                }
                val fileName = getFileNameFromUri(requireContext(), uri)
                if (fileName != null) {
                    binding.etTitle.setText(fileName)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTypeOfFragment()

        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            btnAccept.setOnClickListener {
                checkIfPossibleAndSave()
            }
            chbBold.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
            chbItalic.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
            btnFindTxt.setOnClickListener {
                contractGallery.launch("text/plain")
            }
        }
    }

    private fun setupTypeOfFragment() {
        when (args.typeOfTextFragment) {
            TypeOfTextFragment.TextCreation -> setupTextCreationOrEditionFragment()
            TypeOfTextFragment.TextEdition -> setupTextCreationOrEditionFragment()
            TypeOfTextFragment.TextReader -> setupTextReaderFragment()
        }
    }

    private fun setupTextReaderFragment() {
        with(binding) {
            root.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.background_card_visualization
            )
            btnAccept.visibility = View.GONE
            chbBold.visibility = View.GONE
            chbItalic.visibility = View.GONE
            tvSize.visibility = View.GONE
            etTitle.isEnabled = false
            etBody.isEnabled = false
        }
    }

    private fun setupTextCreationOrEditionFragment() {
        setupSpinner()

        if (args.typeOfTextFragment == TypeOfTextFragment.TextCreation) {
            binding.btnFindTxt.visibility = View.GONE
            binding.root.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_card_capture)

        } else {
            binding.etTitle.isEnabled = false
            binding.root.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.background_card_edition)
        }


    }

    private fun checkIfPossibleAndSave() {
        if (!binding.etTitle.text.isNullOrBlank() && !binding.etBody.text.isNullOrBlank()) {
            if (textToShow == "") {
                textToShow = binding.etBody.text.toString().replace("\n", "<br>")
            }
            StorageManager.addTextFileToStorage(
                requireContext(),
                binding.etTitle.text.toString(),
                textToShow
            )
        } else {
            Toast.makeText(
                requireContext(),
                "Ni el título ni el cuerpo pueden estar vacíos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setTextWithConditions() {
        val originText = binding.etBody.text.toString().replace("\n", "<br>")
        var spanned: Spanned? = null

        if (binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            textToShow = "<b>$originText</b>"
            //textToShow = setFontSize(textToShow)
            //spanned = Html.fromHtml(textToShow)
            spanned = HtmlCompat.fromHtml(textToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

        } else if (binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em><b>$originText</b></em>"
            //textToShow = setFontSize(textToShow)
            //spanned = Html.fromHtml(textToShow)
            spanned = HtmlCompat.fromHtml(textToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

        } else if (!binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em>$originText</em>"
            //textToShow = setFontSize(textToShow)
            //spanned = Html.fromHtml(textToShow)
            spanned = HtmlCompat.fromHtml(textToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

        } else if (!binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            //textToShow = setFontSize(originText)
            //spanned = Html.fromHtml(originText)
            spanned = HtmlCompat.fromHtml(originText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        binding.etBody.setText(spanned)
        setFontSize()

        Log.i(">", "el textToShow ->" + textToShow)
        Log.i(">", "el spaned ->" + spanned)
    }

    private fun setFontSize() {
        with(binding) {
            when (spinner.selectedItem) {
                "14" -> etBody.textSize = 14f
                "16" -> etBody.textSize = 16f
                "18" -> etBody.textSize = 18f
                "20" -> etBody.textSize = 20f
                "22" -> etBody.textSize = 22f
                "24" -> etBody.textSize = 24f
            }
        }
    }

    private fun setupSpinner() {
        val spinnerOptions = listOf("14", "16", "18", "20", "22", "24")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setTextWithConditions()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setTextWithConditions()
            }
        }
    }

    private fun readTextFileContentFromUri(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val stringBuilder = StringBuilder()

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return stringBuilder.toString()
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val contentResolver = context.contentResolver

        // Query para obtener el nombre del archivo a partir de su URI
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use { c ->
            if (c.moveToFirst()) {
                val displayNameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = c.getString(displayNameIndex)
                }
            }
        }

        return fileName
    }
}

/*private fun setFontSize(textToResize: String): String {
        var textToShow: String?
        when (binding.spinner.selectedItem) {
            "h1" -> {
                textToShow = "<h1>" + textToResize + "</h1>"
            }

            "h2" -> {
                textToShow = "<h2>" + textToResize + "</h2>"
            }

            "h3" -> {
                textToShow = "<h3>" + textToResize + "</h3>"
            }

            "h4" -> {
                textToShow = "<h4>" + textToResize + "</h4>"
            }

            "h5" -> {
                textToShow = "<h5>" + textToResize + "</h5>"
            }

            else -> {
                textToShow = textToResize
            }
        }
        return textToShow
    }*/