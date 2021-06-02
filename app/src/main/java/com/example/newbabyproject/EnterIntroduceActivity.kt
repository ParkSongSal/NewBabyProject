package com.example.newbabyproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_app_introduce.*

class EnterIntroduceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_introduce)

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
                startActivity(intent)
                finish()
            }
        }else{
            modifyBtn.visibility = View.GONE
        }

        introduceList("1")

    }
}