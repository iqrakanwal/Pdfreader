package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.ThemeHelper
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.AdRepository
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.FilesRepository
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.AdmobAdsUtils
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.DEFAULT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.DataStoreUtils
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.RemoteConfigEngine
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.AdViewModel
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseClass : Application(), KoinComponent {
    val viewModelModule = module {
        viewModel { MainViewModel(get(), get()) }
        single { FilesRepository(get(), get(), this@BaseClass) }
        single { DataStoreUtils(get()) }
        single { AdRepository(get()) }
        single { AdmobAdsUtils(get()) }
        viewModel { AdViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseClass)
            modules(listOf(viewModelModule))
        }

        val filesRepository: FilesRepository by inject()
   //     filesRepository.isDarkTheme()
        Log.e("fff", "${filesRepository.isDarkTheme()}")
        ThemeHelper.applyTheme(filesRepository.isDarkTheme())
        RemoteConfigEngine.init(if (BuildConfig.DEBUG) 0 else 3600, R.xml.default_config)
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(
            this
        ) { }
    }


}