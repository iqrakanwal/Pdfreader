package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement

import GeneralUtils.AD_TAG
import android.app.Activity
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

class FbInterstitialAds(activity: Activity) {
    var mInterstitialAd: InterstitialAd? = null
    private val mActivity: Activity = activity
    var isLoadingAd = false

    fun loadInterstitialAd(
        fbInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        fbInterstitialOnCallBack: FbInterstitialOnCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isRemoteConfigActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    fbInterstitialIds
                )
                mInterstitialAd?.let{
                    it.loadAd(
                        it.buildLoadAdConfig()
                            .withAdListener(object : InterstitialAdListener {
                                override fun onInterstitialDisplayed(ad: Ad) {
                                    // Interstitial ad displayed callback
                                    fbInterstitialOnCallBack.onInterstitialDisplayed()
                                    Log.i(AD_TAG, "FB Interstitial ad displayed.")
                                }

                                override fun onInterstitialDismissed(ad: Ad) {
                                    // Interstitial dismissed callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onInterstitialDismissed()
                                    Log.i(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd = null
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    mInterstitialAd = null
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onError()
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onAdLoaded()
                                    Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                }

                                override fun onAdClicked(ad: Ad) {
                                    // Ad clicked callback
                                    fbInterstitialOnCallBack.onAdClicked()
                                    Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                }

                                override fun onLoggingImpression(ad: Ad) {
                                    // Ad impression Logged callback
                                    fbInterstitialOnCallBack.onLoggingImpression()
                                    Log.d(AD_TAG, "FB Interstitial ad impression Logged!")
                                }
                            })
                            .build()
                    )
                }

            }
        }

    }

    fun loadShowLoadInterstitialAd(
        fbInterstitialIds: String,
        isRemoteConfigActive: Boolean,
        isAppPurchased: Boolean,
        isInternetConnected:Boolean,
        fbInterstitialOnCallBack: FbInterstitialOnCallBack
    ) {
        if (isInternetConnected && !isAppPurchased && isRemoteConfigActive) {
            if (mInterstitialAd == null && !isLoadingAd) {
                isLoadingAd = true
                mInterstitialAd = InterstitialAd(
                    mActivity,
                    fbInterstitialIds
                )
                mInterstitialAd?.let{
                    it.loadAd(
                        it.buildLoadAdConfig()
                            .withAdListener(object : InterstitialAdListener {
                                override fun onInterstitialDisplayed(ad: Ad) {
                                    // Interstitial ad displayed callback
                                    fbInterstitialOnCallBack.onInterstitialDisplayed()
                                    Log.i(AD_TAG, "FB Interstitial ad displayed.")
                                }

                                override fun onInterstitialDismissed(ad: Ad) {
                                    // Interstitial dismissed callback
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onInterstitialDismissed()
                                    Log.i(AD_TAG, "FB Interstitial ad dismissed.")
                                    mInterstitialAd?.loadAd()
                                }

                                override fun onError(ad: Ad, adError: AdError) {
                                    // Ad error callback
                                    mInterstitialAd = null
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onError()
                                    Log.e(AD_TAG, "FB Interstitial ad failed to load: " + adError.errorMessage)
                                }

                                override fun onAdLoaded(ad: Ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    isLoadingAd = false
                                    fbInterstitialOnCallBack.onAdLoaded()
                                    Log.d(AD_TAG, "FB Interstitial ad is loaded and ready to be displayed!")
                                }

                                override fun onAdClicked(ad: Ad) {
                                    // Ad clicked callback
                                    fbInterstitialOnCallBack.onAdClicked()
                                    Log.d(AD_TAG, "FB Interstitial ad clicked!")
                                }

                                override fun onLoggingImpression(ad: Ad) {
                                    // Ad impression Logged callback
                                    fbInterstitialOnCallBack.onLoggingImpression()
                                    Log.d(AD_TAG, "FB Interstitial ad impression Logged!")
                                }
                            })
                            .build()
                    )
                }

            }
        }

    }

    fun destroyInterstitialAds(){
        isLoadingAd = false
        mInterstitialAd?.destroy()
        mInterstitialAd = null
    }

    fun showInterstitialAds(){
        mInterstitialAd?.let {
            if (it.isAdLoaded){
                it.show()
            }
        }
    }

    fun isInterstitialAdsLoaded():Boolean{
        return mInterstitialAd?.isAdLoaded ?:false
    }


}