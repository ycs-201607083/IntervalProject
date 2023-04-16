package com.example.intervalproject.Dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.intervalproject.R
import com.example.intervalproject.databinding.ActivtyDialogBinding

class CustomDialog constructor(mContext: Context): Dialog(mContext) {
    lateinit var dBinding: ActivtyDialogBinding
    init {
       setContentView(R.layout.activty_dialog)
       window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
       setCanceledOnTouchOutside(false)
       setCancelable(true)
    }
    
}