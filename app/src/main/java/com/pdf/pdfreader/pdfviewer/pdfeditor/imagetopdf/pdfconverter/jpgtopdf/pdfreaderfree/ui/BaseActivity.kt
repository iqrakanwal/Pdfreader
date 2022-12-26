package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
open class   BaseActivity : AppCompatActivity()  {
    //private val mainViewModel: MainViewModel by viewModel()
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    public fun setAppTheme(@StyleRes style: Int) {
        setTheme(style)
    }

   /* public  fun isNightModeEnabled(): Boolean {
        return mainViewModel.isDarkTheme
    }

    public fun setIsNightModeEnabled(state: Boolean) {
        mainViewModel.setDarkTheme(state)
    }*/

}