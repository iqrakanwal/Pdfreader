package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel

import android.annotation.SuppressLint
import android.content.ComponentCallbacks
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.ads.nativead.NativeAd
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfSmartCopy
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.datastore.UIModePreference
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.FilesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(var application: Context, var filesRepository: FilesRepository) : ViewModel() {
    var allPdfs = MutableLiveData<List<PdfModels>>()
    val allPdfList: LiveData<List<PdfModels>> get() = allPdfs
    private var pdfListMemory = mutableListOf<PdfModels>()
    private var fileList = ArrayList<File>()
    var unifiedNativeAdVar: NativeAd? = null
    var dir: File? = null

    private val uiDataStore = UIModePreference(application)

    /**
     * Load All Files
     */
    //   val isDarkTheme get() = uiDataStore.isNightMode
    val isDarkTheme get() = filesRepository.isDarkTheme()

    /*   val getTheme get() = uiDataStore.get*/
    /*   fun getTheme() = run {
           var value = DEFAULT
           runBlocking {
               value = uiDataStore.getValue()
           }
           value
       }*/


    fun setDarkTheme(darktheme: String) {
        filesRepository.setDarkTheme(darktheme)
    }

    val getUIMode = uiDataStore.uiMode
    val getVerticalView = uiDataStore.isVerticale
    val getNightMode = uiDataStore.isNightMode

    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.saveToDataStore(isNightMode)
        }
    }

    fun saveToNightMode(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.saveNightMode(isNightMode)
        }
    }

    fun saveView(isVertial: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.saveIsVerticalView(isVertial)
        }

    }


