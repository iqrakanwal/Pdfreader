package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces

import android.net.Uri
import java.net.URI

interface DeleteClickListener {
    fun onDeleteOption(uri: Uri)
    fun onCancel()
}