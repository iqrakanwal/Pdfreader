package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.media.SoundPool
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import kotlinx.android.synthetic.main.activity_showingclass.*
import kotlinx.coroutines.flow.first
import java.io.File

class ReadePdf : AppCompatActivity() {
    var file: File? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reade_pdf)
        val sports = intent.getStringExtra("Character")
        file = File(sports!!)
        uri = Uri.fromFile(file)

        pdfview.fromUri(uri)
            .defaultPage(1)
            .onPageChange { page, pageCount -> }
            .enableAnnotationRendering(true)
            .onLoad { }
            //  .scrollHandle(DefaultScrollHandle(this))
            .spacing(10)

            .onPageError { page, t ->




            }
            .swipeHorizontal(false)
            .load();
    }
}