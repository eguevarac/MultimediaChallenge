package com.example.multimediachallenge.ui

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.multimediachallenge.R
import com.example.multimediachallenge.databinding.FragmentTextBinding
import com.example.multimediachallenge.utils.StorageManager
import com.example.multimediachallenge.utils.TypeOfTextFragment

class TextFragment : Fragment() {

    private lateinit var binding: FragmentTextBinding
    private val args: TextFragmentArgs by navArgs()

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
        with(binding) {

            if (args.typeOfTextFragment == TypeOfTextFragment.TextCreation) {
                btnFindTxt.visibility = View.GONE
                root.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.background_card_capture)

            } else {
                etTitle.isEnabled = false
                root.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.background_card_edition)
            }

            btnAccept.setOnClickListener {
                StorageManager.addTextFileToStorage(
                    requireContext(),
                    etTitle.text.toString(),
                    etBody.text.toString()
                )
            }
            chbBold.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
            chbItalic.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
        }
    }

    private fun setTextWithConditions() {
        val originText = binding.etBody.text.toString().replace("\n", "<br>")
        var spanned: Spanned? = null
        var textToShow: String? = null

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

        /*Log.i(">", "el textToShow ->" + textToShow)
        Log.i(">", "el spaned ->" + spanned)*/
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