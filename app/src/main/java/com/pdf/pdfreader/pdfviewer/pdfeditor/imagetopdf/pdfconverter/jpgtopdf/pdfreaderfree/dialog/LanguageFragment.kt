package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog
import LanguageSimple
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.MainActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.OnLanClicked
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.openActivity
import kotlinx.android.synthetic.main.language_fragment.*
class LanguageFragment( ) : DialogFragment() {
    private var selectedPosition = 0
    private var selectedLangCode = "en"
    private var facebookads = false
    private var initialLayoutComplete = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var viewRateUsDialog = inflater.inflate(R.layout.language_fragment, container, false)
        return viewRateUsDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appLangList = recyclarview
        val langList = ArrayList<LanguageSimple>()
        val language = LanguageUtitlity.getLanguage(requireContext())
        selectedLangCode = language ?: "en"
        val recyclerLayoutManager = LinearLayoutManager(requireContext())
        appLangList.layoutManager = recyclerLayoutManager
        val appLanguages = resources.getStringArray(R.array.appLanguages)
        val appLangCode = resources.getStringArray(R.array.appLangCode)
        for (i in appLanguages.indices) {
            if (selectedLangCode.equals(appLangCode[i], ignoreCase = true))
                selectedPosition = i
            val languageModel = LanguageSimple(
                appLanguages[i],
                appLangCode[i]
            )
            langList.add(languageModel)
        }
        val localizationAdapter =
            LanguageAdapterPdf(
                langList,
                selectedPosition, requireContext()
            )
        appLangList.adapter = localizationAdapter
        localizationAdapter.setOnLanguageSelected(object : OnLanClicked {
            override fun onLanguageSelected(language: LanguageSimple?) {
                selectedLangCode = language?.code.toString()
                LanguageUtitlity.setAppLocale(requireContext(), selectedLangCode)
                if (requireActivity().isFinishing.not()) {
//                activity.openActivity<MainActivity>()
                    requireActivity().openActivity<MainActivity>()
                    finishAffinity(requireActivity())
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
        dialog!!.window!!.setLayout(width, height)
    }

}