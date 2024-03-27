package com.example.multimediachallenge.ui

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.multimediachallenge.databinding.FragmentTextEditionBinding


class TextEditionFragment : Fragment() {

    private lateinit var binding: FragmentTextEditionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextEditionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        binding.chbBold.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
        binding.chbItalic.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }

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

    private fun setTextWithConditions() {
        val originText = binding.etBody.text.toString().replace("\n", "<br>")
        var spanned: Spanned? = null
        var textToShow: String?

        if (binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            textToShow = "<b>" + originText + "</b>"
            textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em><b>" + originText + "</b></em>"
            textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (!binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em>" + originText + "</em>"
            textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (!binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            textToShow = setFontSize(originText)
            spanned = Html.fromHtml(textToShow)
        }

        binding.etBody.setText(spanned)
        //binding.etBody.textSize = binding.spinner.selectedItem.toString().toFloat()
    }

    private fun setFontSize(textToResize: String): String {
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
    }


    private fun setupSpinner() {
        val spinnerOptions = listOf("Tamaño normal", "h5", "h4", "h3", "h2", "h1")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }
}