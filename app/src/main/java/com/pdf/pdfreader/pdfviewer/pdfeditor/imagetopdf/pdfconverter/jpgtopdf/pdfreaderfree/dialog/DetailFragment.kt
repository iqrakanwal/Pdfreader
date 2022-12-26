package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import kotlinx.android.synthetic.main.detail_dialog.*

class DetailFragment(
    var filePath: String,
    var fileName: String,
    var fileModification: String,
    var fileSize: String
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var viewRateUsDialog = inflater.inflate(R.layout.detail_dialog, container, false)
        return viewRateUsDialog
    }

    fun DetailFragment() {
        // doesn't do anything special
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filepathname.text = requireContext().resources.getString(R.string.file_path)+" ${filePath}"
        filetitlename.text =requireContext().resources.getString(R.string.file_name)+"${fileName}"
        filedatename.text = requireContext().resources.getString(R.string.file_modified)+"${fileModification}"
        filesizename.text = requireContext().resources.getString(R.string.file_sizeg)+"${fileSize}"
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setLayout(width, height)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

}