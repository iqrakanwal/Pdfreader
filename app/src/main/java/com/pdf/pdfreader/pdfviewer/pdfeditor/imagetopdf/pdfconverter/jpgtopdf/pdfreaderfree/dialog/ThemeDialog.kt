import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.MainActivity
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.interfaces.ThemeDailog
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.DARK
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils.Constants.Companion.LIGHT
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.theme_layout_dialog.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ThemeDialog(var themeDailog: ThemeDailog) : DialogFragment() {
    private val mainViewModel: MainViewModel by sharedViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = inflater.inflate(R.layout.theme_layout_dialog, container, false)
        var body = view.findViewById(R.id.radio) as RadioGroup
        var light: RadioButton = view.findViewById(R.id.light)
        var dark: RadioButton = view.findViewById(R.id.dark)
        var cancel: TextView = view.findViewById(R.id.cancel)
     /*  var gettheme= run {
            var wFlashlight = ""
            runBlocking {
                wFlashlight =mainViewModel.getTheme() as String
            }
            wFlashlight
        }*/
        try {
            if (mainViewModel.isDarkTheme== LIGHT) {
                light.isChecked = true
            } else if(mainViewModel.isDarkTheme== DARK)  {
                dark.isChecked = true
            }
            // dark.isChecked = true

              /*light.isChecked = true

              system.isChecked = true*/


        } catch (ex: Exception) {


        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel.setOnClickListener {
            dismiss()
        }

        radio.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.light) {
                    themeDailog.light()
                    mainViewModel.setDarkTheme(LIGHT)
                    dismiss()
                } else if (checkedId == R.id.dark) {
                    mainViewModel.setDarkTheme(DARK)
                    themeDailog.dark()
                    ActivityCompat.recreate(context as MainActivity)
                    dismiss()
                }
               /* else if (checkedId == R.id.system_Ad) {
                    themeDailog.system()
                    mainViewModel.setDarkTheme(DEFAULT)
                    ActivityCompat.recreate(context as MainActivity)
                    dismiss()
                }*/
            })
    }
    fun ThemeDialog() {
        // doesn't do anything special
    }
}