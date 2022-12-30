package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree

import AdmobInterstitialAds
import BannerCallBack
import GeneralUtils
import GeneralUtils.AD_TAG
import InterstitialOnLoadCallBack
import InterstitialOnShowCallBack
import RemoteConfigConstants
import RemoteConfigConstants.prioritySmallNative
import RemoteConfigConstants.smallNativeActive
import ThemeDialog
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.createDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.NativeAdLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.PdfWriter
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.AdfList
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adaptor.ThemeHelper
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.adsmanagement.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.dialog.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.models.PdfModels
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.DARK
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.DEFAULT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.LIGHT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.PATH_SEPERATOR
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.pdfDirectory
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.pdfExtension
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.AdViewModel
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.dialog_file_rename.view.*
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.top_haeder.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    Onlongprocess, OptionsClick, DeleteClickListener, OnBackpressListener, ThemeDailog,
    View.OnLongClickListener, OpenFileInt, createFileInterface {
    lateinit var mPath: String
    private var searchView: SearchView? = null
    private val SELECT_PICTURE = 1
    private var mImagesUri: ArrayList<String> = arrayListOf()
    lateinit var ads_container_layout: LinearLayout
    lateinit var drawer_layout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var admob_native_place_holder: FrameLayout
    lateinit var fb_native_place_holder: NativeAdLayout
    private lateinit var admobInterstitialAds: AdmobInterstitialAds
    private lateinit var fbInterstitialAds: FbInterstitialAds
    lateinit var loading_layout: FrameLayout
    private lateinit var fbBannerAds: FbBannerAds
    private lateinit var admobBannerAds: AdmobBannerAds
    var dir: File? = null
    private val MIN_CLICK_INTERVAL: Long = 600
    public var is_in_action_mode = false
    private val adViewModel: AdViewModel by viewModel()
    var count = 0
    var selectionlist: ArrayList<PdfModels?> = ArrayList<PdfModels?>()
    private var mLastClickTime: Long = 0
    private var facebookads = false
    private var interstitialAd: com.facebook.ads.InterstitialAd? = null
    lateinit var inAppPurchase: InAppPurchase
    val PERMISSION_REQUEST_CODE = 2296
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences> =
        createDataStore(name = "ui_mode_preference")
    var fileUri: String? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var apfadaptor: AdfList? = null
    private val model: MainViewModel by viewModel()
    private var files: ArrayList<PdfModels>? = null
    private var mInterstitialAd: InterstitialAd? = null
    var bottomDialogFragment: ExitBottomSheet? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLanguage()
        setContentView(R.layout.activity_main)
        drawer_layout = findViewById(R.id.drawer_layout)
        ads_container_layout = findViewById(R.id.ads_container_layout)
        admob_native_place_holder = findViewById(R.id.admob_native_place_holder)
        fb_native_place_holder = findViewById(R.id.fb_native_place_holder)
        loading_layout = findViewById(R.id.loading_layout)
        inAppPurchase = InAppPurchase(this)
        setSupportActionBar(toolbar)
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val hView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        val back = hView.findViewById<View>(R.id.backbutton) as ImageView
        bottomDialogFragment = ExitBottomSheet.newInstance(this@MainActivity)
        back.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        /* RemoteConfigEngine.facebook_ads {
             // Toast.makeText(this, "remoteconfig"+it, Toast.LENGTH_SHORT).show()
             if (it) {
                 facebookads = true
                 loadInterstitalFacebook()
             } else {
                 facebookads = false
                 loadInterstitialAdmob()
                 val adView = layoutInflater.inflate(R.layout.ad_unified, null) as NativeAdView
                 adViewModel.loadNativeAd(adView, ad_frame)
                 model.registerOpenAds()

             }
         }*/

        adsConfig()
        model.registerOpenAds()
        //mainViewModel.call()
        if (checkPermission()) {
            model.loadAllPdf(getDirectory())
            setList()
            setNavigationViewListener()
            createRecyclarView()
        } else {
            val fm: FragmentManager = supportFragmentManager
            val editNameDialogFragment: PermissionDialog =
                PermissionDialog() {
                    requestPermission()
                }
            editNameDialogFragment.show(fm, "permission")
        }


/*
        loadInterstitialAdmob()
*/
        counter.setOnClickListener {
            if (selectionlist.size == 0) {
                Toast.makeText(this, "Please select atleast one ", Toast.LENGTH_SHORT).show()
            } else if (selectionlist.size != 0) {
                /*        var array: ArrayList<File> = arrayListOf()
                        for (list in selectionlist) {
                            array?.add(File(list?.filePath))

                            //   shareFiless(File(list?.filePath))

                            //  apfadaptor?.deleteDone(list)
                            *//* val fdelete = File(list?.filePath)
                     if (fdelete.exists()) {
                         if (fdelete.delete()) {
                             Log.e("del", "file Deleted")
                             System.out.println("file Deleted :" + list?.filePath)
                         } else {
                             Log.e("del", "file not Deleted")

                             System.out.println("file not Deleted :" + list?.filePath)
                         }
                     }
                     apfadaptor?.notifyDataSetChanged()
 *//*
                }
                shareFiless(array)
                clear_actio_mode()
*/
            }


        }

        add.setOnClickListener {
            getPhotosForFile()
        }


        /*      btnRateUs.setOnClickListener {
                  val currentClickTime = SystemClock.uptimeMillis()
                  val elapsedTime = currentClickTime - mLastClickTime
                  mLastClickTime = currentClickTime
                  if (elapsedTime <= MIN_CLICK_INTERVAL) return@setOnClickListener
                  rateUs()
              }
              btnShareApp.setOnClickListener {
                  val currentClickTime = SystemClock.uptimeMillis()
                  val elapsedTime = currentClickTime - mLastClickTime
                  mLastClickTime = currentClickTime
                  if (elapsedTime <= MIN_CLICK_INTERVAL) return@setOnClickListener
                  shareApp()
              }
              btnRemoveAds.setOnClickListener {
                  val fm: FragmentManager = supportFragmentManager
                  val editNameDialogFragment: RemoveAds =
                      RemoveAds() {
                          inAppPurchase.productPurchase()
                          //Toast.makeText(this, "rsd", Toast.LENGTH_SHORT).show()
                      }
                  editNameDialogFragment.show(fm, "about")

              }
              etSearchText.addTextChangedListener(object :
                  TextWatcher {
                  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                  }

                  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


                  }

                  override fun afterTextChanged(s: Editable) {

                      try {
                          filter(s.toString())

                      } catch (e: Exception) {

                      }

                  }

                  //hideKeyboard(this@MainActivity)
              })*/


        /*    etSearchText.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->

                Toast.makeText(this, "csjdc", Toast.LENGTH_SHORT).show()

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearchText.getWindowToken(), 0)
            })*/

    }

    private fun getPhotosForFile() {

        getDefaultStorageLocation()
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)


    }

    private fun setFilePath(name: String): String {
        val folder: File = File(getDefaultStorageLocation())
        if (!folder.exists()) folder.mkdir()
        return getDefaultStorageLocation() + name + pdfExtension
    }


    fun getDefaultStorageLocation(): String? {
        val dir = File(
            Environment.getExternalStorageDirectory().absolutePath,
            pdfDirectory
        )
        if (!dir.exists()) {
            val isDirectoryCreated = dir.mkdir()
            if (!isDirectoryCreated) {
                Log.e("Error", "Directory could not be created")
            }
        }
        return dir.absolutePath + PATH_SEPERATOR
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (!activity.isFinishing) {
            val inputMethodManager = activity.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }


    }

    fun createPdfFile() {
        lifecycleScope.launch {

            Log.v("stage 1", "store the pdf in sd card")
            val document = Document(PageSize.A4, 50F, 50F, 50F, 50F)
            Log.v("stage 2", "Document Created")
            val documentRect = document.pageSize
            try {
                val writer = PdfWriter.getInstance(document, FileOutputStream(mPath))
                Log.v("Stage 3", "Pdf writer")
                document.open()
                Log.v("Stage 4", "Document opened")
                for (i in mImagesUri.indices) {
                    var quality: Int
                    quality = 30
                    /* if (StringUtils.getInstance().isNotEmpty(mQualityString)) {
                         quality = mQualityString.toInt()
                     }*/
                    val image = Image.getInstance(mImagesUri[i])
                    // compressionLevel is a value between 0 (best speed) and 9 (best compression)
                    val qualityMod = quality * 0.09
                    image.compressionLevel = qualityMod.toInt()
                    image.border = Rectangle.BOX
                    image.borderWidth = 5F
                    Log.v("Stage 5", "Image compressed $qualityMod")
                    val bmOptions = BitmapFactory.Options()
                    val bitmap = BitmapFactory.decodeFile(mImagesUri[i], bmOptions)
                    Log.v("Stage 6", "Image path adding")
                    val pageWidth: Float = document.pageSize.width - (50F + 50F)
                    val pageHeight: Float = document.pageSize.height - (50F + 50F)
                    image.scaleAbsolute(pageWidth, pageHeight)
                    image.setAbsolutePosition(
                        (documentRect.width - image.scaledWidth) / 2,
                        (documentRect.height - image.scaledHeight) / 2
                    )
                    Log.v("Stage 7", "Image Alignments")
                    document.add(image)
                    document.newPage()
                }
                Log.v("Stage 8", "Image adding")
                document.close()
                Log.v("Stage 7", "Document Closed$mPath")
                Log.v("Stage 8", "Record inserted in database")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                //   mSuccess = false
            }

        }


    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val register = menu.findItem(R.id.delete)
        if (is_in_action_mode) {
            register.isVisible = false
        } else if (is_in_action_mode) {
            register.isVisible = true
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        try {
            if (!isFinishing) {
                hideSoftKeyboard(this@MainActivity)
            }
        } catch (ex: Exception) {
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu1, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        //  sort = (TextView) menu.findItem(R.id.sort).getActionView();
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.setMaxWidth(Int.MAX_VALUE)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                filter(query)
                return false
            }
        })
