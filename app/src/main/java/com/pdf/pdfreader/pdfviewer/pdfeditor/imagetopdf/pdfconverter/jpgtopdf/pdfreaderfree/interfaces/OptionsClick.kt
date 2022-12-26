package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces

import android.net.Uri
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.FileFromPath
import java.net.URI

interface OptionsClick {
    fun onShareClick(uri: Uri)
    fun onDetails(uri: Uri, filename:String, fileFromPath: String,fileSize:String )
    fun onDelete(uri: Uri,position:Int)
    fun onOpen(uri: Uri)
    fun OnRenaming(filepath:String,position: Int,  name:String)
    fun onClick()
}