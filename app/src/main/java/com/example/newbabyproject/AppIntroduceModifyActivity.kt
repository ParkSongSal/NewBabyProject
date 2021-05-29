package com.example.newbabyproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce_modify.*

class AppIntroduceModifyActivity : AppCompatActivity() {
    var boardGubun :String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce_modify)

        if(intent != null){
            boardGubun = intent.getStringExtra("boardGubun")
        }else{
            Common.intentCommon(this@AppIntroduceModifyActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        when(boardGubun){
            "0" ->{
                board_gubun.text = "앱 소개"
                intent = Intent(this@AppIntroduceModifyActivity, AppIntroduceActivity::class.java)
            }
            "1" ->{
                board_gubun.text = "입원안내문"
                intent = Intent(this@AppIntroduceModifyActivity, EnterIntroduceActivity::class.java)
            }
        }

    }
}