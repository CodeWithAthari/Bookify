package com.devatrii.bookify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devatrii.bookify.Adapters.NotesAdpater
import com.devatrii.bookify.AppDatabase
import com.devatrii.bookify.PdfActivity
import com.devatrii.bookify.ViewModels.ToolsViewModel
import com.devatrii.bookify.ViewModels.ToolsViewModelFactory
import com.devatrii.bookify.databinding.FragmentNotesBinding
import com.devatrii.bookify.entities.NotesEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotesFragment : BottomSheetDialogFragment() {
    private val binding by lazy {
        FragmentNotesBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        val mActivity = requireActivity() as PdfActivity
        ViewModelProvider(mActivity, ToolsViewModelFactory(mActivity))[ToolsViewModel::class.java]
    }
    private val notesList: MutableList<NotesEntity> = mutableListOf()
    private val adapter by lazy {
        NotesAdpater(notesList, requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mNotesRv.adapter = adapter
            getData()
            addNote()
        }
    }

    private fun addNote() {
        binding.apply {
            btnAddNote.setOnClickListener {
                val note = edtNote.text.toString()
                if (note.isEmpty()) {
                    Toast.makeText(
                        requireActivity(),
                        "Please add note to continue",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.addNote(note)
                edtNote.text = null
                getData()


            }
        }
    }

    private fun getData() {
        val pdfActivity = requireActivity() as PdfActivity
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getDatabase(requireActivity())
            notesList.clear()
            database?.notesDao()?.getNotes(pdfActivity.bookId)?.forEach {
                notesList.add(it)
            }
            pdfActivity.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}




















