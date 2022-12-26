package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import AdmobInterstitialAds
import InterstitialOnLoadCallBack
import InterstitialOnShowCallBack
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.FbInterstitialAds
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.FbInterstitialOnCallBack
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.MainActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.isInternetConnected
import kotlinx.android.synthetic.main.activity_get_started_screen.*

class GetStartedScreen : AppCompatActivity() {
    val MIN_CLICK_INTERVAL: Long = 600
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var admobInterstitialAds: AdmobInterstitialAds
    private lateinit var fbInterstitialAds: FbInterstitialAds
    var mLastClickTime: Long = 0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started_screen)



            adsConfig()


        getstarted.setOnClickListener {
            if (admobInterstitialAds.isInterstitialLoaded() || fbInterstitialAds.isInterstitialAdsLoaded() && isInternetConnected(
                   this
                )
            ) {
                showInterstitialAd()
            } else {
                nextActivity()
            }


            /* if (mInterstitialAd != null) {

             } else {
                 (activity as StartInfoActivity).nextActivity()
             }*/
        }
        Handler().postDelayed({
            getstarted.visibility = View.VISIBLE
            spin_kit.visibility= View.GONE
        }, 5000)
    }




    @RequiresApi(Build.VERSION_CODES.M)
    private fun adsConfig() {
        let { mActivity ->
            admobInterstitialAds = AdmobInterstitialAds(mActivity)
            fbInterstitialAds = FbInterstitialAds(mActivity)
            loadInterstitialAd()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadInterstitialAd() {
        let { mActivity ->
            Log.i("projjljlj", "${RemoteConfigConstants.priorityInterstitial}")
            when (RemoteConfigConstants.priorityInterstitial) {
                1 -> {
                    Log.d(GeneralUtils.AD_TAG, "Call FB Interstitial")
                    fbInterstitialAds.loadInterstitialAd(
                        mActivity.resources.getString(R.string.Interstitial),
                        RemoteConfigConstants.interstitialActive,
                        false,
                        GeneralUtils.isInternetConnected(mActivity),
                        object : FbInterstitialOnCallBack {
                            override fun onInterstitialDisplayed() {}


                            override fun onInterstitialDismissed() {
                                val currentClickTime = SystemClock.uptimeMillis()
                                val elapsedTime = currentClickTime - mLastClickTime
                                mLastClickTime = currentClickTime
                                if (elapsedTime <= MIN_CLICK_INTERVAL) return
                                //mInterstitialAd = null
                               nextActivity()

                            }

                            override fun onError() {}

                            override fun onAdLoaded() {}

                            override fun onAdClicked() {}

                            override fun onLoggingImpression() {}

                        })
                }
                2 -> {
                    Log.d(GeneralUtils.AD_TAG, "Call Admob Interstitial")
                    admobInterstitialAds.loadInterstitialAd(
                        mActivity.resources.getString(R.string.Interstitial),
                        RemoteConfigConstants.interstitialActive,
                        false,
                        GeneralUtils.isInternetConnected(mActivity),
                        object : InterstitialOnLoadCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                                Log.e("admondsad", "onAdFailedToLoad")
                            }

                            override fun onAdLoaded() {

                                Log.e("admondsad", "load")


                            }

                        })
                }
            }
        }
    }

    private fun showInterstitialAd() {
        let { mActivity ->
            Log.i("projjljlj", "${RemoteConfigConstants.priorityInterstitial}")
            when (RemoteConfigConstants.priorityInterstitial) {
                1 -> {
                    loading_adlayout.visibility =
                        View.VISIBLE
                    Handler().postDelayed({
                        loading_adlayout.visibility =
                            View.GONE
                        fbInterstitialAds.showInterstitialAds()
                    }, 800)
                }
                2 -> {
                    loading_adlayout.visibility =
                        View.VISIBLE
                    Handler().postDelayed({
                        loading_adlayout.visibility =
                            View.GONE
                        admobInterstitialAds.showInterstitialAd(object :
                            InterstitialOnShowCallBack {
                            override fun onAdDismissedFullScreenContent() {
                                val currentClickTime = SystemClock.uptimeMillis()
                                val elapsedTime = currentClickTime - mLastClickTime
                                mLastClickTime = currentClickTime
                                if (elapsedTime <= MIN_CLICK_INTERVAL) return
                                //mInterstitialAd = null
                              nextActivity()


                            }

                            override fun onAdFailedToShowFullScreenContent() {
                            }

                            override fun onAdShowedFullScreenContent() {
                                //mInterstitialAd = null
                            }

                            override fun onAdImpression() {
                            }

                        })
                    }, 800)


                }
                else -> {}
            }
        }

    }
    fun nextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}