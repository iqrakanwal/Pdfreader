package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.gms.ads.nativead.NativeAdView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.AdmobAdsUtils

class AdRepository(var admobAdsUtils: AdmobAdsUtils) {
    fun loadBannerAds(adContainer: LinearLayout) {
        admobAdsUtils.loadBannerAds(adContainer)
    }


    fun loadNativeAd(adView: NativeAdView, adFrkljlkame: FrameLayout) {
        admobAdsUtils.loadNativeAd(adView, adFrkljlkame)
    }







}