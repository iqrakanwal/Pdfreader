package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement

interface FbInterstitialOnCallBack {
    fun onInterstitialDisplayed()
    fun onInterstitialDismissed()
    fun onError()
    fun onAdLoaded()
    fun onAdClicked()
    fun onLoggingImpression()
}