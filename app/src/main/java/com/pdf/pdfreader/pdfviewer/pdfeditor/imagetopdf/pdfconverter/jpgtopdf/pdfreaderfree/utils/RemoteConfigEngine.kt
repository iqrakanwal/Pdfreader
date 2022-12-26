package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils
import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.Keep
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.BuildConfig
import org.json.JSONObject

@Keep
object RemoteConfigEngine {
    @SuppressLint("StaticFieldLeak")
    private var remoteConfigEngine: FirebaseRemoteConfig? = null
    private var recentSavedJSON: JSONObject? = null
    fun init(
        fetchDuration: Long = if (BuildConfig.DEBUG) 0 else 3600,
        defaultsPrefs: Int
    ) {
        try {
            remoteConfigEngine = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(fetchDuration)
                .build()
            remoteConfigEngine?.setConfigSettingsAsync(configSettings)
            remoteConfigEngine?.setDefaultsAsync(defaultsPrefs)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    fun activateIt(activity: Activity, callback: (JSONObject?) -> Unit) {
        try {
            if (recentSavedJSON == null) {
                remoteConfigEngine!!
                    .fetchAndActivate()
                    .addOnCompleteListener(activity) {
                        if(it.isSuccessful){

                            try {
                                val jsonString =
                                    if (BuildConfig.DEBUG) {
                                        remoteConfigEngine!!.getValue("ads_setting_debug")
                                            .asString()

                                    } else {
                                        remoteConfigEngine!!.getValue("ads_setting")
                                            .asString()
                                    }
                                val jsonObject = JSONObject(jsonString)
                                recentSavedJSON = jsonObject
                                callback(jsonObject)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                callback(null)
                            }

                        }



                    }
            } else {
                callback(recentSavedJSON)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            callback(null)
        }
    }
    private fun getJsonVal(key: String): Boolean {
        return try {
            recentSavedJSON!!.getBoolean(key)
        } catch (ex: Exception) {
            true
        }
    }
    private fun fetchFromLoadedJSON(key: String, callback: (Boolean) -> Unit) {
        try {
            callback(getJsonVal(key))
        } catch (ex: Exception) {
            ex.printStackTrace()
            callback(true)
        }
    }
    fun isExitDialogNativeOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("exit_dialog", callback)
    }
    fun isActivityHowToUserLowerOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("activity_how_to_use_lower_ad", callback)
    }
    fun isMainScreenLowerNativeOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("main_screen_lower_native", callback)
    }
    fun isHowToUseInterstitialOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("how_to_use_interstitial", callback)
    }
    fun isConnectButtonInterstitialOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("connect_button_interstitial", callback)
    }
    fun isSplashInterstitialOnline(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("splash_interstitial", callback)
    }
    fun imagepreview_native(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("imagepreview_native", callback)
    }
    fun settings_native(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("setting_native", callback)
    }
    fun facebook_ads(callback: (Boolean) -> Unit) {
        fetchFromLoadedJSON("show_facebook_ads", callback)
    }


}