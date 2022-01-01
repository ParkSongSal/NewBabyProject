package com.psmStudio.newbabyproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.view.View
import android.widget.TextView


open class CustomDialog(context: Context?) : Dialog(context!!) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_dialog)
        val quit = findViewById<TextView>(R.id.dialog_btn_quit)
        val back = findViewById<TextView>(R.id.dialog_btn_back)


        // 종료 버튼
        quit.setOnClickListener { Process.killProcess(Process.myPid()) }

        // 취소 버튼
        back.setOnClickListener { dismiss() }
    }
}