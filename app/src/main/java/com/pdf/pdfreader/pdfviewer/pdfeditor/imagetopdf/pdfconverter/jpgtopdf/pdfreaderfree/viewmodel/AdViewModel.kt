package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel

import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.nativead.NativeAdView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.AdRepository

class AdViewModel(var adRepository: AdRepository) : ViewModel() {

    fun loadBanner(adContainer: LinearLayout) {
        adRepository.loadBannerAds(adContainer)
    }
    fun loadNativeAd(adView: NativeAdView, adFrkljlkame: FrameLayout) {
        adRepository.loadNativeAd(adView, adFrkljlkame)
    }


}