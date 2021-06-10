package com.example.newbabyproject.Visit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.MainActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common

class VisitAdminCalendarActivity : BaseActivity() {
    var parentName : String? = ""
    var userId : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_calendar)

        init(this@VisitAdminCalendarActivity)

        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            userId = intent.getStringExtra("userId")
        }else{
            Common.intentCommon(this@VisitAdminCalendarActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }


    }
}