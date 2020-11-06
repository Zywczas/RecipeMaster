package com.zywczas.recipemaster.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.zywczas.recipemaster.R
import com.zywczas.recipemaster.utilities.*
import kotlinx.android.synthetic.main.dialog_save_image.*

class SaveImageDialog : DialogFragment() {

    private var arePermissionsGranted = false
    private val storageRequestCode by lazyAndroid { 777 }
    private val permissions by lazyAndroid { arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )}
    private lateinit var saveImageListener: SaveImageListener
    private val imageIndex by lazyAndroid { arguments?.get(EXTRA_VIEW_INDEX) as? Int }

    interface SaveImageListener {
        fun saveImage(viewId : Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveImageListener = context as SaveImageListener
        logD("przed null check: dialog pobiera view id: $imageIndex")
        if (imageIndex == null){
            logD("No arguments passed to dialog. Dialog closed in onAttach().")
            dialog?.dismiss()
        }
        logD("po null check: dialog pobiera view id: $imageIndex")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { //todo sprawdzic czy moze byc podane w konstruktorze
        return layoutInflater.inflate(R.layout.dialog_save_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyStoragePermissions()
        setupOnClickListeners()
    }

    private fun verifyStoragePermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
        ) {
            arePermissionsGranted = true
        }
    }

    private fun setupOnClickListeners(){
        no_textView_dialog.setOnClickListener{ dialog?.dismiss() }
        yes_textView_dialog.setOnClickListener(yesClick)
    }

    private val yesClick = View.OnClickListener {
        if (arePermissionsGranted){
            saveImageListener.saveImage(imageIndex!!)
            dialog?.dismiss()
        } else {
            askForPermissionsAndSavePhoto()
        }
    }

    private fun askForPermissionsAndSavePhoto() {
        ActivityCompat.requestPermissions(requireActivity(), permissions, storageRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            storageRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   //todo raczej nie potrzebne arePermissionsGranted = true
                    logD("permission granted: ${permissions[0]}")
                    logD("permission granted: ${permissions[1]}")
                    saveImageListener.saveImage(imageIndex!!)
                    dialog?.dismiss()
                } else {
                    showToast(getString(R.string.permission_warning))
                    dialog?.dismiss()
                }
            }
        }
    }

}