/*        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        //  sort = (TextView) menu.findItem(R.id.sort).getActionView();
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.setMaxWidth(Int.MAX_VALUE)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                filter(query)
                return false
            }
        })*/


//        sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Log.e("pos", "fdff")
        if (id == R.id.share) {
            if (selectionlist.size == 0) {
                Toast.makeText(this, "Please select atleast one ", Toast.LENGTH_SHORT).show()
            } else if (selectionlist.size != 0) {
                var array: ArrayList<File> = arrayListOf()
                for (list in selectionlist) {
                    array?.add(File(list?.filePath))

                    //   shareFiless(File(list?.filePath))

                    //  apfadaptor?.deleteDone(list)
                    /* val fdelete = File(list?.filePath)
                     if (fdelete.exists()) {
                         if (fdelete.delete()) {
                             Log.e("del", "file Deleted")
                             System.out.println("file Deleted :" + list?.filePath)
                         } else {
                             Log.e("del", "file not Deleted")

                             System.out.println("file not Deleted :" + list?.filePath)
                         }
                     }
                     apfadaptor?.notifyDataSetChanged()
 */
                }
                shareFiless(array)
                clear_actio_mode()

            }

            return true
        } else if (id == R.id.delete) {
            if (selectionlist.size == 0) {
                Toast.makeText(this, "Please select atleast one ", Toast.LENGTH_SHORT).show()
            } else if (selectionlist.size != 0) {
                val fm: FragmentManager = supportFragmentManager
                val editNameDialogFragment: DeleteMultiDialog =
                    DeleteMultiDialog(object : Linstener {
                        override fun onListener(boolean: Boolean) {

                            if (boolean) {
                                var array: ArrayList<PdfModels> = arrayListOf()
                                for (list in selectionlist) {
                                    array.add(list!!)
                                    //   shareFiless(File(list?.filePath))
                                    // apfadaptor?.deleteDone(list)
                                    val fdelete = File(list?.filePath)
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            Log.e("del", "file Deleted")
                                            System.out.println("file Deleted :" + list?.filePath)
                                        } else {
                                            Log.e("del", "file not Deleted")

                                            System.out.println("file not Deleted :" + list?.filePath)
                                        }
                                    }

                                }
                                apfadaptor?.deleteFile(array)
                                apfadaptor?.notifyDataSetChanged()
                                // shareFiless(array)
                                clear_actio_mode()

                            } else {
                                clear_actio_mode()


                            }


                        }


                    })
                editNameDialogFragment.show(fm, "DeleteDailog")


            }
        } else if (id == R.id.crown) {
            removeAds()


        }
        return super.onOptionsItemSelected(item)
    }


    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.


            menuInflater.inflate(R.menu.ui_menu, menu)

        return true
    }


