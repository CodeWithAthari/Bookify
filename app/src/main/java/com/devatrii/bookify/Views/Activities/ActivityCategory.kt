package com.devatrii.bookify.Views.Activities

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.devatrii.bookify.Adapters.CategoryAdapter
import com.devatrii.bookify.Models.BooksModel
import com.devatrii.bookify.Utils.SpringScrollHelper
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
            SpringScrollHelper().apply {
                attachToRecyclerView(mRVCats)
            }
            val booksList = intent.getSerializableExtra("book_list") as ArrayList<BooksModel>
            val title = intent.getStringExtra("cat_title").toString()
            supportActionBar?.title = title
            booksList.forEach {
                list.add(it)
                adapter.notifyItemChanged(list.size)
            }
        }
    }

    override fun onBackPressed() {
        finish()
        with(window){
            sharedElementReturnTransition = null
            sharedElementReenterTransition = null
        }
        binding.mRVCats.transitionName = null
    }
}