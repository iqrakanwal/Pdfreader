package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces

import com.google.android.gms.ads.interstitial.InterstitialAd

interface AdsCallbackMethods {
    fun onAdLoaded()
    fun onInterstitalLoaded(interstitialAd: InterstitialAd)
    fun onAdFailed()
    fun onDismissed()




}