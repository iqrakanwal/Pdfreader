package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.BaseClass
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui.AppOpenManager
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.DEFAULT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.LIGHT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.NIGHT_MODE
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.PURCHASED
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.DataStoreUtils
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.DataTypeValue
import kotlinx.coroutines.runBlocking

class FilesRepository(private val dataStoreUtils: DataStoreUtils, var context: Context, var app:BaseClass) {
    var allPdfFile: MutableList<LiveData<PdfModels>>? = null
    var isCheck = true
    fun initAppOpenAds(){
        if (isCheck){
            isCheck = false
            if (app != null ) {
                AppOpenManager(app)
            }
        }
    }

    init {
   //    initFirebaseRemoteConfig()
    }
    public fun initFirebaseRemoteConfig() {
        FirebaseApp.initializeApp(context)
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
            setConfigSettingsAsync(configSettings)
            //set this during development

            setDefaultsAsync(R.xml.default_config)
            fetchAndActivate().addOnCompleteListener { task ->
                val updated = task.result
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("initConfig", "Config params updated: $updated")
                } else {
                    Log.d("initConfig", "Config params updated: $updated")
                }
            }
        }

    }

    fun setDarkTheme(darktheme: Any) {
        runBlocking {
            dataStoreUtils.saveValue(NIGHT_MODE, darktheme)
        }
    }

    fun isDarkTheme() = run {
        var isDarktheme = DEFAULT
        runBlocking {
            isDarktheme =
                dataStoreUtils.getValue(
                    NIGHT_MODE,
                    DataTypeValue.STRING,
                    isDarktheme
                ) as String
        }
        isDarktheme
    }



    fun isPurchased() = run {
        var isPurchased = false
        runBlocking {
            isPurchased =
                dataStoreUtils.getValue(
                    PURCHASED,
                    DataTypeValue.BOOLEAN,
                    isPurchased
                ) as Boolean
        }
        isPurchased
    }

    fun setPurchase(purchase: Any) {
        runBlocking {
            dataStoreUtils.saveValue(PURCHASED, purchase)

        }

    }
}