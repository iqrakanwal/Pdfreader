package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui.FileSelectionList
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.ui.FileSelectionsSplit

class FileSelectionSplitAdaptor(var list: List<PdfModels>, var hkjhkjh: FileSelectionsSplit): RecyclerView.Adapter<FileSelectionSplitAdaptor.ItemView>() {
   inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var namepdf: TextView? = null
        var sizePDF: TextView? = null
        var datePDF: TextView? = null
        var layout: LinearLayout? = null
        var checkbox: CheckBox? = null
        var icon: ImageView? = null
        var container: View? = itemView
        init {
            namepdf = itemView.findViewById(R.id.namePDF)
            sizePDF = itemView.findViewById(R.id.sizePDF)
            datePDF = itemView.findViewById(R.id.datePDF)
            layout = itemView.findViewById(R.id.layoout)
            checkbox = itemView.findViewById(R.id.checkBox3)
            icon = itemView.findViewById(R.id.icon)
            checkbox?.setOnClickListener(this)
            //      container?.setOnLongClickListener(activity)
        }

        override fun onClick(p0: View?) {
            hkjhkjh.prepareselection(p0!!, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_selection_item_row, parent, false)
        return ItemView(view)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.namepdf?.text = list.get(position).filesName.toString()
        holder.sizePDF?.text = list.get(position).fileSize.toString()
        holder.datePDF?.text = list.get(position).fileCreationDate.toString()
        holder.checkbox!!.isChecked = list.get(position).isSelected

    }

    override fun getItemCount(): Int {
        return list.size
    }
}