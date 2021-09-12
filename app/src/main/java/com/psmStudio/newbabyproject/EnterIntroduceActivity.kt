package com.psmStudio.newbabyproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_app_introduce.modifyBtn
import kotlinx.android.synthetic.main.activity_enter_introduce.*
import kotlinx.android.synthetic.main.item_toolbar.*

class EnterIntroduceActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_introduce)


        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "입원 안내문"
        setSupportActionBar(toolbar)

        init(applicationContext)
        introduceValidate("1")  // 입원 안내문

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if(loginId == "admin"){
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener{
                var intent = Intent(this@EnterIntroduceActivity, AppIntroduceModifyActivity::class.java)
                intent.putExtra("boardGubun", "1")
                intent.putExtra("actGubun", actGubun)
                if(actGubun == "update"){
                    intent.putExtra("content",enterIntroTxt.text)
                }
                startActivity(intent)
                finish()
            }
        }else{
            modifyBtn.visibility = View.GONE
        }

        introduceList("1")

    }
}