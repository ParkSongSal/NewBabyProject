package com.example.newbabyproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.newbabyproject.utils.Common

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun mOnClick(view: View) {
        when(view.id){

            /* 앱 소개 */
            R.id.intro_card,
            R.id.appIntro,
            R.id.intro_img ->{
                Common.intentCommon(this@MainActivity, AppIntroduceActivity::class.java)
            }



            /* 설정 */
            R.id.settingImg,
            R.id.setting_card,
            R.id.settingBtn -> {
                Common.intentCommon(this@MainActivity, SettingActivity::class.java)
            }

        }
    }
}