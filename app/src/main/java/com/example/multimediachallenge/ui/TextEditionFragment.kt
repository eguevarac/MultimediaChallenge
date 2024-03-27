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
import com.example.multimediachallenge.utils.TextManager


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

        binding.btnAccept.setOnClickListener {

            TextManager.addTextFileToStorage(
                requireContext(),
                binding.etTitle.text.toString(),
                binding.etBody.text.toString()
            )

            binding.etBody.setText(binding.etBody.text.toString())

        }
        binding.chbBold.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
        binding.chbItalic.setOnCheckedChangeListener { _, _ -> setTextWithConditions() }
    }

    private fun setTextWithConditions() {
        val originText = binding.etBody.text.toString().replace("\n", "<br>")
        var spanned: Spanned? = null
        var textToShow: String? = null

        if (binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            textToShow = "<b>$originText</b>"
            //textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em><b>$originText</b></em>"
            //textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (!binding.chbBold.isChecked && binding.chbItalic.isChecked) {

            textToShow = "<em>$originText</em>"
            //textToShow = setFontSize(textToShow)
            spanned = Html.fromHtml(textToShow)

        } else if (!binding.chbBold.isChecked && !binding.chbItalic.isChecked) {

            //textToShow = setFontSize(originText)
            spanned = Html.fromHtml(originText)
        }

        binding.etBody.setText(spanned)
        setFontSize()

        /*Log.i(">", "el textToShow ->" + textToShow)
        Log.i(">", "el spaned ->" + spanned)*/
    }

    private fun setFontSize() {

        when (binding.spinner.selectedItem) {
            "14" -> {
                binding.etBody.textSize = 14f
            }

            "16" -> {
                binding.etBody.textSize = 16f
            }

            "18" -> {
                binding.etBody.textSize = 18f
            }

            "20" -> {
                binding.etBody.textSize = 20f
            }

            "22" -> {
                binding.etBody.textSize = 22f
            }

            else -> {
                binding.etBody.textSize = 12f
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