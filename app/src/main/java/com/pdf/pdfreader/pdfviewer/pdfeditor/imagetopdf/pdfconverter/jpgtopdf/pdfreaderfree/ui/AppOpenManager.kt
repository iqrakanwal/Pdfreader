package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.facebook.ads.AudienceNetworkActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.BaseClass
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.FilesRepository
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.isInternetConnected
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppOpenManager(myApplication: BaseClass) : Application.ActivityLifecycleCallbacks,
    KoinComponent,
    LifecycleObserver {
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    private val myApplication: BaseClass = myApplication
    private var isShowingAd = false
    val dataRepositoryClass: FilesRepository by inject()

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
        Log.e("inapp", "fdfflk")
    }


    /** Shows the ad if one isn't already showing.  */


    /** Request an ad  */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }

        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                Log.e("openad", "open")
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {

                Log.e("openad", "onAdFailedToLoad")

                // Handle the error.
            }
        }
        val request: AdRequest = adRequest
        AppOpenAd.load(
            myApplication, myApplication.baseContext.getString(R.string.App_Open), request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }

    /** Creates and returns ad request.  */
    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()

    /** Utility method that checks if ad exists and can be shown.  */
    val isAdAvailable: Boolean
        get() = appOpenAd != null

    companion object {
        private const val LOG_TAG = "AppOpenManager"
        //   private const val AD_UNIT_ID = getRe
    }

    /** LifecycleObserver methods  */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (!dataRepositoryClass.isPurchased()) {
            Handler().postDelayed({
                if (currentActivity != null && currentActivity !is SplashScreen && currentActivity !is AdActivity && currentActivity !is AudienceNetworkActivity) {
                    showAdIfAvailable()
                    Log.d(LOG_TAG, "onStart")
                } else {

                }

            }, 500)


        }

    }

    fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        if (!dataRepositoryClass.isPurchased()) {
                            if (isInternetConnected(myApplication)) {
                                /* RemoteConfigEngine.activateIt(Activity()) {
                                     if (it?.getBoolean("App_Open_Ads") == true) {*/
                                //Toast.makeText(myApplication.applicationContext, "app open add", Toast.LENGTH_LONG).show()
                                fetchAd()
                                /*     }
                                 }*/

                            }
                        } else if (dataRepositoryClass.isPurchased()) {


                        }

                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.setFullScreenContentCallback(fullScreenContentCallback)
            appOpenAd!!.show(currentActivity)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            if (!dataRepositoryClass.isPurchased()) {
                if (isInternetConnected(myApplication)) {
                    /*  RemoteConfigEngine.activateIt(Activity()) {
                          if (it?.getBoolean("App_Open_Ads") == true) {*/
                    //  Toast.makeText(myApplication.applicationContext, "app open ad", Toast.LENGTH_LONG).show()
                    fetchAd()
                    /*    }
                    }*/

                }
            } else if (dataRepositoryClass.isPurchased()) {


            }


            // fetchAd()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity;
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity;
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity;
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null;
    }


}