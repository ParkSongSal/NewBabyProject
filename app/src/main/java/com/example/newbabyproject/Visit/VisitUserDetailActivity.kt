package com.example.newbabyproject.Visit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.BaseActivity
import com.example.newbabyproject.R
import java.util.*

class VisitUserDetailActivity : BaseActivity() {

    var writeDateAry : ArrayList<String>? = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_detail)

        init(this@VisitUserDetailActivity)

        if(intent != null){
            if(intent.hasExtra("resultVisit")){
                val resultVisit: ResultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "Detail Activity writeDateArray : ${resultVisit.toString()}")
            }
        }
    }
}