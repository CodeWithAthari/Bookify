package com.devatrii.bookify

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.devatrii.bookify.Utils.loadBannerAd
import com.devatrii.bookify.databinding.ActivityPdfBinding
import com.devatrii.bookify.fragments.PdfToolsFragment
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

class PdfActivity : AppCompatActivity() {
    val activity = this
    lateinit var binding: ActivityPdfBinding


    lateinit var bookPdf: String
    lateinit var bookId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfBinding.inflate(layoutInflater)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupBasicViews()
        binding.apply {
            bookPdf = intent.getStringExtra("book_pdf").toString()
            bookId = intent.getStringExtra("book_id").toString()
            mBannerAd.loadBannerAd()

            pdfView.fromUri(bookPdf.toUri())
                .swipeHorizontal(false)
                .scrollHandle(DefaultScrollHandle(activity))
                .enableSwipe(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .load()
        }

    }

    private fun setupBasicViews() {
        binding.mToolsFab.setOnClickListener {
            val toolsBottomSheet = PdfToolsFragment()
            toolsBottomSheet.show(supportFragmentManager, toolsBottomSheet.tag)
        }

    }
}



















