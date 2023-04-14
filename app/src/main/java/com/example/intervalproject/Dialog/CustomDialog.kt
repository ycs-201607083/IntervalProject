package com.example.intervalproject.Dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.intervalproject.R

class CustomDialog(mContext: Context) {
    private val dialog = Dialog(mContext)

    fun myDialog(){
        dialog.setContentView(R.layout.activty_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                        WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
    }
}