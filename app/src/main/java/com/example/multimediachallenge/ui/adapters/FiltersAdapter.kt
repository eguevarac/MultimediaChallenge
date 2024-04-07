package com.example.multimediachallenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediachallenge.R
import com.example.multimediachallenge.data.FilterImg
import com.example.multimediachallenge.databinding.ItemFilterBinding

class FiltersAdapter(private val filters: List<FilterImg>, private val listener: FilterListener) :
    RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {


    interface FilterListener {
        fun onFilterClick(filterName: String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemFilterBinding.bind(view)
        fun setupListener(filterName: String) {
            binding.root.setOnClickListener { listener.onFilterClick(filterName) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filters.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            imgFilter.setImageResource(filters[position].img)
            tvNameFilter.text = filters[position].name
        }
        holder.setupListener(filters[position].name)
    }
}