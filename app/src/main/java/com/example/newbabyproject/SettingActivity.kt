package com.example.newbabyproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init(this@SettingActivity)

        loginId = setting.getString("loginId", "").toString()

    }
    fun mOnClick(view: View) {
        when (view.id) {

            // 공지게시판
            R.id.noticeBtn ->{
                Toast.makeText(applicationContext, "현재 등록된 공지가 없습니다.", Toast.LENGTH_SHORT).show()
            }

            // 로그아웃
            R.id.logoutBtn -> {
                dlg.setTitle("로그아웃 알림")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        Logout()
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("아니오",
                        null)
                dlg.show()
            }

            R.id.unRegisterBtn -> {
                dlg.setTitle("회원탈퇴 알림")
                    .setMessage("탈퇴시 회원님이 작성한 모든 게시물과\n회원정보가 삭제됩니다.\n탈퇴를 진행하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        if ("admin" == loginId) {
                            Toast.makeText(applicationContext, "관리자 계정은 탈퇴할수 없습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            //UnRegister()
                        }
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("아니오",
                        null
                        )
                dlg.show()

            }

            R.id.contact_us -> {
                val intent= Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                // email setting 배열로 해놔서 복수 발송 가능
                // email setting 배열로 해놔서 복수 발송 가능
                val address = arrayOf("dev_sari_190102@daum.net")
                intent.putExtra(Intent.EXTRA_EMAIL, address)
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(intent)
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