package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import AdmobClass
import RemoteConfigConstants.IS_INTERSTITIAL_ACTIVE
import RemoteConfigConstants.IS_MEDIUM_NATIVE_ACTIVE
import RemoteConfigConstants.IS_SMALL_BANNER_ACTIVE
import RemoteConfigConstants.IS_SMALL_NATIVE_ACTIVE
import RemoteConfigConstants.PRIORITY_INTERSTITIAL
import RemoteConfigConstants.PRIORITY_MEDIUM_NATIVE
import RemoteConfigConstants.PRIORITY_SMALL_BANNER
import RemoteConfigConstants.PRIORITY_SMALL_NATIVE
import RemoteConfigConstants.interstitialActive
import RemoteConfigConstants.mediumNativeActive
import RemoteConfigConstants.priorityInterstitial
import RemoteConfigConstants.priorityMediumNative
import RemoteConfigConstants.prioritySmallBanner
import RemoteConfigConstants.prioritySmallNative
import RemoteConfigConstants.smallBannerActive
import RemoteConfigConstants.smallNativeActive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.facebook.ads.Ad
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.InterstitialAdListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.MainActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.ThemeHelper
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.AdsCallbackMethods
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashScreen : BaseActivity(), AdsCallbackMethods {
    private lateinit var handler: Handler
    private val model: MainViewModel by viewModel()
    private lateinit var remoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_splash_screen)
        handler = Handler(Looper.getMainLooper())
        ThemeHelper.applyTheme(model.isDarkTheme)
        if (isInternetConnected(this)) {
            initRemoteConfig()
        } else {
            handler.postDelayed({
                startMainActivity()
            }, 2000)
        }

    }


    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchRemoteValues()
    }

    private fun fetchRemoteValues() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            updateRemoteValues()
        }

    }




    private fun updateRemoteValues() {
        interstitialActive = remoteConfig[IS_INTERSTITIAL_ACTIVE].asBoolean()
        mediumNativeActive = remoteConfig[IS_MEDIUM_NATIVE_ACTIVE].asBoolean()
        smallNativeActive = remoteConfig[IS_SMALL_NATIVE_ACTIVE].asBoolean()
        smallBannerActive = remoteConfig[IS_SMALL_BANNER_ACTIVE].asBoolean()
        priorityInterstitial = remoteConfig[PRIORITY_INTERSTITIAL].asLong().toInt()
        priorityMediumNative = remoteConfig[PRIORITY_MEDIUM_NATIVE].asLong().toInt()
        prioritySmallNative = remoteConfig[PRIORITY_SMALL_NATIVE].asLong().toInt()
        prioritySmallBanner = remoteConfig[PRIORITY_SMALL_BANNER].asLong().toInt()
        Log.e("sdfjsldkf", "${interstitialActive}  ${priorityInterstitial}")
        // loadAds()


        handler.postDelayed({ startMainActivity() }, 5000)

    }

    private fun startMainActivity() {
        openActivity<GetStartedScreen>(true)


    }


    /* if (mInterstitialAd != null) {
         mInterstitialAd!!.show(this)
     } else {
         openActivity<MainActivity>(true)

     }*/


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onAdLoaded() {


    }

    override fun onInterstitalLoaded(interstitialAd: InterstitialAd) {
    }

    override fun onAdFailed() {
        startMainActivity()
    }

    override fun onDismissed() {

        openActivity<MainActivity>(true)

    }


    /*fun loadFacebookinterstital() {
        interstitialAd =
            com.facebook.ads.InterstitialAd(this, resources.getString(R.string.facebook_interstital))
        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                handler.removeCallbacksAndMessages(null)

                Log.e("fjgldfkgj", "Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {

                handler.removeCallbacksAndMessages(null)
                startMainActivity()
            }

            override fun onError(ad: Ad?, adError: AdError) {
                Log.e("fjgldfkgj", "Interstitial ad failed to load: " + adError.getErrorMessage())
            }

            override fun onAdLoaded(ad: Ad) {
                handler.removeCallbacksAndMessages(null)

                Log.d("fjgldfkgj", "Interstitial ad is loaded and ready to be displayed!")
                if (!this@SplashScreen.isFinishing)
                    interstitialAd?.show()
            }

            override fun onAdClicked(ad: Ad) {
                Log.d("fjgldfkgj", "Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                Log.d(TAG, "Interstitial ad impression logged!")
            }
        }
        interstitialAd?.loadAd(
            interstitialAd?.buildLoadAdConfig()
                ?.withAdListener(interstitialAdListener)
                ?.build()
        )


    }*/
    /*  fun loadFacebookinterstital() {
          interstitialAd =
              com.facebook.ads.InterstitialAd(
                  this,
                  resources.getString(R.string.facebook_interstital)
              )
          val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
              override fun onInterstitialDisplayed(ad: Ad) {
                  handler.removeCallbacksAndMessages(null)

                  Log.e("fjgldfkgj", "Interstitial ad displayed.")
              }

              override fun onInterstitialDismissed(ad: Ad) {

                  handler.removeCallbacksAndMessages(null)
                  startMainActivity()
              }

              override fun onError(ad: Ad?, adError: com.facebook.ads.AdError) {
                  Log.e("fjgldfkgj", "Interstitial ad failed to load: " + adError.getErrorMessage())
              }

              override fun onAdLoaded(ad: Ad) {
                  handler.removeCallbacksAndMessages(null)

                  Log.d("fjgldfkgj", "Interstitial ad is loaded and ready to be displayed!")
                  if (!this@SplashScreen.isFinishing)
                      interstitialAd?.show()
              }

              override fun onAdClicked(ad: Ad) {
                  Log.d("fjgldfkgj", "Interstitial ad clicked!")
              }

              override fun onLoggingImpression(ad: Ad) {
                  Log.d(AudienceNetworkAds.TAG, "Interstitial ad impression logged!")
              }
          }
          interstitialAd?.loadAd(
              interstitialAd?.buildLoadAdConfig()
                  ?.withAdListener(interstitialAdListener)
                  ?.build()
          )


      }

      fun loadInterstitialAdmob() {
          val adRequest: AdRequest = AdRequest.Builder().build()
          InterstitialAd.load(this, resources.getString(R.string.Interstitial), adRequest,
              object : InterstitialAdLoadCallback() {
                  override fun onAdLoaded(interstitialAd: InterstitialAd) {
                      mInterstitialAd = interstitialAd
                      Log.i("interst", "onAdLoaded")
                      mInterstitialAd!!.setFullScreenContentCallback(object :
                          FullScreenContentCallback() {
                          override fun onAdDismissedFullScreenContent() {
                              // openActivity<WhiteBoard>(false)
                              *//*  if (!SharedPreferenceClass(
                                      this@SplashScreen
                                  ).getBoolean("first_time")
                              ) {
                                  openActivity<OnBoardingScreen>(true)
                              } else {
                                  openActivity<MainActivity>(true)
                              }*//*


                            openActivity<MainActivity>(true)

                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {

                        }

                        override fun onAdShowedFullScreenContent() {
                            mInterstitialAd = null
                        }
                    })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.i("interst", loadAdError.message)
                    mInterstitialAd = null
                }
            })

    }*/
}
