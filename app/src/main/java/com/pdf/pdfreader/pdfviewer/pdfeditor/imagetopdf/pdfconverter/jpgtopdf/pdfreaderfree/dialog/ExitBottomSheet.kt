package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.exit_bottom_sheet_layout.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ExitBottomSheet(context: Context) : BottomSheetDialogFragment() {
    var dialogView: View? = null
    private val mainViewModel: MainViewModel by sharedViewModel()
    var isAdEnable = false
    var exitadView: NativeAdView? = null
    //   private var nativeAdLayout: NativeAdLayout? = null

    companion object {
        fun newInstance(contextObj: Context): ExitBottomSheet {
            return ExitBottomSheet(contextObj)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.exit_bottom_sheet_layout, container, false)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        try {
            view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val dialog = dialog as BottomSheetDialog
                }
            })
            dialogView = view
            exitadView = layoutInflater.inflate(R.layout.ad_unified, null) as NativeAdView
          //  adsManager?.populateUnifiedNativeAdView(mainViewModel.getBottomSheetAd()!!, exitadView!!, exit_ad_frame)
            // nativeAdLayout = layoutInflater.inflate(R.layout.facebook_native_ad, null) as NativeAdLayout
//            if (mainViewModel.getUnifiedAd() != null) {
////                adsManager?.populateUnifiedNativeAdView(
////                    mainViewModel.getUnifiedAd()!!,
////                    exitadView!!,
////                    exit_ad_frame
////                )
//                // populateAd(it, exitadslayout!!)
//            }
//            else
//                if (mainViewModel.getexitfacebooknativead() != null) {
//                adsManager?.inflateNativeAdFacebook(
//                    "dcxkvosdkvc", mainViewModel.getexitfacebooknativead()!!,
//                    context as HomeActivity, nativeAdLayout!!, exit_ad_frame
//                )
//            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        view.btn_tap_again.setOnClickListener {


            System.exit(0)
            //  (view.context as MainActivity2).finish()
            // Toast.makeText(context, "exit", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        if (dialogView != null) {
            dialogView?.viewTreeObserver?.addOnGlobalLayoutListener(null)
        }
        super.onDestroyView()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext()) {
        }

    }





}


