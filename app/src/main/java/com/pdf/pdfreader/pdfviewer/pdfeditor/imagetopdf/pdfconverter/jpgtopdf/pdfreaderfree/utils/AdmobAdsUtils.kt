package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import java.util.*

class AdmobAdsUtils(context: Context) {
    private  var adaptiveAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private  var  adRequest: AdRequest
    private val mContext = context

    init {
        MobileAds.initialize(mContext) { it


        }

        adaptiveAdView = AdView(mContext)
        adRequest = AdRequest.Builder().build()

    }

    fun loadInterstitialAds(){

        InterstitialAd.load(mContext,mContext.resources.getString(R.string.Interstitial), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadInterstitialAgain()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
    }



    fun loadNativeAd(adView: NativeAdView, ad_frame:FrameLayout){
        val adLoader = AdLoader.Builder(mContext,mContext.resources.getString(R.string.Native))
            .forNativeAd { ad : NativeAd ->
                populateUnifiedNativeAdView(ad, adView)
                ad_frame.removeAllViews()
                ad_frame.addView(adView)            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build()).withAdListener(object : AdListener() {
                // AdListener callbacks can be overridden here.
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())

    }


    private fun loadInterstitialAgain(){

        InterstitialAd.load(mContext,mContext.resources.getString(R.string.Interstitial), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun showInterstitialAds(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(mContext as Activity)
        }
    }


    fun loadBannerAds(adContainer: LinearLayout){
        adContainer.addView(adaptiveAdView)
        adaptiveAdView.adUnitId = mContext.resources.getString(R.string.Banner)
            adaptiveAdView.setAdSize(AdSize.SMART_BANNER);


        val adRequest = AdRequest
            .Builder()
            .build()
        adaptiveAdView.loadAd(adRequest)
    }


    fun testMediation(){
//        MediationTestSuite.launch(mContext)
    }


    private fun getAdSize(adContainer:LinearLayout):AdSize{
        val display =  (mContext as Activity).windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, adWidth)

    }
    private fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
    //    adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
     //   adView.priceView = adView.findViewById(R.id.ad_price)
       // adView.starRatingView = adView.findViewById(R.id.ad_stars)
       // adView.storeView = adView.findViewById(R.id.ad_store)
    //    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
     //   nativeAd.mediaContent?.let { adView.mediaView?.setMediaContent(it) }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }

      /*  if (nativeAd.price == null) {
            adView.priceView?.visibility = View.INVISIBLE
        } else {
            adView.priceView?.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }*/

//        if (nativeAd.store == null) {
//            adView.storeView?.visibility = View.INVISIBLE
//        } else {
//            adView.storeView?.visibility = View.VISIBLE
//            (adView.storeView as TextView).text = nativeAd.store
//        }

  /*      if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            (adView.advertiserView as TextView).visibility = View.VISIBLE
        }*/

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        /*   val vc = nativeAd.videoController

           // Updates the UI to say whether or not this ad has a video asset.
           if (vc.hasVideoContent()) {
               videostatus_text.text = String.format(
                   Locale.getDefault(),
                   "Video status: Ad contains a %.2f:1 video asset.",
                   vc.aspectRatio
               )

               // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
               // VideoController will call methods on this object when events occur in the video
               // lifecycle.
               vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                   override fun onVideoEnd() {
                       // Publishers should allow native ads to complete video playback before
                       // refreshing or replacing them with another ad in the same UI location.
                       refresh_button.isEnabled = true
                       videostatus_text.text = "Video status: Video playback has ended."
                       super.onVideoEnd()
                   }
               }
           } else {
               videostatus_text.text = "Video status: Ad does not contain a video asset."
               refresh_button.isEnabled = true
           }*/
    }
}