//    fun setDarkTheme(darktheme: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            uiDataStore.saveNightMode(darktheme)
//        }
//    }


    fun setTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiDataStore.setTheme(theme)
        }


    }

    suspend fun getvalue() {
        Log.e("fffffffffd", getUIMode.first().toString())
    }


    fun loadAllPdf(dir: File) {
        viewModelScope.launch {
            pdfListMemory.clear()
            withContext(Dispatchers.IO) {
                val pdfList = getAllFiles(dir)
                allPdfs.postValue(pdfList)
            }
        }
    }

    /**
     * Delete Files
     */
    fun deleteFiles(position: Int): Boolean {
        var ifDeleted = false
        viewModelScope.launch {
            if (deletingFiles(position)) {
                ifDeleted = true
                delay(2000)
            }
        }
        return ifDeleted
    }

    /**
     * Rename Files
     */
    fun renameFile(filePath: String, position: Int, newName: String, callbacks: (String) -> Unit) {
        viewModelScope.launch {
            renamingFile(filePath, position, newName, callbacks)
        }
    }

    fun renamefun(b: Boolean, func: (Boolean) -> Unit) {
        func(b)
    }

    suspend fun getAllFiles(dir: File): List<PdfModels> {
        val listFile = dir.listFiles()
        if (listFile != null && listFile.isNotEmpty()) {
            for (i in listFile.indices) {
                if (listFile[i].isDirectory) {
                    Log.e("pdf", "isDirectory")
                    getAllFiles(listFile[i])
                } else {
                    if (listFile[i].name.endsWith(".pdf")) {
                        //to-remove the duplication of files
                        var booleanpdf = false
                        for (j in fileList.indices) {
                            if (fileList[j].name == listFile[i].name) {
                                booleanpdf = true
                            }
                        }
                        var name = listFile[i].name
                        val position = name.lastIndexOf(".")
                        if (position > 0 && position < name.length - 1) {
                            name = name.substring(0, position)
                        }
                        val dateInMillis = Date(listFile[i].lastModified())
                        @SuppressLint("SimpleDateFormat") val dateFormat =
                            SimpleDateFormat("dd-MMM-yy, hh:mm a")
                        val date = dateFormat.format(dateInMillis)
                        val size: String = formatSize(listFile[i].length())
                        val path: String = listFile[i].path
                        if (!booleanpdf) {
                            Log.e("pdf", ",l,l,")
                            pdfListMemory.add(PdfModels(name, size, date, path))
                        }
                    }
                }
            }
        }
        return pdfListMemory
    }

    fun clearList() {
        pdfListMemory.clear()
    }

    private fun formatSize(size1: Long): String {
        var size = size1
        var suffix: String? = null
        if (size >= 1024) {
            suffix = " KB"
            size /= 1024
            if (size >= 1024) {
                suffix = " MB"
                size /= 1024
            }
        }
        val resultBuffer = StringBuilder(size.toString())
        var commaOffset = resultBuffer.length - 3
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',')
            commaOffset -= 3
        }
        if (suffix != null) resultBuffer.append(suffix)
        return resultBuffer.toString()
    }

    private fun deletingFiles(position: Int): Boolean {
        var file: File? = null
        // for (position in filesPositions) {
        file = allPdfs.value?.get(position)?.filePath?.let { File(it) }
        file?.delete()
        if (!(file!!.exists() && !file.delete())) {
            allPdfs.value = allPdfs.value?.toMutableList()?.apply { this.removeAt(position) }
        }
        //}
        return !(file!!.exists() && !file.delete())
    }

    private fun renamingFile(
        filePath1: String, position: Int, newName: String, addfunc: (String) -> Unit
    ) {
        val oldFile: File = File(filePath1)
        val oldPath = oldFile.path
        val newFileName = (oldPath.substring(0, oldPath.lastIndexOf('/')) + "/" + newName + ".pdf")
        val newFile = File(newFileName)
        if (newFile.exists()) {
            addfunc("exist")
            Log.e("file exists", "before true")
        } else {
            addfunc("not exist")
            Log.e("file exists", "before false")
            val newFilePath = newFile.path
            Log.e("size", "before ${allPdfs.value?.size}")
            if (oldFile.renameTo(newFile)) {
                //  allPdfs.value?.
                allPdfs.value = allPdfs.value?.mapIndexed { index, allPdfModel ->
                    if (index != position) {
                        allPdfModel
                    } else {
                        allPdfModel.apply {
                            filesName = newName
                            filePath = newFilePath
                        }

                    }
                }
                addfunc("rename")
                Log.e("size", "after ${allPdfs.value?.size}")
                // allPdfs.postValue(pdfList)

                true
            } else {
                false
            }
        }

    }


    /**
     * Add New Files
     */
    fun addNewFile(filePath: String) {
        viewModelScope.launch {
            addingFile(filePath)
        }
    }

    private fun addingFile(filePath: String) {
        val file = File(filePath)
        var name = file.name
        val position = name.lastIndexOf(".")
        if (position > 0 && position < name.length - 1) {
            name = name.substring(0, position)
        }
        val dateInMillis = Date(file.lastModified())
        @SuppressLint("SimpleDateFormat") val dateFormat =
            SimpleDateFormat("dd-MMM-yy hh.mm a")
        val date = dateFormat.format(dateInMillis)
        val size: String = formatSize(file.length())
        val path: String = file.path
        allPdfs.value = allPdfs.value?.toMutableList()?.apply {
            this.add(PdfModels(name, size, date, path))
        }?.toList()
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        value.addAll(values)
        this.value = value
    }

    fun getNativeAdHome(): NativeAd? {
        return unifiedNativeAdVar
    }

    fun setNativeAdHome(unifiedNativeAd: NativeAd) {
        unifiedNativeAdVar = unifiedNativeAd
    }

    fun registerOpenAds() {
        filesRepository.initAppOpenAds()
    }

    private suspend fun doTaskOne(): String {
        delay(2000)
        return "One"
    }

    private suspend fun doTaskTwo(): String {
        delay(2000)
        return "Two"
    }

    // kotlin function using async
    fun startLongRunningTaskInParallel() {
        viewModelScope.launch {
            val resultOneDeferred = async { doTaskOne() }
            val resultTwoDeferred = async { doTaskTwo() }
            val combinedResult = resultOneDeferred.await() + resultTwoDeferred.await()
        }
    }

    suspend fun testWithContext() {
        var resultOne = "GFG"
        var resultTwo = "Is Best"
        Log.i("withContext", "Before")
        resultOne = withContext(Dispatchers.IO) { function1() }
        resultTwo = withContext(Dispatchers.IO) { function2() }
        Log.i("withContext", "After")
        val resultText = resultOne + resultTwo
        Log.i("withContext", resultText)
    }

    suspend fun function1(): String {
        delay(10000L)
        val message = "function1"
        Log.i("withContext", message)
        return message
    }

    suspend fun function2(): String {
        delay(20000L)
        val message = "function2"
        Log.i("withContext", message)
        return message
    }

    fun mergeFile(list: ArrayList<PdfModels?>, filename: String) {
        try {
            val document = Document()
            val copy = PdfCopy(document, FileOutputStream(getDirectory(filename)))
            document.open()
            for (list in list) {
                var list1 = PdfReader(File(list?.filePath).absolutePath)
                for (i in 1..list1.numberOfPages) {
                    copy.addPage(copy.getImportedPage(list1, i))
                }
                list1.close()
            }

            document.close()
            Log.i("MergePDF", "PDFs merged successfully!")
        } catch (e: Exception) {
            Log.e("MergePDF", "Error merging PDFs", e)
        }
    }

    fun spliteFile(list: String) {
        val reader = PdfReader(list)
        val numberOfPages = reader.numberOfPages
        val pageSize = reader.getPageSize(1)

        try {
            for (i in 1..numberOfPages step 2) {
                val document = Document(pageSize)
                val copy = PdfSmartCopy(
                    document, FileOutputStream(
                        getDirectory("output_$i.pdf")
                    )
                )
                document.open()
                copy.addPage(copy.getImportedPage(reader, i))
                if (i + 1 <= numberOfPages) {
                    copy.addPage(copy.getImportedPage(reader, i + 1))
                }
                document.close()
                copy.close()
            }
            reader.close()
            Log.i("SplitPDF", "PDF split successfully!")
        } catch (e: Exception) {
            Log.e("SplitPDF", "Error splitting PDF", e)
        }
    }

    private fun getDirectory(filename: String): File {
        val externalStorageDirectory = File(System.getenv("EXTERNAL_STORAGE"))
        dir = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            File(externalStorageDirectory.toString() + "/${filename}")
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/${filename}")
        }
        return dir as File
    }

}

