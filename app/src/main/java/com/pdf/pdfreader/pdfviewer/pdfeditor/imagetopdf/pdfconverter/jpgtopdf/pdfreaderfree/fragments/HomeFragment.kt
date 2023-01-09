package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.fragments

import AdmobInterstitialAds
import InterstitialOnLoadCallBack
import InterstitialOnShowCallBack
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.BuildConfig
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.Showingclass
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.AdfList
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.FbInterstitialAds
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.FbInterstitialOnCallBack
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog.DeleteDialog
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog.DetailFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.DeleteClickListener
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.OptionsClick
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.isInternetConnected
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.dialog_file_rename.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.main_content.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*


class HomeFragment : Fragment(), OptionsClick, LifecycleOwner, DeleteClickListener {
    private val model: MainViewModel by sharedViewModel()
    private var mLastClickTime: Long = 0
    private lateinit var admobInterstitialAds: AdmobInterstitialAds
    private lateinit var fbInterstitialAds: FbInterstitialAds
    var fileUri: String? = null
    private val MIN_CLICK_INTERVAL: Long = 600
    private var files: ArrayList<PdfModels>? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var dir: File? = null
    var apfadaptor: AdfList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsConfig()
        setList()
        model.loadAllPdf(getDirectory())

    }

    private fun adsConfig() {
        fbInterstitialAds = FbInterstitialAds(requireActivity())
        admobInterstitialAds = AdmobInterstitialAds(requireActivity())
        loadInterstitialAd()
    }

    private fun setList() {
        model.allPdfList.observe(getViewLifecycleOwner()) {
            //   runBlocking { kotlinx.coroutines.delay(1000) }
            //  Log.e("list", "${it?.size}")
            files = arrayListOf()
            files?.clear()
            if (it.size != 0) {
                for (item: PdfModels in it) {
                    files?.add(item)
                }
                nofile.visibility = View.GONE

            } else {
                nofile.visibility = View.VISIBLE
            }
            apfadaptor = AdfList(files!!, this, requireActivity(), requireActivity())
            linearLayoutManager = LinearLayoutManager(requireContext())
            pdffileslist.adapter = apfadaptor
            pdffileslist.layoutManager = linearLayoutManager
            /*        pdffileslist.setOnLongClickListener(object :View.OnLongClickListener{
                        override fun onLongClick(p0: View?): Boolean {


                        }


                    })*/
            progress.visibility = View.GONE


        }


    }


    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<PdfModels> = ArrayList()

        // running a for loop to compare elements.
        for (item in files!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.filesName.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            apfadaptor!!.filterList(filteredlist)
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

    override fun onShareClick(uri: Uri) {
        var array: ArrayList<File> = arrayListOf()
        array.add(File(uri.toString()))
        shareFiless(
            array
        )
        /*     try {
                 var file: File = File(uri.toString())
                 var uri: Uri =
                     FileProvider.getUriForFile(this, getPackageName(), file);
                 var intent: Intent? = getPackageManager().getLaunchIntentForPackage(getPackageName());
                 if (intent == null) {
                     Toast.makeText(
                         this,
                         "Sorry, could not get launch intent for: " + getPackageName(),
                         Toast.LENGTH_LONG
                     ).show();
                     return;
                 }
                 intent.setAction(Intent.ACTION_VIEW);
                 intent.setDataAndType(uri, "application/pdf");
                 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                 startActivity(intent);
             } catch (e: IllegalArgumentException) {
                 e.printStackTrace();
                 Toast.makeText(this, "IllegalArgumentException: ${e.message}", Toast.LENGTH_LONG)
                     .show();

             } catch (e: Exception) {
                 e.printStackTrace();
                 Toast.makeText(this, "e.getMessage() ${e.message}", Toast.LENGTH_LONG).show();
             }
     */

//        val aName = intent.getStringExtra(uri)
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(this,File(this.getExternalFilesDir(null)?.absolutePath.toString(), "$aName")))
//        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        shareIntent.type = "application/pdf"
//        startActivity(Intent.createChooser(shareIntent, "share.."))
    }

    fun shareFiless(file: ArrayList<File>) {

        val uris = ArrayList<Uri>()
        /*     val uri = FileProvider.getUriForFile(
                 Objects.requireNonNull(getApplicationContext()),
                 BuildConfig.APPLICATION_ID + ".provider", file!!
             );*/

        for (path in file) {
            var uri = FileProvider.getUriForFile(
                Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider",
                path
            )
            uris.add(uri)
        }


        // uris.add(uri)
        shareFile(uris)
    }

    private fun shareFile(uris: ArrayList<Uri>) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putExtra(
            Intent.EXTRA_TEXT,
            this!!.getString(R.string.i_have_attached_pdfs_to_this_message)
        )
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = this!!.getString(R.string.pdf_type)
        this!!.startActivity(
            Intent.createChooser(
                intent,
                this!!.resources.getString(R.string.share_chooser)
            )
        )
    }

    override fun onDetails(uri: Uri, filename: String, fileFromPath: String, fileSize: String) {
        val modalBottomSheet = DetailFragment(uri.toString(), filename, fileFromPath, fileSize)
        modalBottomSheet.show(childFragmentManager, "DetailFragment")
    }

    override fun onDelete(uri: Uri, position: Int) {
        val fm: FragmentManager = requireActivity().getSupportFragmentManager();

        val editNameDialogFragment: DeleteDialog =
            DeleteDialog(uri, this, ::clicked, position)
        editNameDialogFragment.show(fm, "DeleteDailog")
    }

    fun clicked(clicked: Boolean, position: Int, uri: Uri) {
        try {
            if (clicked) {
                model.deleteFiles(position)
            } else {
            }
        } catch (ex: Exception) {


        }


    }

    override fun onOpen(uri: Uri) {
        fileUri = uri.toString()
        if (admobInterstitialAds.isInterstitialLoaded() || fbInterstitialAds.isInterstitialAdsLoaded() && isInternetConnected(
                requireContext()
            )
        ) {
            showInterstitialAd()
        } else {
            val currentClickTime = SystemClock.uptimeMillis()
            val elapsedTime = currentClickTime - mLastClickTime
            mLastClickTime = currentClickTime
            if (elapsedTime <= MIN_CLICK_INTERVAL) return
            val intent = Intent(requireContext(), Showingclass::class.java)
            intent.putExtra("Character", uri.toString())
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadInterstitialAd() {
        let { mActivity ->
            Log.i("projjljlj", "${RemoteConfigConstants.priorityInterstitial}")
            when (RemoteConfigConstants.priorityInterstitial) {
                1 -> {
                    Log.d(GeneralUtils.AD_TAG, "Call FB Interstitial")
                    fbInterstitialAds.loadInterstitialAd(
                        mActivity.resources.getString(R.string.facebook_interstital),
                        RemoteConfigConstants.interstitialActive,
                        false,
                        GeneralUtils.isInternetConnected(requireContext()),
                        object : FbInterstitialOnCallBack {
                            override fun onInterstitialDisplayed() {}

                            override fun onInterstitialDismissed() {
                                val currentClickTime = SystemClock.uptimeMillis()
                                val elapsedTime = currentClickTime - mLastClickTime
                                mLastClickTime = currentClickTime
                                if (elapsedTime <= MIN_CLICK_INTERVAL) return
                                val intent = Intent(requireContext(), Showingclass::class.java)
                                intent.putExtra("Character", fileUri.toString())
                                startActivity(intent)
                            }

                            override fun onError() {}

                            override fun onAdLoaded() {}

                            override fun onAdClicked() {}

                            override fun onLoggingImpression() {}

                        })
                }
                2 -> {
                    Log.d(GeneralUtils.AD_TAG, "Call Admob Interstitial")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        admobInterstitialAds.loadInterstitialAd(
                            mActivity.resources.getString(R.string.Interstitial),
                            RemoteConfigConstants.interstitialActive,
                            false,
                            GeneralUtils.isInternetConnected(requireContext()),
                            object : InterstitialOnLoadCallBack {
                                override fun onAdFailedToLoad(adError: String) {
                                    Log.e("admondsad", "onAdFailedToLoad")
                                }

                                override fun onAdLoaded() {

                                    Log.e("admondsad", "load")


                                }

                            })
                    }
                }
                else -> {}
            }
        }
    }

    private fun showInterstitialAd() {
        let { mActivity ->
            Log.i("projjljlj", "${RemoteConfigConstants.priorityInterstitial}")
            when (RemoteConfigConstants.priorityInterstitial) {
                1 -> {
                    loading_adlayoutd.visibility =
                        View.VISIBLE
                    Handler().postDelayed({
                        loading_adlayoutd.visibility =
                            View.GONE
                        fbInterstitialAds.showInterstitialAds()
                    }, 800)
                }
                2 -> {
                    loading_adlayoutd.visibility = View.VISIBLE
                    Handler().postDelayed({
                        loading_adlayoutd.visibility =
                            View.GONE
                        admobInterstitialAds.showInterstitialAd(object :
                            InterstitialOnShowCallBack {
                            override fun onAdDismissedFullScreenContent() {
                                val intent = Intent(requireContext(), Showingclass::class.java)
                                intent.putExtra("Character", fileUri.toString())
                                startActivity(intent)
                                loadInterstitialAd()
                                //view?.findNavController()?.navigate(R.id.action_navigation_indoor_exercise_to_navigation_exercise_start)
                            }

                            override fun onAdFailedToShowFullScreenContent() {

                            }

                            override fun onAdShowedFullScreenContent() {
                                //mInterstitialAd = null
                            }

                            override fun onAdImpression() {

                            }

                        })
                    }, 800)


                }
                else -> {}
            }
        }
    }


    override fun OnRenaming(filepath: String, position: Int, name: String) {
        val renameFileBuilder: AlertDialog.Builder?
        val renameFileDialog: AlertDialog?
        renameFileBuilder = AlertDialog.Builder(requireContext())
        var viewRateUsDialog = getLayoutInflater().inflate(R.layout.dialog_file_rename, null)
        renameFileBuilder.setView(viewRateUsDialog)
        renameFileDialog = renameFileBuilder.create()
        renameFileDialog.setCancelable(true)
        // renameFileDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewRateUsDialog.inputRenameDialog.setText(name)
        viewRateUsDialog.btnCancelRenameDialog.setOnClickListener {
            if (renameFileDialog.isShowing) {
                renameFileDialog.dismiss()
            }
        }
        viewRateUsDialog.btnOkRenameDialog.setOnClickListener {


            if (viewRateUsDialog.inputRenameDialog.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    R.string.snackbar_name_not_blank,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var file = File(viewRateUsDialog.inputRenameDialog.text.toString().trim())
                if (file.exists()) {

                    Toast.makeText(requireContext(), "file already exist", Toast.LENGTH_SHORT)
                        .show()


                } else {
                    model.renameFile(
                        filepath,
                        position,
                        viewRateUsDialog.inputRenameDialog.text.toString().trim(), ::renameFile
                    )
                    /*) {

                        //   apfadaptor?.notifyDataSetChanged()
                        *//*      Toast.makeText(this, R.string.snackbar_file_renamed, Toast.LENGTH_SHORT)
                                  .show()*//*
                    } else {
                        Toast.makeText(this, R.string.snackbar_file_not_renamed, Toast.LENGTH_SHORT)
                            .show()
                    }*/

                }

                if (renameFileDialog.isShowing) {
                    renameFileDialog.dismiss()
                }
            }
        }
        renameFileDialog.show()

        // renamingFile(filepath, position, name)    }
    }

    override fun onClick() {
        TODO("Not yet implemented")
    }


    fun renameFile(string: String) {
        //   Toast.makeText(this, "${string}", Toast.LENGTH_SHORT).show()

    }

    fun getDefaultStorageLocation(): String? {
        val dir = File(
            Environment.getExternalStorageDirectory().absolutePath,
            Constants.pdfDirectory
        )
        if (!dir.exists()) {
            val isDirectoryCreated = dir.mkdir()
            if (!isDirectoryCreated) {
                Log.e("Error", "Directory could not be created")
            }
        }
        return dir.absolutePath + Constants.PATH_SEPERATOR
    }

    override fun onDeleteOption(uri: Uri) {
    }

    override fun onCancel() {
    }
}