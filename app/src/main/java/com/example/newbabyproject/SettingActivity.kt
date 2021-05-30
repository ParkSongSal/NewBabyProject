package com.example.newbabyproject

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    lateinit var setting : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init(applicationContext)

        logoutBtn.setOnClickListener{

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
                    }).show()

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
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
        finish()
    } //뒤로가기 종료버튼


}