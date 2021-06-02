package com.example.newbabyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_app_introduce.*

class OutIntroduceActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_introduce)

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