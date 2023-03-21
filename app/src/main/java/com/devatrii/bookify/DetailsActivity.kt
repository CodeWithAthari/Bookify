package com.devatrii.bookify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devatrii.bookify.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    val activity = this
    lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bookTitle = intent.getStringExtra("book_title").toString()
        val bookDesc = intent.getStringExtra("book_desc").toString()
        val bookPDF = intent.getStringExtra("book_pdf").toString()
        val bookImage = intent.getIntExtra("book_image", 0)

        binding.apply {
            mBookTitle.text = bookTitle
            mBookDesc.text = bookDesc
            mBookImage.setImageResource(bookImage)

            mReadBookBtn.setOnClickListener {
                val intent = Intent()
                intent.putExtra("book_pdf", bookPDF)
                intent.setClass(activity, PdfActivity::class.java)
                startActivity(intent)
            }
        }

    }
}