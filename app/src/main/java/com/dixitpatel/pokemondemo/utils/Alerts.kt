package com.dixitpatel.pokemondemo.utils


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.dixitpatel.pokemondemo.R
import com.dixitpatel.pokemondemo.databinding.DialogOneTwoButtonsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

/**
 *  Alerts class to show different types of alerts, and Dialogs
 */
object Alerts {

    private var dialog: Dialog? = null

    // Show Progress bar
    fun showProgressBar(context: Context?) {
        dismissProgressBar()
        try {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.dialog_progress)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setCancelable(false)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()
            val indeterminateMode: CircularProgressView = dialog!!.findViewById(R.id.progressBar)
            indeterminateMode.backgroundProgressBarColor =
                ContextCompat.getColor(context, R.color.transparent)
            indeterminateMode.backgroundProgressBarWidth = 4f
            indeterminateMode.progressBarWidth = 4f
            indeterminateMode.progressBarColor = ContextCompat.getColor(context, R.color.white)
            indeterminateMode.indeterminateMode = true
            indeterminateMode.roundBorder = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Dismiss Progress bar
    fun dismissProgressBar() {
        try {
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
                dialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Show BottomSheetDialog with yes no button.
    fun showBottomSheetSimpleConfirmationDialog(
        context: Activity, titleText: String?, subTitleText: String?,
        showOneButton: Boolean, negativeText: String?, positiveText: String?,
        onConfirmationDialog: OnConfirmationDialog?
    ) {
        try {
            val mBottomSheetDialog =
                BottomSheetDialog(context, R.style.AppBottomSheetDialogThemeWhiteWoShadow2)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.setCanceledOnTouchOutside(false)
            mBottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            val sheetView = context.layoutInflater.inflate(R.layout.dialog_one_two_buttons, null)
            val binding = DialogOneTwoButtonsBinding.bind(sheetView)
            mBottomSheetDialog.setContentView(binding.root)
            val v1 = sheetView.parent as View
            v1.setBackgroundColor(Color.TRANSPARENT)
            binding.tvTitle.text = titleText
            binding.tvPositive.text = positiveText
            if (subTitleText != null) {
                binding.tvSubTitle.text = subTitleText
                binding.tvSubTitle.visibility = View.VISIBLE
            } else {
                binding.tvSubTitle.visibility = View.GONE
            }
            if (negativeText != null) {
                binding.tvNegative.text = negativeText
            }
            if (showOneButton) {
                binding.tvNegative.visibility = View.GONE
            } else {
                binding.tvNegative.visibility = View.VISIBLE
            }
            binding.tvPositive.setOnClickListener {
                onConfirmationDialog?.onYes()
                mBottomSheetDialog.dismiss()
            }
            binding.tvNegative.setOnClickListener {
                onConfirmationDialog?.onNo()
                mBottomSheetDialog.dismiss()
            }
            mBottomSheetDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Show Snack bar
    @Throws(Exception::class)
    fun showSnackBar(context: Activity, message: String?) {
        try {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(context.findViewById<View>(R.id.container).windowToken, 0)
            val snackBar =
                Snackbar.make(context.findViewById(R.id.container), message!!, Snackbar.LENGTH_LONG)
            snackBar.show()
        } catch (e: Exception) {
            throw Exception("container id should be root Layout of each layout")
        }
    }

    interface OnConfirmationDialog {
        fun onYes()
        fun onNo()
    }

}