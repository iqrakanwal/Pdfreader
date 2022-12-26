package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter

class Documentjgghg(): PrintDocumentAdapter() {
    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        p2: CancellationSignal?,
        p3: LayoutResultCallback?,
        p4: Bundle?
    ) {
    }

    override fun onWrite(
        p0: Array<out PageRange>?,
        p1: ParcelFileDescriptor?,
        p2: CancellationSignal?,
        p3: WriteResultCallback?
    ) {
    }
}