*/


    /*   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
           menuInflater.inflate(R.menu.ui_menu, menu)
           return true
       }
   */

    /* private fun filter(text: String) {
         // creating a new array list to filter our data.
         val filteredlist: ArrayList<CourseModal> = ArrayList()

         // running a for loop to compare elements.
         for (item in courseModalArrayList!!) {
             // checking if the entered string matched with any item of our recycler view.
             if (item.courseName.toLowerCase().contains(text.toLowerCase())) {
                 // if the item is matched we are
                 // adding it to our filtered list.
                 filteredlist.add(item)
             }
         }
         if (filteredlist.isEmpty()) {
             // if no item is added in filtered list we are
             // displaying a toast message as no data found.
             Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
         } else {
             // at last we are passing that filtered
             // list to our adapter class.
             adapter!!.filterList(filteredlist)
         }
     }*/


    private fun createRecyclarView() {


    }

    private fun setList() {
        model.allPdfList.observe(this) {
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

            apfadaptor = AdfList(files!!, this, this, this)
            linearLayoutManager = LinearLayoutManager(this)
            pdffileslist.adapter = apfadaptor
            pdffileslist.layoutManager = linearLayoutManager
            /*        pdffileslist.setOnLongClickListener(object :View.OnLongClickListener{
                        override fun onLongClick(p0: View?): Boolean {


                        }


                    })*/
            progress.visibility = View.GONE


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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_about_us -> {
                aboutUs()
                true
            }
            R.id.language -> {
                lanaguage()
                true
            }
            R.id.theme -> {
                val fm: FragmentManager = supportFragmentManager
                val editNameDialogFragment: ThemeDialog = ThemeDialog(this)
                editNameDialogFragment.show(fm, "ThemeDialog")
                // startActivity(Intent(this, SettingsActivity::class.java))
                drawer_layout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.nav_share_app -> {
                shareApp()
                true
            }
            R.id.nav_rate_us -> {
                rateUs()
                true
            }
            R.id.nav_privacy -> {
                navPrivacy()
                true
            }
            R.id.nav_remove_ads -> {
                removeAds()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun lanaguage() {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: LanguageFragment =
            LanguageFragment()
        editNameDialogFragment.show(fm, "language")
        drawer_layout.closeDrawer(GravityCompat.START)

        isFinishing


    }

    private fun restorePurchase() {
        Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show()
        drawer_layout.closeDrawer(GravityCompat.START)


    }

    private fun removeAds() {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: RemoveAds =
            RemoveAds() {
                inAppPurchase.productPurchase()
                //  Toast.makeText(this, "rsd", Toast.LENGTH_SHORT).show()
            }
        editNameDialogFragment.show(fm, "about")
        drawer_layout.closeDrawer(GravityCompat.START)

    }

    private fun navPrivacy() {

        privacyLink()
        drawer_layout.closeDrawer(GravityCompat.START)

    }

    private fun rateUs() {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: RateUsDailog =
            RateUsDailog()
        editNameDialogFragment.show(fm, "fragment_edit_name")
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun shareApp() {
        shareApplication(this)
        //drawer_layout.closeDrawer(GravityCompat.START)

    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun aboutUs() {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: AboutUs =
            AboutUs()
        editNameDialogFragment.show(fm, "about")
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun feedback() {

    }

    private fun renamingFile(filePath: String, position: Int, newName: String): Boolean {
        val oldFile: File = File(filePath)
        val oldPath = oldFile.path
        val newFileName = (oldPath.substring(0, oldPath.lastIndexOf('/'))
                + "/" + newName + ".pdf")
        val newFile = File(newFileName)
        val newFilePath = newFile.path
        return if (oldFile.renameTo(newFile)) {
            apfadaptor?.renameFile(true, position, newName, newFilePath)
            /*        mainViewModel.allPdfs.value = mainViewModel.allPdfs.value?.mapIndexed { index, allPdfModel ->
                        if (index != position) {
                            allPdfModel
                        } else {
                            allPdfModel.apply {
                                filesName = newName
                                fileUri = newFilePath
                            }
                        }
                    }*/
            true
        } else {
            false
        }
    }

    private fun privacyLink() {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(getString(R.string.app_more_app_url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            if (!this.isFinishing) {
                Toast.makeText(
                    this,
                    getString(R.string.error_no_browser),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private fun setNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(this)
        /*  navigationicon.setOnClickListener {
              drawer_layout.openDrawer(GravityCompat.START)
          }*/
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("sffgeg", "gs")
        Toast.makeText(this, "dg", Toast.LENGTH_SHORT).show()
    }

    fun openCloseNavigationDrawer(view: View) {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
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

    fun uriFromFile(context: Context, file: File): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        } else {
            return Uri.fromFile(file)
        }
    }


    override fun onDetails(uri: Uri, filename: String, fileFromPath: String, fileSize: String) {
        val modalBottomSheet = DetailFragment(uri.toString(), filename, fileFromPath, fileSize)
        modalBottomSheet.show(supportFragmentManager, "DetailFragment")
    }

    override fun onDelete(uri: Uri, position: Int) {
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: DeleteDialog =
            DeleteDialog(uri, this, ::clicked, position)
        editNameDialogFragment.show(fm, "DeleteDailog")
    }


    fun clicked(clicked: Boolean, position: Int, uri: Uri) {
        try {
            if (clicked) {


                model.deleteFiles(position)


                /*    apfadaptor?.deleteDone(position)
                    if (uri != null) {
                        val fdelete = File(uri.path)
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                Log.e("del", "file Deleted")
                                System.out.println("file Deleted :" + uri.path)
                            } else {
                                Log.e("del", "file not Deleted")

                                System.out.println("file not Deleted :" + uri.path)
                            }
                        }
                    }

                    apfadaptor?.notifyDataSetChanged()*/
                //    files?.removeAt(position)

            } else {
            }
        } catch (ex: Exception) {


        }


    }

    override fun onOpen(uri: Uri) {

/*        if (facebookads) {
            fileUri = uri.toString()
            if (interstitialAd != null && interstitialAd!!.isAdLoaded()) {
                interstitialAd?.show()
                // etSearchText.getText().clear();
                // etSearchText.
            } else {
                //  etSearchText.getText().clear();
                val intent = Intent(applicationContext, Showingclass::class.java)
                intent.putExtra("Character", uri.toString())
                startActivity(intent)
            }

        } else {
            fileUri = uri.toString()
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
                // etSearchText.getText().clear();
                // etSearchText.
            } else {
                //  etSearchText.getText().clear();
                val intent = Intent(applicationContext, Showingclass::class.java)
                intent.putExtra("Character", uri.toString())
                startActivity(intent)
            }


        }*/
        fileUri = uri.toString()

        if (admobInterstitialAds.isInterstitialLoaded() || fbInterstitialAds.isInterstitialAdsLoaded() && isInternetConnected(
                this
            )
        ) {
            showInterstitialAd()
        } else {
            val currentClickTime = SystemClock.uptimeMillis()
            val elapsedTime = currentClickTime - mLastClickTime
            mLastClickTime = currentClickTime
            if (elapsedTime <= MIN_CLICK_INTERVAL) return
            val intent = Intent(applicationContext, Showingclass::class.java)
            intent.putExtra("Character", uri.toString())
            startActivity(intent)
        }
    }

    override fun OnRenaming(filepath: String, position: Int, name: String) {
        val renameFileBuilder: AlertDialog.Builder?
        val renameFileDialog: AlertDialog?
        renameFileBuilder = AlertDialog.Builder(this)
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
                Toast.makeText(this, R.string.snackbar_name_not_blank, Toast.LENGTH_SHORT).show()
            } else {
                var file = File(viewRateUsDialog.inputRenameDialog.text.toString().trim())
                if (file.exists()) {

                    Toast.makeText(this, "file already exist", Toast.LENGTH_SHORT).show()


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

        // renamingFile(filepath, position, name)
    }


    fun renameFile(string: String) {
        //   Toast.makeText(this, "${string}", Toast.LENGTH_SHORT).show()

    }

    override fun onClick() {


    }

    override fun onDeleteOption(uri: Uri) {

    }


    //    fun deleteFile(file: File): Boolean {
//        var deleted: Boolean
//        //Delete from Android Medialib, for consistency with device MTP storing and other apps listing content:// media
//        if (file.isDirectory) {
//            deleted = true
//            for (child in file.listFiles()) deleted = deleted and deleteFile(child)
//            if (deleted) deleted = deleted and file.delete()
//        } else {
//            val cr = AppContextProvider.appContext.contentResolver
//            try {
//                deleted = cr.delete(
//                    MediaStore.Files.getContentUri("external"),
//                    MediaStore.Files.FileColumns.DATA + "=?", arrayOf(file.path)) > 0
//            } catch (ignored: IllegalArgumentException) {
//                deleted = false
//            } catch (ignored: SecurityException) {
//                deleted = false
//            }
//            // Can happen on some devices...
//            if (file.exists()) deleted = deleted or file.delete()
//        }
//        return deleted
//    }
    override fun onCancel() {


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    model.loadAllPdf(getDirectory())
                    setList()
                    setNavigationViewListener()
                    createRecyclarView()

                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        /*    else if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {


                    Log.e("apth", "${PathUtils.getPath(this, Uri.parse(data.toString()))}")
              //      mImagesUri.add(PathUtils.getPath(this, Uri.parse(data.toString())))


                    // createPdfFile()
                }
            }*/
        else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                //data.getParcelableArrayExtra(name);
                //If Single image selected then it will fetch from Gallery
                mImagesUri.clear()
                if (data?.data != null) {
                    val mImageUri = data?.data
                    mImagesUri.add(PathUtils.getPath(this, mImageUri))
                    val fm: FragmentManager = supportFragmentManager
                    val editNameDialogFragment: CreateFile = CreateFile(this)
                    editNameDialogFragment.show(fm, "exit")
                    //   Log.v("LOG_TAG", "Selected Images" + mArrayUri.size)


                } else {
                    if (data?.clipData != null) {
                        val mClipData = data?.clipData
                        val mArrayUri = ArrayList<Uri>()
                        for (i in 0 until mClipData!!.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val uri = item.uri
                            mArrayUri.add(uri)
                            mImagesUri.add(PathUtils.getPath(this, uri))
                            Log.v("LOG_TAG", "Selected Images " + PathUtils.getPath(this, uri))
                        }
                        val fm: FragmentManager = supportFragmentManager
                        val editNameDialogFragment: CreateFile = CreateFile(this)
                        editNameDialogFragment.show(fm, "exit")
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size)
                    }
                }
            }
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

/*override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // below line is to get our inflater
    val inflater = menuInflater
    // inside inflater we are inflating our menu file.
    inflater.inflate(R.menu.ui_menu, menu)
    // below line is to get our menu item.
    val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
    // getting search view of our item.
    val searchView: SearchView = searchItem.getActionView() as SearchView
    // below line is to call set on query text listener method.
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            // inside on query text change method we are
            // calling a method to filter our recycler view.
            filter(newText)
            return false
        }
    })
    return true
}*/

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

    fun loadInterstitialAdmob() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, resources.getString(R.string.Interstitial), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.i("interst", "onAdLoaded")
                    mInterstitialAd!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            val intent = Intent(applicationContext, Showingclass::class.java)
                            intent.putExtra("Character", fileUri.toString())
                            startActivity(intent)
                            mInterstitialAd = null

                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {

                        }

                        override fun onAdShowedFullScreenContent() {
                            mInterstitialAd = null
                        }
                    })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.i("interst", loadAdError.message)
                    mInterstitialAd = null
                }
            })

    }

    override fun onBackPressed() {


        if (is_in_action_mode) {
            clear_actio_mode()
        } else if (!is_in_action_mode) {
            if (drawer_layout?.isDrawerOpen(GravityCompat.START)!!) {
                drawer_layout?.closeDrawer(GravityCompat.START)
            } else {

                showBottomSheetWithAd()
            }


            /*   val fm: FragmentManager = supportFragmentManager
               val editNameDialogFragment: ExitDialog =
                   ExitDialog(this)
               editNameDialogFragment.show(fm, "exit")*/
        } else {
            super.onBackPressed()
        }


        /*      if (!etSearchText.text.equals("")) {
                  etSearchText.setText("")
              } else if (etSearchText.text.equals("")) {

              }*/
    }

    override fun onYes() {
        finishAffinity()
    }

    override fun onNo() {


    }

    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    model.loadAllPdf(getDirectory())
                    setList()
                    setNavigationViewListener()
                    createRecyclarView()
                } else {

                }
            }
        }


    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)

        if (requestCode == 2296) {


        }


    }


    override fun onResume() {
        super.onResume()
        //  etSearchText.getText().clear();


    }

    override fun dark() {
        ThemeHelper.applyTheme(DARK)


    }

    override fun light() {
        ThemeHelper.applyTheme(LIGHT)
    }

    override fun system() {
        ThemeHelper.applyTheme(DEFAULT)

    }


    override fun onLongClickAction(position: Int) {
        Toast.makeText(this, "dff", Toast.LENGTH_SHORT).show()
        if (!is_in_action_mode) {
            is_in_action_mode = true
            count = count + 1
            files!![position]?.isSelected = true
            apfadaptor?.notifyDataSetChanged()
        }

    }

    override fun onSingleAction(check: View?, positon: Int) {
        val v = check as CheckBox
        if (v.isChecked) {
            count++
            files?.get(positon)?.isSelected = true
            Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show()
        } else if (!v.isChecked) {
            count--
            files?.get(positon)?.isSelected = false
            Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show()
        }
    }


    fun prepareselection(check: View, position: Int) {
        val v = check as CheckBox
        if (v.isChecked) {
            count = count + 1
            selectionlist.add(files?.get(position))
            updateCounter(count)
            files?.get(position)?.isSelected = true
        } else if (!v.isChecked) {
            selectionlist.remove(files?.get(position))
            count = count - 1
            updateCounter(count)
            files?.get(position)?.isSelected = false
        }
    }

    fun updateCounter(counter1: Int) {
        if (count == 0) {
            counter!!.text = "0 item selected"
        } else {
            counter!!.text = "$counter1 item selected"
        }
    }

    fun clear_actio_mode() {
        is_in_action_mode = false
        toolbar!!.menu.clear()
        toolbar!!.inflateMenu(R.menu.main_menu1)
        toolbar!!.title = resources.getString(R.string.app_name)

        //   supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        counter!!.visibility = View.GONE
        counter!!.text = "0 item Selected"
        count = 0
        selectionlist.clear()
        for (i in files?.indices!!) {
            files!![i]?.isSelected = false
        }
        apfadaptor?.notifyDataSetChanged()
    }

    override fun delete() {

    }

    override fun share() {


    }

    override fun restore() {

    }

    override fun play(uri: Uri?) {
    }

    override fun onLongClick(p0: View?): Boolean {
        toolbar!!.menu.clear()
        toolbar!!.title = ""
        toolbar!!.inflateMenu(R.menu.main_menu)
        /*   val searchItem: MenuItem =toolbar!!.inflateMenu(R.menu.main_menu).findItem(R.id.action_search)

           val searchManager = this@MainActivity.getSystemService(SEARCH_SERVICE) as SearchManager

           val searchView: SearchView? = null*/
        if (!is_in_action_mode) {
            is_in_action_mode = true
            counter!!.visibility = View.VISIBLE
            counter!!.text = "0 item Selected"
            apfadaptor?.notifyDataSetChanged()
            // supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        return true


    }

    fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun adsConfig() {
        let { mActivity ->
            fbBannerAds = FbBannerAds(mActivity)
            admobBannerAds = AdmobBannerAds(mActivity)
            fbInterstitialAds = FbInterstitialAds(this)
            admobInterstitialAds = AdmobInterstitialAds(this)
            loadInterstitialAd()
            when (prioritySmallNative) {
                1 -> {
                    Log.d(AD_TAG, "Call FB Native")
                    admob_native_place_holder.visibility = View.GONE
                    fb_native_place_holder.visibility = View.VISIBLE
                    fbBannerAds.loadNativeSmallAd(ads_container_layout,
                        fb_native_place_holder,
                        loading_layout,
                        getString(R.string.facebook_native),
                        smallNativeActive,
                        false,
                        isInternetConnected(mActivity),
                        object : FbNativeCallBack {
                            override fun onError(adError: String) {
                                ads_container_layout.visibility = View.GONE
                            }

                            override fun onAdLoaded() {}

                            override fun onAdClicked() {}

                            override fun onLoggingImpression() {}

                            override fun onMediaDownloaded() {}

                        })
                }

                2 -> {
                    Log.d(AD_TAG, "Call Admob Native")
                    admob_native_place_holder.visibility = View.VISIBLE
                    fb_native_place_holder.visibility = View.GONE
                    admobBannerAds.loadNativeAds(ads_container_layout,
                        admob_native_place_holder,
                        loading_layout,
                        getString(R.string.Native),
                        smallNativeActive,
                        false,
                        1,
                        isInternetConnected(mActivity),
                        object : BannerCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                                ads_container_layout.visibility = View.GONE
                            }

                            override fun onAdLoaded() {

                            }

                            override fun onAdImpression() {
                            }
                        }
                    )
                }
                else -> {
                    ads_container_layout.visibility = View.GONE
                }
            }
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
                        GeneralUtils.isInternetConnected(mActivity),
                        object : FbInterstitialOnCallBack {
                            override fun onInterstitialDisplayed() {}

                            override fun onInterstitialDismissed() {
                                val currentClickTime = SystemClock.uptimeMillis()
                                val elapsedTime = currentClickTime - mLastClickTime
                                mLastClickTime = currentClickTime
                                if (elapsedTime <= MIN_CLICK_INTERVAL) return
                                val intent = Intent(applicationContext, Showingclass::class.java)
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
                    admobInterstitialAds.loadInterstitialAd(
                        mActivity.resources.getString(R.string.Interstitial),
                        RemoteConfigConstants.interstitialActive,
                        false,
                        GeneralUtils.isInternetConnected(mActivity),
                        object : InterstitialOnLoadCallBack {
                            override fun onAdFailedToLoad(adError: String) {
                                Log.e("admondsad", "onAdFailedToLoad")
                            }

                            override fun onAdLoaded() {

                                Log.e("admondsad", "load")


                            }

                        })
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
                    loading_adlayoutd.visibility =
                        View.VISIBLE
                    Handler().postDelayed({
                        loading_adlayoutd.visibility =
                            View.GONE
                        admobInterstitialAds.showInterstitialAd(object :
                            InterstitialOnShowCallBack {
                            override fun onAdDismissedFullScreenContent() {
                                val intent = Intent(applicationContext, Showingclass::class.java)
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

    override fun open(path: String) {


        val intent = Intent(applicationContext, Showingclass::class.java)
        intent.putExtra("Character", path.toString())
        startActivity(intent)


    }

    override fun closeope() {


    }

    override fun create(path: String) {
        mPath = setFilePath(path)
        createPdfFile()
        model.addNewFile(mPath)
        val fm: FragmentManager = supportFragmentManager
        val editNameDialogFragment: FileName =
            FileName(this, path, mPath)
        editNameDialogFragment.show(fm, "exit")

    }

    override fun close() {
    }


    fun secondInterstitial() {


    }

    fun showBottomSheetWithAd() {
        try {
            supportFragmentManager.executePendingTransactions()
            val addTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            bottomDialogFragment?.let {
                try {
                    if (it.isAdded) {
                        val removeTransaction: FragmentTransaction =
                            supportFragmentManager.beginTransaction()
                        removeTransaction.remove(it)
                        removeTransaction.commitNow()
                    }
                    addTransaction.add(it, "ExitBottomSheet")
                    addTransaction.commitNow()
                } catch (ex: java.lang.IllegalStateException) {
                    finish()
                    ex.printStackTrace()
                } catch (ex: Exception) {
                    finish()
                    ex.printStackTrace()
                }
            }
        } catch (ex: java.lang.IllegalStateException) {
            finish()
            ex.printStackTrace()
        } catch (ex: Exception) {
            finish()
            ex.printStackTrace()
        }
    }


}



