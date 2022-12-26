package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models

import androidx.annotation.Keep


@Keep
data class PdfModels(var filesName:String,
                     var fileSize:String,
                     var fileCreationDate:String,
                     var filePath:String,
                     var isSelected: Boolean = false


)
