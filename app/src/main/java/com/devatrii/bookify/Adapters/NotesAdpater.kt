package com.devatrii.bookify.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.bookify.databinding.ItemNotesBinding
import com.devatrii.bookify.entities.NotesEntity

class NotesAdpater(private var list: MutableList<NotesEntity>, var context: Context) :
    RecyclerView.Adapter<NotesAdpater.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: NotesEntity, context: Context) {
            binding.apply {
                mNote.text = model.note


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotesBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model = list[position], context = context)
    }
}
