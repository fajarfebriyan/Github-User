package com.astro.test.fajarfebriyan.core.util.common

import android.app.ProgressDialog
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.astro.test.fajarfebriyan.R

object Loading {
    fun showDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(ContextCompat.getColor(context, android.R.color.transparent).toDrawable())
            it.setContentView(R.layout.progress_dialog)
            it.isIndeterminate = true
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
}