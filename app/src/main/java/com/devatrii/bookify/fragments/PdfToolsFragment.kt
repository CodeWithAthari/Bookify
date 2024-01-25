package com.devatrii.bookify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.devatrii.bookify.Adapters.ToolsAdapter
import com.devatrii.bookify.Models.ToolsModel
import com.devatrii.bookify.PdfActivity
import com.devatrii.bookify.R
import com.devatrii.bookify.ViewModels.ToolsViewModel
import com.devatrii.bookify.ViewModels.ToolsViewModelFactory
import com.devatrii.bookify.databinding.FragmentPdfToolsBinding
import com.devatrii.bookify.enums.ToolsType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PdfToolsFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentPdfToolsBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        val mActivity = requireActivity() as PdfActivity
        ViewModelProvider(mActivity, ToolsViewModelFactory(mActivity))[ToolsViewModel::class.java]
    }
    private val list: MutableList<ToolsModel> = mutableListOf()
    private val adapter by lazy {
        ToolsAdapter(list = list,viewModel=viewModel,fragment=this, context = requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvinit()
    }

    private fun rvinit() {
        binding.mToolsRV.adapter = adapter
        // let's add tools

        list.apply {
            add(ToolsModel("Bookmark Me", R.drawable.ic_bookmarks, ToolsType.ADD_TO_BOOKMARKS))
            add(ToolsModel("My Bookmarks", R.drawable.ic_all_bookmarks, ToolsType.BOOKMARKS))
            add(ToolsModel("My Notes", R.drawable.ic_note, ToolsType.NOTES))
            add(ToolsModel("Night Mode", R.drawable.ic_night_mode, ToolsType.NIGHT_MODE))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}



















