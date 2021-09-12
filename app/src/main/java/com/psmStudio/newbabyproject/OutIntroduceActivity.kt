package com.psmStudio.newbabyproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_app_introduce.*
import kotlinx.android.synthetic.main.activity_app_introduce.modifyBtn
import kotlinx.android.synthetic.main.activity_out_introduce.*
import kotlinx.android.synthetic.main.item_toolbar.*

class OutIntroduceActivity : BaseActivity() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_introduce)


        toolbar.setTitleTextColor(getColor(R.color.whiteColor))
        toolbar.title = "퇴원 안내문"
        setSupportActionBar(toolbar)

        init(applicationContext)
        introduceValidate("2")  // 퇴원 안내문

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if (loginId == "admin") {
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener {


                if (validate) {
                    val intent =
                        Intent(this@OutIntroduceActivity, AppIntroduceModifyActivity::class.java)
                    intent.putExtra("boardGubun", "2")
                    intent.putExtra("actGubun", actGubun)
                    if(actGubun == "update"){
                        intent.putExtra("content",outIntroTxt.text)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@OutIntroduceActivity,
                        "다시 시도 바랍니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        } else {
            modifyBtn.visibility = View.GONE
        }

        introduceList("2")

    }
}