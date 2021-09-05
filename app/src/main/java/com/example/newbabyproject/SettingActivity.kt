package com.example.newbabyproject

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.newbabyproject.Visit.VisitParentCalendarActivity
import com.example.newbabyproject.utils.Common
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init(this@SettingActivity)

    }
    fun mOnClick(view: View) {
        when (view.id) {

            // 로그아웃
            R.id.logoutBtn ->{
                dlg.setTitle("로그아웃 알림")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        Logout()
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("아니오",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@SettingActivity,
                                "로그아웃하지 않습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                dlg.show()
            }

            R.id.unRegisterBtn ->{
                dlg.setTitle("회원탈퇴 알림")
                    .setMessage("회원탈퇴 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        //UnRegister()
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("아니오",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(
                                this@SettingActivity,
                                "로그아웃하지 않습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                dlg.show()

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
        intent = Intent(this@SettingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
        finish()
    }

    // 회원탈퇴
    private fun UnRegister(){

    }

    override fun onBackPressed() {
        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
        finish()
    } //뒤로가기 종료버튼


}