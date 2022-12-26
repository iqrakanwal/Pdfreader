package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.DeleteClickListener
import kotlinx.android.synthetic.main.delete_dialog.*
import kotlinx.android.synthetic.main.exit_dialog.deny

class DeleteDialog(
    var uri: Uri,
    var exitInterface: DeleteClickListener,
    var callbackMethod: (Boolean,Int, Uri) -> Unit,
    var position: Int
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var viewRateUsDialog = inflater.inflate(R.layout.delete_dialog, container, false)
        return viewRateUsDialog
    }


    fun DeleteDialog() {
        // doesn't do anything special
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.deny.setOnClickListener {
            // exitInterface.onCancel()
            callbackMethod(false, position,uri)
            dismiss()
        }

        this.allow.setOnClickListener {
            //  exitInterface.onDeleteOption(uri)
            callbackMethod(true, position,uri)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setLayout(width, height)
    }


}