package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui.FileSelectionList
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui.FileSelectionsSplit
import kotlinx.android.synthetic.main.fragment_tool.*


class ToolFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tool, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mergefile.setOnClickListener {
            startActivity(Intent(requireContext(), FileSelectionList::class.java))
        }
        splitefile.setOnClickListener {
            startActivity(Intent(requireContext(), FileSelectionsSplit::class.java))
        }
    }


}