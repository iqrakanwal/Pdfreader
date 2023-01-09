package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments.HomeFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments.ToolFragment
import kotlinx.android.synthetic.main.activity_main_screen2.*


class MainScreen : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen2)
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView?.setOnNavigationItemSelectedListener(this);    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.files -> {
                fragment = HomeFragment()
                toolbar.title = "Files"



            }
            R.id.tools -> {
                fragment = ToolFragment()
                toolbar.title = "Tools"


            }
        }
        if (fragment != null) {
            loadFragment(fragment)
        }
        return true    }

    fun loadFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction().replace(R.id.relativelayout, fragment!!).commit()
    }
}