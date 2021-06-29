package com.example.newbabyproject.Visit

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.R
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_visit_admin_to_parent_detail.*
import kotlinx.android.synthetic.main.item_toolbar.*
import java.lang.Exception

class VisitAdminToParentDetailActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_to_parent_detail)

        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "면회내용 상세보기"
        setSupportActionBar(toolbar)

        init(this@VisitAdminToParentDetailActivity)

        try {
            val intent : Intent = intent
            if(intent.hasExtra("resultVisit")){
                val resultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "Detail Activity writeDateArray : ${resultVisit.toString()}")

                babyNameTxt.text = resultVisit.babyName + "아기 면회소식"
                writeNameTxt.text = "관리자"
                writeDateTxt.text = Common.dataSplitFormat(resultVisit.writeDate, "date")
                visitNoticeTxt.text = resultVisit.visitNotice
                babyWeightTxt.text = resultVisit.babyWeight
                babyLactationTxt.text = resultVisit.babyLactation
                babyRequireItemTxt.text = resultVisit.babyRequireItem
                babyEtcTxt.text = resultVisit.babyEtc
                Log.d("TAG", "Detail Activity Exceptilon ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "Detail Activity Exceptilon ${e.toString()}")
        }

    }
}