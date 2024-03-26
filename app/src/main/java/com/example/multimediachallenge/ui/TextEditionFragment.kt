package com.example.multimediachallenge.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.chbBold.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val originText = binding.etBody.text.toString().replace("\n", "<br>")
                Log.i(">", "MUESTRA TEXT_ORIGIN")
                Log.i(">", originText)
                val boldText: String = "<b>" + originText + "</b>"
                Log.i(">", "MUESTRA TEXT_BOLD")
                Log.i(">", boldText)

                /*boldText.replace("\n", "<br>")
                Log.i(">", "MUESTRA TEXT_BOLD DESPUÉS DEL REPLACE")
                Log.i(">", boldText)*/

                val spanned = Html.fromHtml(boldText)
                Log.i(">", "MUESTRA SPANNED")
                Log.i(">", spanned.toString())
                binding.etBody.setText(spanned)
                /*if(binding.chbItalic.isChecked){

                }else{

                } */
            } else {
                val originText = binding.etBody.text.toString().replace("\n", "<br>")
                val spanned = Html.fromHtml(originText)
                //originText.replace("\n", "<br>")
                binding.etBody.setText(spanned)
                /*if(binding.chbItalic.isChecked){

                }else{

                }*/
            }

        }
    }

    private fun setupSpinner() {
        val spinnerOptions = listOf("8", "10", "12", "14", "16", "18", "20", "22")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
    }
}