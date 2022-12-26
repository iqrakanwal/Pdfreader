package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.delay
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.feedbackEmail
import kotlinx.android.synthetic.main.rate_us_new.*

class RateUsDailog() : DialogFragment() {
    var rateornot = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        dialog!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var viewRateUsDialog = inflater.inflate(R.layout.rate_us_new, container, false)
        return viewRateUsDialog
    }



    init {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        this.cancel.setOnClickListener {
//            // rateUs()
//            dismiss()
//        }
//        this.disconnect.setOnClickListener {
//            when (this.ratenowtext.text) {
//                resources.getString(R.string.feedback) -> {
//                    try {
//                        // feedbackEmail(context)
//                        //   if (callback != null) exitDialogChanged.loadAds(context,)
//                    } catch (ex: Exception) {
//                        ex.printStackTrace()
//                    }
//                    dismiss()
//                }
//                resources.getString(R.string.rateUs) -> {
//                    try {
//                        context?.startActivity(
//                            Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")
//                            )
//                        )
//                        //  if (callback != null) exitDialogChanged.loadAds(context,)
//                    } catch (e: ActivityNotFoundException) {
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("market://details?id=${context?.packageName}")
//                        ).apply {
//                            context?.startActivity(this)
//                        }
//                        //if (callback != null) exitDialogChanged.loadAds(context,)
//                    }
//                    dismiss()
//                }
//                resources.getString(R.string.exit) -> {
//                    (context as MainActivity).apply {
//                        dismiss()
//                        finish()
//                    }
//
//
//                }
//            }
//
//
////            if (cccrattingstars?.isEnabled!!) {
////
////                if (rateornot == true) {
////                    delay(300, Runnable {
////
////                    })
////                } else if (rateornot != true) {
////
////                }
////            }
////            else if (!cccrattingstars?.isEnabled!!) {
////                hideDialog()
////
////
////            }
//
//        }

        this.cccrattingstars.setOnStarChangeListener { _, star ->
            rateornot = star.toInt()
            when (star.toInt()) {
                0 -> {
                    feedbackEmail(requireActivity())
                    dismiss()
                   // this.ratenowtext.text = resources.getString(R.string.exit)
                }

                1, 2, 3 -> {
                    feedbackEmail(requireActivity())
                    dismiss()
                    //  this.ratenowtext.text = resources.getString(R.string.feedback)
                }
                4, 5 -> {
                    try {
                        context?.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")
                            )
                        )
                        //  if (callback != null) exitDialogChanged.loadAds(context,)
                    } catch (e: ActivityNotFoundException) {
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${context?.packageName}")
                        ).apply {
                            context?.startActivity(this)
                        }
                        //if (callback != null) exitDialogChanged.loadAds(context,)
                    }
                    dismiss()
                    //  this.ratenowtext.text = resources.getString(R.string.rateUs)
                }

//                else -> {
//                    if (isFromBackPress)
//                        this.ratenowtext.text = resources.getString(R.string.exit)
//                    else
//                        this.ratenowtext.text = resources.getString(R.string.feedback)
//
//                }


            }

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    fun rateUs() {


        delay(300, Runnable {
            try {
                context?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}")
                    )
                )
                // if (callback != null) exitDialogChanged.loadAds(context,)
            } catch (e: ActivityNotFoundException) {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${context?.packageName}")
                ).apply {
                    context?.startActivity(this)
                }

                //if (callback != null) exitDialogChanged.loadAds(context,)
            }
            dismiss()
        })
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setLayout(width, height)

    }

}