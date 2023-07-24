package com.devatrii.bookify.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.bookify.DetailsActivity
import com.devatrii.bookify.Models.BooksModel
import com.devatrii.bookify.Utils.loadOnline
import com.devatrii.bookify.databinding.ItemCategoryBinding

class CategoryAdapter(val list: ArrayList<BooksModel>, val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BooksModel, context: Context) {
        binding.apply {
            mBookImage.loadOnline(model.image)
            mBookTitle.text = model.title
            mAuthorName.text = model.author
            mBookDesc.text = model.description
            binding.root.setOnClickListener {
                Intent().apply {
                    putExtra("book_model",model)
                    setClass(context, DetailsActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        materialCardView,
                        materialCardView.transitionName
                    )
                    context.startActivity(this,options.toBundle())
                }
            }
        }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(
            model = list[position], context = context
        )
    }
}