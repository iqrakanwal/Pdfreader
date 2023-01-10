package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfImportedPage
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfWriter
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments.HomeFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments.ToolFragment
import kotlinx.android.synthetic.main.activity_main_screen2.*
import java.io.InputStream
import java.io.OutputStream


class MainScreen : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen2)
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView?.setOnNavigationItemSelectedListener(this);    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.files -> {
                fragment = HomeFragment()
                toolbar.title = "Files"



            }
            R.id.tools -> {
                fragment = ToolFragment()
                toolbar.title = "Tools"


            }
        }
        if (fragment != null) {
            loadFragment(fragment)
        }
        return true    }

    fun loadFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction().replace(R.id.relativelayout, fragment!!).commit()
    }

    @Throws(Exception::class)
    fun splitPdfFile(
        inputPdf: InputStream?,
        outputStream: OutputStream, startPage: Int,
        endPage: Int
    ) {
        //Create document and pdfReader objects.
        var startPage = startPage
        val document = Document()
        val pdfReader = PdfReader(inputPdf)

        //Get total no. of pages in the pdf file.
        val totalPages: Int = pdfReader.numberOfPages

        //Check the startPage should not be greater than the endPage
        //and endPage should not be greater than total no. of pages.
        if (startPage > endPage || endPage > totalPages) {
            println(
                "Kindly pass the valid values " +
                        "for startPage and endPage."
            )
        } else {
            // Create writer for the outputStream
            val writer = PdfWriter.getInstance(document, outputStream)

            //Open document
            document.open()

            //Contain the pdf data.
            val pdfContentByte = writer.directContent
            var page: PdfImportedPage?
            while (startPage <= endPage) {
                document.newPage()
                page = writer.getImportedPage(pdfReader, startPage)
                pdfContentByte.addTemplate(page, 0f, 0f)
                startPage++
            }

            //Close document and outputStream.
            outputStream.flush()
            document.close()
            outputStream.close()
        }
    }
}