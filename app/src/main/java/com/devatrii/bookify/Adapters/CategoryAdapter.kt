package com.devatrii.bookify.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.bookify.Views.Activities.DetailsActivity
import com.devatrii.bookify.Models.BooksModel
import com.devatrii.bookify.Utils.loadOnline
import com.devatrii.bookify.databinding.ItemCategoryBinding

class CategoryAdapter(val list: ArrayList<BooksModel>, val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ChildViewHolder>() {

    class ChildViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BooksModel, context: Context) {
            binding.apply {
                model.apply {
                    imageView.loadOnline(image)
                    mTitle.text = title
                    mAuthor.text = author
                    mDesc.text = description

                    mRootView.setOnClickListener {
                        // handle on click here
                        Intent().apply {
                            putExtra("book_model", model)
                            setClass(context, DetailsActivity::class.java)
                            context.startActivity(this)
                        }
                    }

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(list[position], context)
    }
}