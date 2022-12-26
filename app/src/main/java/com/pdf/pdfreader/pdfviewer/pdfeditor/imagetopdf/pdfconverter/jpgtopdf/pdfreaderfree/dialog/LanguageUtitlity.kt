package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import java.util.*

object LanguageUtitlity {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    @JvmStatic
    @SuppressLint("DefaultLocale", "ObsoleteSdkInt")
    fun setAppLocale(context: Context, localeCode: String) {

        persist(context, localeCode)
        Log.d("locality_code", "setAppLocale $localeCode")

        try {
            val resources = context.resources
            val dm = resources.displayMetrics
            val config = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(Locale(localeCode.toLowerCase()))
            } else {
                config.locale = Locale(localeCode.toLowerCase())
            }
            Locale.setDefault(config.locale)
            resources.updateConfiguration(config, dm)
        } catch (ex: Exception) {
            Log.d("locality_code", "ExLanguageSet $ex")
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @JvmStatic
    fun setDefaultAppLocale(context: Context) {
        val resources = context.resources
        val dm = resources.displayMetrics
        val config = resources.configuration
        config.setLocale(Locale(Locale.getDefault().language))
        resources.updateConfiguration(config, dm)
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }


    fun getLanguageName(context: Context): String {
        val appLanguages = context.resources.getStringArray(R.array.appLanguages)
        val appLangCode = context.resources.getStringArray(R.array.appLangCode)
        val language = getPersistedData(context, Locale.getDefault().language)
        appLangCode.forEachIndexed { index, code ->
            run {
                if (code == language) {
                    return appLanguages[index]
                }
            }
        }
        return appLanguages[0]
    }



}