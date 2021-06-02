package com.example.newbabyproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.utils.Common

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(this@MainActivity)
    }

    fun mOnClick(view: View) {
        when (view.id) {

            /* 앱 소개 */
            R.id.intro_ll,
            R.id.appIntro,
            R.id.intro_img -> {
                Common.intentCommon(this@MainActivity, AppIntroduceActivity::class.java)
            }

            /* 입원 안내문 소개  */
            R.id.enter_ll,
            R.id.enterTxt,
            R.id.enter_img ->{
                Common.intentCommon(this@MainActivity, EnterIntroduceActivity::class.java)
            }

            /* 퇴원 안내문 소개*/
            R.id.leave_ll,
            R.id.leave_txt,
            R.id.leave_img ->{
                Common.intentCommon(this@MainActivity, OutIntroduceActivity::class.java)
            }

            R.id.logoutImg ->{
                dlg.setTitle("로그아웃 알림")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        Logout()
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("아니오",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@MainActivity,
                                "로그아웃하지 않습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                dlg.show()
            }

            /* 공지사항 */
            R.id.notice_ll,
            R.id.notice_txt,
            R.id.notice_img ->{
                Common.intentCommon(this@MainActivity, NoticeListActivity::class.java)
            }
        }
    }

    // 로그아웃
    private fun Logout() {
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.clear()
        //editor.commit();
        editor.apply()
        intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
        finish()
    }
}




           /* *//* 설정 *//*
            R.id.settingImg,
            R.id.setting_card,
            R.id.settingBtn -> {
                Common.intentCommon(this@MainActivity, SettingActivity::class.java)
                finish()
            }
*/