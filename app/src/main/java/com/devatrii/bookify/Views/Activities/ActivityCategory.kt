package com.devatrii.bookify.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devatrii.bookify.Adapters.CategoryAdapter
import com.devatrii.bookify.Models.BooksModel
import com.devatrii.bookify.databinding.ActivityCategoryBinding

class ActivityCategory : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }
    private val list = ArrayList<BooksModel>()
    private val adapter = CategoryAdapter(list, activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            mRVCats.adapter = adapter
            val booksList = intent.getSerializableExtra("book_list") as ArrayList<BooksModel>
            booksList.forEach {
                list.add(it)
                adapter.notifyItemChanged(list.size)
            }
        }
    }
}