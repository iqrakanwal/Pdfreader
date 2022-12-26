package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces

import android.net.Uri
import android.view.View

interface Onlongprocess {
    fun onLongClickAction(position: Int)
    fun onSingleAction(check: View?, positon: Int)
    fun delete()
    fun share()
    fun restore()
    fun play(uri: Uri?)
}