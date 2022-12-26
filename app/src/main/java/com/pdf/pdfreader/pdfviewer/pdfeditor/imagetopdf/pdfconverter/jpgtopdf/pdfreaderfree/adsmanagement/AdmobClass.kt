import android.annotation.SuppressLint
import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.AdsCallbackMethods
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.Adddtypes


class AdmobClass(var context: Context, var adsCallbackMethods: AdsCallbackMethods) {
    fun addBannerAds(adId: String, adView: AdView) {
        val adView = AdView(context)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = adId
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
            }

            override fun onAdOpened() {
            }

            override fun onAdClicked() {
            }

            override fun onAdClosed() {
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getNatAdd(
        b: Boolean,
        context: Context?,
        adView: NativeAdView,
        adFrame: FrameLayout?,
        mainViewModel: MainViewModel,
        adsid: String,
        adtype: Adddtypes
    ) {
        val builder = AdLoader.Builder(context, adsid)
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        var choice = NativeAdOptions.ADCHOICES_TOP_RIGHT
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setAdChoicesPlacement(choice)
            .build()

        val adLoader = builder.forNativeAd { unifiedNativeAd ->
            if (adtype == Adddtypes.HOME) {
                mainViewModel.setNativeAdHome(unifiedNativeAd)
                adsCallbackMethods.onAdLoaded()
             //   adFrame?.let { populateUnifiedNativeAdView(unifiedNativeAd, adView, it) }
            }
        }.withAdListener(
            object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdClicked() {

                }

                override fun onAdClosed() {
                    // adsCallbacks.onAdClosed(AdType.HONENATIVE, AdNetWorkType.ADMOB, isFail = false)
                }

                override fun onAdOpened() {
                    //adsCallbacks.onAdClicked(0)
                }
            }).withNativeAdOptions(adOptions)
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun getInterstitial(adId: String) {
        var mInterstitialAd: InterstitialAd? = null;
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adId, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    adsCallbackMethods.onInterstitalLoaded(mInterstitialAd!!)
                    mInterstitialAd?.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            adsCallbackMethods.onDismissed()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {


                        }

                        override fun onAdShowedFullScreenContent() {
                            mInterstitialAd = null
                        }
                    })


                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mInterstitialAd = null
                    adsCallbackMethods.onAdFailed()
                }
            })


    }


fun getfacebookInterstital(){






}






}