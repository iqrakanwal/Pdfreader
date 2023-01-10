package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.FileSelectionAdaptor
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog.CreateFile
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.createFileInterface
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_file_selection_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.net.FileNameMap
import java.util.ArrayList

class FileSelectionList : AppCompatActivity(), createFileInterface {
    private val model: MainViewModel by viewModel()
    private var files: java.util.ArrayList<PdfModels> = arrayListOf()
    private lateinit var fileSelectionList: FileSelectionAdaptor
    private lateinit var linearLayoutManager: LinearLayoutManager
    var dir: File? = null
    var selectionlist: java.util.ArrayList<PdfModels?> = java.util.ArrayList<PdfModels?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_selection_list)
        mergebutton.setOnClickListener {










            if (selectionlist.size != 0) {
                Toast.makeText(this, "Please select ${selectionlist.size}", Toast.LENGTH_SHORT).show()
                val fm: FragmentManager = supportFragmentManager
                val editNameDialogFragment: CreateFile = CreateFile(this)
                editNameDialogFragment.show(fm, "exit")
            } else {
                Toast.makeText(this, "Please select ${selectionlist.size}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        model.loadAllPdf(getDirectory())
        model.allPdfList.observe(this) {
            if (it != null) {
                files.addAll(it)
                linearLayoutManager = LinearLayoutManager(this)
                fileSelectionList = FileSelectionAdaptor(files, this)
                recyclarview.layoutManager = linearLayoutManager
                recyclarview.adapter = fileSelectionList
            }

        }
    }


    private fun getDirectory(): File {
        val externalStorageDirectory = File(System.getenv("EXTERNAL_STORAGE"))
        dir = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            File(externalStorageDirectory.toString())
        } else {
            File(Environment.getExternalStorageDirectory().toString())
        }
        return dir as File
    }


    fun prepareselection(check: View, position: Int) {
        val v = check as CheckBox
        if (v.isChecked) {
            selectionlist.add(files[position])
            files[position]?.isSelected = true
        } else if (!v.isChecked) {
            selectionlist.remove(files[position])
            files[position]?.isSelected = false
        }
    }


    fun mergeFile(list: ArrayList<PdfModels?>, fileName: String) {
        model.mergeFile(list, fileName)
    }

    override fun close() {
    }

    override fun create(path: String) {
        mergeFile(selectionlist, "${path}.pdf")
    }


}