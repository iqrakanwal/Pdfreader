package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog

import LanguageSimple
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.OnLanClicked
import kotlinx.android.synthetic.main.row_language.view.*

class LanguageAdapterPdf(
    private val languagesList: ArrayList<LanguageSimple>,
    var lastSelectedPosition: Int
    , var context: Context
) : RecyclerView.Adapter<LanguageAdapterPdf.ViewHolder>() {





    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_language, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val languageModel = languagesList[position]
        holder.offerName.text = languageModel.name
        if (position == lastSelectedPosition) {
            holder.tick.visibility = View.VISIBLE
            holder.offerName.setTextColor(context.resources.getColor(R.color.purple_700))
        } else if (position != lastSelectedPosition) {
            holder.tick.visibility = View.INVISIBLE
           // holder.offerName.setTextColor(context.resources.getColor(R.color.common))





        }
        holder.view.setOnClickListener {
            if (position != lastSelectedPosition) {
                lastSelectedPosition = position
                //holder.tick.visibility = View.VISIBLE
                onLanClicked?.onLanguageSelected(languagesList[position])
                notifyDataSetChanged()
            } else if (position == lastSelectedPosition) {
                lastSelectedPosition = position
                onLanClicked?.onLanguageSelected(languagesList[position])
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return languagesList.size
    }

    var onLanClicked: OnLanClicked? = null
    fun setOnLanguageSelected(onLanClicked: OnLanClicked) {
        this.onLanClicked = onLanClicked
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var offerName: TextView = view.tvLanguageName
        var tick: ImageView = view.tick
        var view: ConstraintLayout = view.view
    }

}