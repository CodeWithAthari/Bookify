package com.devatrii.bookify.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.bookify.ViewModels.ToolsViewModel
import com.devatrii.bookify.databinding.ItemBookmarksBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookmarksAdapter(
    private var list: MutableList<Int>,
    private var viewModel: ToolsViewModel, var fragment: BottomSheetDialogFragment,

    var context: Context
) :
    RecyclerView.Adapter<BookmarksAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBookmarksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pageNo: Int, context: Context) {
            binding.apply {
                mBtnBookmark.text = "${pageNo+1}"

                mBtnBookmark.setOnClickListener {
                    viewModel.jumpToPage(pageNo)
                    fragment.dismiss()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBookmarksBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pageNo = list[position], context = context)
    }
}
