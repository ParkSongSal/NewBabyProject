package com.example.newbabyproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_app_introduce.*

class AppIntroduceActivity : AppCompatActivity() {

    var loginId = ""


    // 아이디 저장 기능
    lateinit var setting : SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce)

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if(loginId == "admin"){
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener{
                var intent = Intent(this@AppIntroduceActivity, AppIntroduceModifyActivity::class.java)
                intent.putExtra("boardGubun", "0")
                startActivity(intent)
            }
        }else{
            modifyBtn.visibility = View.GONE
        }

    }
}

