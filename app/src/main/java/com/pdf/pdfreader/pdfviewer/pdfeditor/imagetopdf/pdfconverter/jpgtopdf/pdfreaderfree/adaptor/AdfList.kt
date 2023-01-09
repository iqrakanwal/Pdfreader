package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.signature.ObjectKey
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.MainActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.Onlongprocess
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.OptionsClick
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.FileOutputStream


class AdfList(
    var pdffiles: ArrayList<PdfModels>,
    var optionsClick: OptionsClick,
    var activity:Activity ,
    var context: Activity,
    var onlongprocess: Onlongprocess? = null
) :
    RecyclerView.Adapter<AdfList.ItemView>() {
    val MIN_CLICK_INTERVAL: Long = 600
    var mLastClickTime: Long = 0
    inner class ItemView(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var namepdf: TextView? = null
        var sizePDF: TextView? = null
        var datePDF: TextView? = null
        var menu: ImageView? = null
        var layout: LinearLayout? = null
        var checkbox: CheckBox? = null
        var icon: ImageView? = null
        var container: View? = itemView
        init {
            namepdf = itemView.findViewById(R.id.namePDF)
            sizePDF = itemView.findViewById(R.id.sizePDF)
            datePDF = itemView.findViewById(R.id.datePDF)
            menu = itemView.findViewById(R.id.imageoption)
            layout = itemView.findViewById(R.id.layoout)
            checkbox = itemView.findViewById(R.id.checkBox3)
            icon = itemView.findViewById(R.id.icon)
            checkbox?.setOnClickListener(this)
      //      container?.setOnLongClickListener(activity)
        }

        override fun onClick(p0: View?) {
           // p0?.let { activity.prepareselection(it, adapterPosition) }
        }

    }


    /*   class ItemHolder(var container: View) : RecyclerView.ViewHolder(
           container
       ),
           View.OnClickListener {
           protected var folderTitle: TextView
           protected var folderPath: TextView
           protected var imageViewOption: ImageView
           protected var checkbox: CheckBox
           override fun onClick(v: View) {
               activity.prepareselection(v, adapterPosition)
           }

           init {
               folderTitle = container.findViewById(R.id.title)
               folderPath = container.findViewById(R.id.path)
               imageViewOption = container.findViewById(R.id.optionmenu)
               checkbox = container.findViewById(R.id.checkBox3)
               container.setOnLongClickListener(activity)
               checkbox.setOnClickListener(this)
           }
       }*/
    override fun onBindViewHolder(holder: ItemView, position: Int) {
        if(pdffiles!=null){
            val videoItem: PdfModels = pdffiles.get(position)
            holder.namepdf?.text = pdffiles.get(position).filesName.toString()
            holder.sizePDF?.text = pdffiles.get(position).fileSize.toString()
            holder.datePDF?.text = pdffiles.get(position).fileCreationDate.toString()
            holder.checkbox!!.visibility = View.GONE
            holder.menu?.setVisibility(View.VISIBLE)
            holder.checkbox!!.isChecked = pdffiles.get(position).isSelected
            // Glide.with(context).load(generateImageFromPdf(pdffiles.get(position).filePath)).into(holder.icon);
            //  val internetUrl = "http://i.imgur.com/DvpvklR.png"

//Log.e("dsdc", "${generateImageFromPdf(Uri.parse(pdffiles.get(position).filePath))}")


            /*    Glide
                    .with(context)
                    .load(generateImageFromPdf(Uri.parse(pdffiles.get(position).filePath)))
                    .thumbnail(0.1f)
                    .into(holder.icon!!)*/

            holder.menu?.setOnClickListener {
                val popup = PopupMenu(it.context, it)
                popup.menuInflater
                    .inflate(R.menu.popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    if (item.title == context.resources.getString(R.string.share_app)) {
                        optionsClick.onShareClick(Uri.parse(pdffiles.get(position).filePath))
                    } else if (item.title == context.resources.getString(R.string.delete)) {
                        optionsClick.onDelete(Uri.parse(pdffiles.get(position).filePath), position)
                    } else if (item.title == context.resources.getString(R.string.information)) {
                        optionsClick.onDetails(
                            Uri.parse(pdffiles.get(position).filePath),
                            pdffiles.get(position).filesName,
                            pdffiles.get(position).fileCreationDate,
                            pdffiles.get(position).fileSize
                        )
                    } else if (item.title == context.resources.getString(R.string.rename)) {
                        optionsClick.OnRenaming(
                            pdffiles.get(position).filePath,
                            position,
                            pdffiles.get(position).filesName
                        )
                    }
                    true
                }
                popup.show()
            }

            holder.container?.setOnClickListener {
                val currentClickTime = SystemClock.uptimeMillis()
                val elapsedTime = currentClickTime - mLastClickTime
                mLastClickTime = currentClickTime
                if (elapsedTime <= MIN_CLICK_INTERVAL) return@setOnClickListener
                optionsClick.onOpen(Uri.parse(pdffiles.get(position).filePath))
            }
            holder.icon?.setOnClickListener {
                holder.container?.performClick()
            }
         /*   if (activity.is_in_action_mode) {
                holder.checkbox!!.visibility = View.VISIBLE
                holder.menu?.setVisibility(View.GONE)
            }*/








        }

    }

    override fun getItemCount(): Int {
        return pdffiles.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ItemView(view)
    }

    fun filterList(filterllist: ArrayList<PdfModels>) {
        pdffiles = filterllist
        notifyDataSetChanged()
    }

    public fun deleteDone(position: Int) {
        pdffiles.removeAt(position)
    }

    fun renameFile(b: Boolean, position: Int, fileName: String, filePath: String) {


        if (b == true) {


            for (pdf in pdffiles) {

                //   pdffiles[position].filesName=


            }

        }

    }
    /* @TargetApi(Build.VERSION_CODES.LOLLIPOP)
     private fun generateImageFromPdf(filePath: String):Bitmap? {

 // filePath must be like : Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/42080.pdf"
         val file = File(filePath)
         // FileDescriptor for file, it allows you to close file when you are
 // done with it
         var mFileDescriptor: ParcelFileDescriptor? = null
         try {
             mFileDescriptor = ParcelFileDescriptor.open(
                 file,
                 ParcelFileDescriptor.MODE_READ_ONLY
             )
         } catch (e: FileNotFoundException) {
             e.printStackTrace()
         }
         // PdfRenderer enables rendering a PDF document
         var mPdfRenderer: PdfRenderer? = null
         try {
             mPdfRenderer = mFileDescriptor?.let { PdfRenderer(it) }
         } catch (e: IOException) {
             e.printStackTrace()
         }

 // Open page with specified index
         val mCurrentPage = mPdfRenderer!!.openPage(0)
         val bitmap = Bitmap.createBitmap(
             mCurrentPage.width,
             mCurrentPage.height, Bitmap.Config.ARGB_8888
         )

 // Pdf page is rendered on Bitmap
         mCurrentPage.render(
             bitmap, null, null,
             PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
         )
         // Set rendered bitmap to ImageView (pdfView in my case)
       //  pdfView.setImageBitmap(bitmap)
         mCurrentPage.close()
         mPdfRenderer.close()
         try {
             mFileDescriptor!!.close()
         } catch (e: IOException) {
             e.printStackTrace()
         }
         return bitmap
     }*/


    fun deleteFile(list: ArrayList<PdfModels>) {
        for (list in list) {
            pdffiles.remove(list)


        }


    }

    //PdfiumAndroid (https://github.com/barteksc/PdfiumAndroid)
    //https://github.com/barteksc/AndroidPdfViewer/issues/49
    fun generateImageFromPdf(pdfUri: Uri): Bitmap? {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(context)
        var bitmap:Bitmap?=null
        try {
            val fd: ParcelFileDescriptor? = context.getContentResolver().openFileDescriptor(pdfUri, "r")
            val pdfDocument: PdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            //saveImage(bmp)
            pdfiumCore.closeDocument(pdfDocument)
            bitmap= bmp// important!
        } catch (e: Exception) {
            //todo with exception
        }
        return bitmap
    }

       val FOLDER = Environment.getExternalStorageDirectory().toString() + "/PDF"
//       val FOLDER = Environment.getExternalStorageDirectory().toString() + "/Pdffiles"
       private fun saveImage(bmp: Bitmap) {
           var out: FileOutputStream? = null
           try {
               val folder = File(FOLDER)
               if (!folder.exists()) folder.mkdirs()
               val file = File(folder, "PDF.png")
               out = FileOutputStream(file)
               bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
           } catch (e: Exception) {
               //todo with exception
           } finally {
               try {
                   if (out != null) out.close()
               } catch (e: Exception) {
                   //todo with exception
               }
           }
       }






 /*   class ThumbnailBuilder(mContext: Context) :
        ModelLoader<String, Bitmap?> {
        private val mContext: Context

        @Nullable
        override fun buildLoadData(
            input: String,
            width: Int,
            height: Int,
            options: Options
        ): LoadData<Bitmap?>? {
            return LoadData(ObjectKey(input), ThumbnailCreator(input))
        }

        override fun handles(input: String): Boolean {
            // handles only pdf file
            return input.endsWith(".pdf")
        }

        private inner class ThumbnailCreator(private val input: String) :
            DataFetcher<Bitmap> {
            override fun loadData(
                priority: Priority,
                callback: DataFetcher.DataCallback<in Bitmap?>
            ) {
                try {
                    val output: Bitmap
                    val photoCacheDir = Glide.getPhotoCacheDir(mContext.getApplicationContext())
                    val thumbnail = File(
                        photoCacheDir, Uri.parse(
                            input
                        ).lastPathSegment + ".png"
                    )
                    // check if file is already exist then there is no need to re create it
                    if (!thumbnail.exists()) {
                        val pdDocument: PdfDocument = PdfDocument.load(File(input))
                        // create thumbnail for first page of pdf file
                        output = PDFRenderer(pdDocument).renderImage(0)
                        output.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(thumbnail))
                        pdDocument.close()
                    } else {
                        output = BitmapFactory.decodeFile(thumbnail.absolutePath)
                    }

                    // send output data
                    callback.onDataReady(output)
                } catch (e: java.lang.Exception) {
                    // if error
                    callback.onLoadFailed(e)
                }
            }

            override fun cleanup() {
                // empty
            }

            override fun cancel() {
                // empty
            }

            override fun getDataClass(): Class<Bitmap> {
                // output data class
                return Bitmap::class.java
            }

            override fun getDataSource(): DataSource {
                // data source local or network base
                return DataSource.LOCAL
            }
        }

        init {
            this.mContext = mContext
        }
    }*/
}