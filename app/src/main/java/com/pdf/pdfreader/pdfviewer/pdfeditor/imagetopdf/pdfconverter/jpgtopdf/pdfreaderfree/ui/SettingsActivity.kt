package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SettingsFragment())
                .commit()
        }

        findViewById<View>(R.id.always_dark_activity).setOnClickListener {
           // startActivity(Intent(this, AlwaysDarkActivity::class.java))
        }
        findViewById<View>(R.id.force_dark_activity).setOnClickListener {
         //   startActivity(Intent(this, ForceDarkActivity::class.java))
        }
        findViewById<View>(R.id.ignore_override_force_dark_activity).setOnClickListener {
         //   startActivity(Intent(this, IgnoreOverrideForceDarkOptionActivity::class.java))
        }

    }
}